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
public class FSqrWorkerThreadPool {

    private final Deque<FSqrWorkerThread> createdWorkers;
    private final Deque<FSqrWorkerThread> pooledWorkers;
    private final Set<FSqrWorkerThread> borrowedWorkers;
    private final Deque<FSqrWorkerThread> finishedWorkers;
    private String threadName;

    private boolean shutdownInitiated = false;

    /**
     * 
     */
    public FSqrWorkerThreadPool( int threadPoolSize, String threadName ) {
        this.threadName = threadName;

        this.createdWorkers = new ArrayDeque<>( threadPoolSize + 1 );
        this.pooledWorkers = new ArrayDeque<>( threadPoolSize + 1 );
        this.borrowedWorkers = new HashSet<>( threadPoolSize + 1 );
        this.finishedWorkers = new ArrayDeque<>( threadPoolSize + 1 );

        // create threads and then put them into the created threads queue
        for (int i = 0; i < threadPoolSize; i++) {
            FSqrWorkerThread threadWorker = new FSqrWorkerThread( this, threadName + "Worker-" + i );
            this.createdWorkers.addLast( threadWorker );
        }
    }

    public void initializeThreadPool() {
        FSqrWorkerThread createdWorker;

        // we move each thread from created to pooled 
        while ((createdWorker = createdWorkers.pollFirst()) != null) {

            // tell each thread that it is now pooled
            createdWorker.pooled();

            // start thread (Thread.start())
            createdWorker.start();

            // put thread into the pooled queue
            synchronized (pooledWorkers) {
                pooledWorkers.addLast( createdWorker );
            }
        }
    }

    // TODO BORROW... we can only borrow, if isWorkerAvaiable, this must be checked before, otherwise 
    // borrow thread must not be called. this method should never return null, instead throw an illegal state exception
    public FSqrWorkerThread borrowThread() {
        FSqrWorkerThread borrowedWorker;
        // we look for a pooled thread in the pooled list, and pull the first. (synchronized)
        synchronized (pooledWorkers) {
            borrowedWorker = pooledWorkers.pollFirst();
        }

        if (borrowedWorker == null) {
            if (shutdownInitiated) {
                // actually we are in shutdown mode, we should not start or borrow new threads
            }
            else {
                // TODO: REEEE this queue was empty, throw an illegal state exception....
            }
        }

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

        if (shutdownInitiated) {
            // TODO: actually we should shut down the thread via join (and not return it into the queue)
        }
        else {
            // we take it from the borrowed queue to finished queue
            synchronized (finishedWorkers) {
                finishedWorkers.addLast( finishedThread );
            }
        }
    }

    // isWorkerThreadAvailable, looks if pooledQueue is not empty. 
    // if empty we try to collect all the finished threads, and then check if pooled Queue is still empty .. thats then the result.

    // TODO collectFinishedThreads
    // we take all threads from the finished queue declare them pooled and add them to the pooled Deque

    public void gracefulShutdownThreadPool() {
        this.shutdownInitiated = true;
    }

}
