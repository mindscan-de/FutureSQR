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
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class FSqrWorkerThreadPool implements FSqrThreadPool {

    private final Deque<FSqrWorkerThread> createdWorkers;
    private final Deque<FSqrWorkerThread> pooledWorkers;
    private final Set<FSqrWorkerThread> borrowedWorkers;
    private final Deque<FSqrWorkerThread> finishedWorkers;
    private String threadPoolName;

    private boolean shutdownInitiated = false;
    private int threadPoolSize;

    /**
     * 
     */
    public FSqrWorkerThreadPool( int threadPoolSize, String threadPoolName ) {
        this.threadPoolSize = threadPoolSize;
        this.threadPoolName = threadPoolName;

        this.createdWorkers = new ArrayDeque<>( threadPoolSize + 1 );
        // TODO: actually this should be an ArrayDeque which is able to wake up the taskdispatcherthread. using the taskdspatcherthreadmutex 
        this.pooledWorkers = new ArrayDeque<>( threadPoolSize + 1 );
        this.borrowedWorkers = new HashSet<>( threadPoolSize + 1 );
        this.finishedWorkers = new ArrayDeque<>( threadPoolSize + 1 );

        // create threads and then put them into the created threads queue
        for (int i = 0; i < threadPoolSize; i++) {
            FSqrWorkerThread threadWorker = new FSqrWorkerThread( this, threadPoolName + "Worker-" + i );
            this.createdWorkers.addLast( threadWorker );
            // TODO: maybe have a set of all known threads as well, such we can easier do a threaddump, and a killall operation. also we can then dig for lost threads?
        }
    }

    public void initializeThreadPool() {
        FSqrWorkerThread createdWorker;

        // we move each thread from created to pooled 
        while ((createdWorker = createdWorkers.pollFirst()) != null) {

            // start thread (Thread.start())
            createdWorker.start();

            // put thread into the pooled queue
            addToPooled( createdWorker );
        }
    }

    // isWorkerThreadAvailable, looks if pooledQueue is not empty
    public boolean isWorkerThreadAvailable() {
        // no worker is available if shutown is going on.
        if (shutdownInitiated) {
            return false;
        }

        // if empty we try to collect all the finished threads, and then check if pooled Queue is still empty .. thats then the result.
        synchronized (pooledWorkers) {
            if (!pooledWorkers.isEmpty()) {
                return true;
            }
        }

        this.collectFinishedThreads();

        synchronized (pooledWorkers) {
            return !pooledWorkers.isEmpty();
        }
    }

    // TODO BORROW... we can only borrow, if isWorkerAvaiable, this must be checked before, otherwise 
    // borrow thread must not be called. this method should never return null, instead throw an illegal state exception
    public FSqrWorkerThread borrowThread() {
        // TODO: you cant borrow if shutdown is going on....
        if (shutdownInitiated) {

        }

        FSqrWorkerThread borrowedWorker;
        // we look for a pooled thread in the pooled list, and pull the first. (synchronized)
        synchronized (pooledWorkers) {
            borrowedWorker = pooledWorkers.pollFirst();
        }

        if (borrowedWorker == null) {
            if (isShutdownInitiated()) {
                // actually we are in shutdown mode, we should not start or borrow new threads
            }
            else {
                // REEEE this queue was empty, throw an illegal state exception....
                throw new IllegalStateException( "make sure a workerthread is available using #isWorkerThreadAvailable(), there is no workerthread for you." );
            }
        }

        // TODO we should have a check for shutdown --- we should then invoke terminated on borrowed worker?

        // we tell the thread that this thread is now borrowed.
        borrowedWorker.borrowed();

        // we add the thread to the borrowed queue
        synchronized (borrowedWorkers) {
            borrowedWorkers.add( borrowedWorker );
        }
        return borrowedWorker;
    }

    // workerComplete() is used to inform the pool that this worker thread is available again after it was borrowed.
    public void workerComplete( FSqrWorkerThread finishedThread ) {
        if (finishedThread == null) {
            throw new IllegalArgumentException( "the finished thread must not be null." );
        }

        boolean result;

        synchronized (borrowedWorkers) {
            result = borrowedWorkers.remove( finishedThread );
        }

        if (result == false) {
            // we should have found this in the borrowed Workers ...
            return;
        }

        if (isShutdownInitiated()) {
            // TODO: actually we should shut down the thread via join (and not return it into the queue)
            // TODO: remove from full threadlist.
            // finishedThread.terminated();
        }
        else {
            // we take it from the borrowed queue to finished queue
            synchronized (finishedWorkers) {
                finishedWorkers.addLast( finishedThread );
            }

            // TODO: maybe call the collect finished threads, to unlock the WorkerThreadPool, who might wait for this. 
        }
    }

    // we take all threads from the finished queue declare them pooled and add them to the pooled Deque
    public void collectFinishedThreads() {
        FSqrWorkerThread finishedWorker;

        // only transfer a maximum number of threadpool size to the pooledWorkers
        for (int i = 0; i < this.threadPoolSize; i++) {
            synchronized (finishedWorkers) {
                finishedWorker = finishedWorkers.pollFirst();
            }

            // if no thread in finished workers found, we can quit collecting finished threads
            if (finishedWorker == null) {
                break;
            }

            if (isShutdownInitiated()) {
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

        // TODO: we should also let a dispatcher know, that an element was added to the pool.
        // problem is that this is run while "collectFinishedThreads" in "isWorkerThreadAvailable" is called, 
        // which means that this will essentially become a infinite wait
    }

    public void gracefulShutdownThreadPool() {
        this.shutdownInitiated = true;
    }

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
