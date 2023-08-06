/**
 * 
 * MIT License
 *
 * Copyright (c) 2023 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.futuresqr.core.thread;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The FSqrWorkerThreadPool manages a number of FSqrWorkerThread, their creation, their 
 * availability in a pool and if they are borrowed or finished with the assigned task.
 *  
 * These WorkerThreads have different states within the pool. A WorkerThread is either
 * created, pooled, borrowed and finished. The ThreadPool is currently implemented as
 * a kind of static thread pool, where each worker thread lives forever. Probably this
 * has to change later, where the thread pool creates and finishes threads all the time,
 * such they are "fresh" and without baggage, and if they throw an exception can still 
 * be reused - or may end in a deadlock.
 * 
 * But for now this concept is good enough and just a proof-of-concept.
 * 
 * TODO: this whole task/worker/dispatcher concept has to be simplified much more...
 */
public class FSqrWorkerThreadPool implements FSqrThreadPool {

    private final Deque<FSqrWorkerThread> createdWorkers;
    private final Deque<FSqrWorkerThread> pooledWorkers;
    private final Set<FSqrWorkerThread> borrowedWorkers;
    private final Deque<FSqrWorkerThread> finishedWorkers;
    private final List<FSqrWorkerThread> allKnownWorkers;

    private final String threadPoolName;
    private final int threadPoolSize;

    private boolean shutdownInitiated = false;

    /**
     * 
     */
    public FSqrWorkerThreadPool( int threadPoolSize, String threadPoolName ) {
        this.threadPoolSize = threadPoolSize;
        this.threadPoolName = threadPoolName;

        this.createdWorkers = new ArrayDeque<>( threadPoolSize + 1 );
        this.pooledWorkers = new ArrayDeque<>( threadPoolSize + 1 );
        this.borrowedWorkers = new HashSet<>( threadPoolSize + 1 );
        this.finishedWorkers = new ArrayDeque<>( threadPoolSize + 1 );
        this.allKnownWorkers = new ArrayList<>( threadPoolSize + 1 );

        createThreadPool( threadPoolSize, threadPoolName );
    }

    private void createThreadPool( int threadPoolSize, String threadPoolName ) {
        for (int i = 0; i < threadPoolSize; i++) {
            FSqrWorkerThread threadWorker = new FSqrWorkerThread( this, threadPoolName + "Worker-" + i );

            this.createdWorkers.addLast( threadWorker );
            this.allKnownWorkers.add( threadWorker );
        }
    }

    @Override
    public void initializeThreadPool() {
        FSqrWorkerThread createdWorker;

        // move each thread from created to pooled 
        while ((createdWorker = createdWorkers.pollFirst()) != null) {
            createdWorker.start();
            addToPooled( createdWorker );
        }
    }

    @Override
    public boolean isWorkerThreadAvailable() {
        if (isShutdownInitiated()) {
            return false;
        }

        synchronized (pooledWorkers) {
            if (!pooledWorkers.isEmpty()) {
                return true;
            }
        }

        this.recycleFinishedThreads();

        synchronized (pooledWorkers) {
            return !pooledWorkers.isEmpty();
        }
    }

    @Override
    public FSqrWorkerThread borrowThread() {
        if (isShutdownInitiated()) {
            throw new IllegalStateException( "shutdown is active, no more threads can get borrowed." );
        }

        FSqrWorkerThread borrowedWorker;

        synchronized (pooledWorkers) {
            borrowedWorker = pooledWorkers.pollFirst();
        }

        if (borrowedWorker == null) {
            throw new IllegalStateException( "make sure a workerthread is available using #isWorkerThreadAvailable(), there is no workerthread available." );
        }

        borrowedWorker.borrowed();

        synchronized (borrowedWorkers) {
            borrowedWorkers.add( borrowedWorker );
        }

        return borrowedWorker;
    }

    @Override
    public void workerComplete( FSqrWorkerThread completedThread ) {
        if (completedThread == null) {
            throw new IllegalArgumentException( "the completed thread must not be null." );
        }

        boolean result;

        synchronized (borrowedWorkers) {
            result = borrowedWorkers.remove( completedThread );
        }

        if (result == false) {
            // UNDECIDED MXM:
            // we should have found this in the borrowed Workers ...
            // maybe something went wrong, but we currently don't know
            // maybe this should be logged, if a logging framework becomes available during development
            return;
        }

        if (isShutdownInitiated()) {
            // UNDECIDED MXM:
            // actually we should shut down the thread via join (and not return it into the queue)
            // remove from allKnownWorkers thread list.
            // maybe put them into a terminated list as well instead of terminated.
            // maybe have a thread which will terminate threads, which will be terminated, when the last thread is terminated.
            // since this is called from the completed Thread itself, this might be a wrong decision here.
            // completedThread.terminated();
        }
        else {
            // we take it from the borrowed queue to finished queue
            synchronized (finishedWorkers) {
                finishedWorkers.addLast( completedThread );
            }

            // UNDECIDED MXM:
            // maybe call the collect finished threads, to unlock the WorkerThreadPool, who might wait for this.
            // that would introduce different thread calls on recycleFinihesThreads
            // this.recycleFinishedThreads():
        }
    }

    @Override
    public void recycleFinishedThreads() {
        FSqrWorkerThread finishedWorker;

        // only transfer a maximum number of threadpool size to the pooledWorkers
        for (int i = 0; i < this.threadPoolSize; i++) {
            synchronized (finishedWorkers) {
                finishedWorker = finishedWorkers.pollFirst();
            }

            if (finishedWorker == null) {
                // if no thread any more, we quit recycling finished threads
                break;
            }

            if (isShutdownInitiated()) {
                // UNDECIDED MXM:
                // if we are in shutdown mode, we don't forward this thread to the pooled workers any more.
                // or we terminate them?
                break;
            }
            else {
                addToPooled( finishedWorker );
            }
        }
    }

    private void addToPooled( FSqrWorkerThread workerThread ) {
        // first declare the element pooled
        workerThread.pooled();

        // only then add it to the pooled workers again 
        synchronized (pooledWorkers) {
            pooledWorkers.addLast( workerThread );
        }

        // only then wake up a waiting mutex, that may poll the pooled worker "instantly", otherwise we have 
        // lifecycle exceptions and such.

        // UNDECIDED MXM:
        // we should also let a dispatcher know, that an element was added to the pool.
        // problem is that this is run while "collectFinishedThreads" in "isWorkerThreadAvailable" is called, 
        // which means that this will essentially become a infinite wait
    }

    @Override
    public void gracefulShutdownThreadPool() {
        this.shutdownInitiated = true;
    }

    @Override
    public boolean isShutdownInitiated() {
        return shutdownInitiated;
    }

    public String getThreadPoolName() {
        return threadPoolName;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public int getNumberOfPooledThreads() {
        synchronized (pooledWorkers) {
            return pooledWorkers.size();
        }
    }

    public int getNumberOfBorrowedThreads() {
        synchronized (borrowedWorkers) {
            return borrowedWorkers.size();
        }
    }

    public int getNumberOfFinishedThreads() {
        synchronized (finishedWorkers) {
            return finishedWorkers.size();
        }
    }

    public void printThreadDump() {
        // we want to dump every threadstate and every worker.
    }
}
