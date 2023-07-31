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

/**
 * 
 */
public class FSqrWorkerThreadPool {

    private final Deque<FSqrWorkerThread> createdWorkers;
    private final Deque<FSqrWorkerThread> pooledWorkers;
    private final Deque<FSqrWorkerThread> borrowedWorkers;
    private final Deque<FSqrWorkerThread> finishedWorkers;

    /**
     * 
     */
    public FSqrWorkerThreadPool( int threadPoolSize ) {
        // TODO maybe provide a threadprefix and a name for the threadpool??

        this.createdWorkers = new ArrayDeque<>( threadPoolSize + 1 );
        this.pooledWorkers = new ArrayDeque<>( threadPoolSize + 1 );
        // MAYBE implement the borrowed Workers as a set...
        this.borrowedWorkers = new ArrayDeque<>( threadPoolSize + 1 );
        this.finishedWorkers = new ArrayDeque<>( threadPoolSize + 1 );

        // TODO create threads and then put them into the created threads queue
    }

    // TODO: initialize threadpool
    // we move each thread from created, 
    // tell each thread that it is now pooled and
    // start thread (Thread.start())
    // put them into the pooled queue

    // TODO BORROW...
    public FSqrWorkerThread borrowThread() {
        // TODO we look for a pooled thread in the pooled list, and pull the first. (synchronized)

        // we tell the thread that this thread is now borrowed.
        // we add the thread to the borrowed queue

        return null;
    }

    // TODO finishedThread()
    // we take it from the borrowed queue to finished queue

    // isWorkerThreadAvailable, looks if pooledQueue is not empty. 
    // if empty we try to collect all the finished threads, and then check if pooled Queue is still empty .. thats then the result.

    // TODO collectFinishedThreads
    // we take all threads from the finished queue declare them pooled and add them to the pooled Deque

}
