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

/**
 * 
 */
public enum FSqrWorkerThreadLifecylce {

    /**
     * W Worker thread which is created but not yet pooled.
     */
    CREATED,

    /**
     * A worker thread which is currently in the pool
     */
    POOLED,

    /**
     * A worker thread which is currently taken from the pool, and gets assigned to a Task 
     */
    BORROWED,

    /**
     * The assignment is still not good enough, we want the thread to be ready to go,
     * Such that the workerthread can wake up and start the Task.
     */
    READYTOGO,

    /**
     * a thread which is about to start the payload, and does some initialization
     * 
     * run some initialization on the task? Task bookkeeping.
     */
    STARTING,

    /**
     * a task is currently running in this thread, Task bookkeeping.
     */
    RUNNING,

    /**
     * a task is finished and the state where the Workerthread can be returned to the threadpool.
     * 
     * maybe we want that the task can clean itself up (e.g. memory loss etc.) Task bookkeeping.
     */
    FINISHED;

    // TODO: TERMINATED?

    public static FSqrWorkerThreadLifecylce checkTransition( FSqrWorkerThreadLifecylce fromstate, FSqrWorkerThreadLifecylce toState ) {

        switch (toState) {
            // OK nice 
            case CREATED:
                break;

            case POOLED:
                // should either come from CREATED OR FROM FINISHED
                // is there a reason to put it into POOLED from BORROED?
                // otherwise lfecycleexception....
                break;

            case BORROWED:
                // ONLY FROM POOLED
                break;

            case READYTOGO:
                // ONLY FROM BORROWED
                break;

            case STARTING:
                // ONLY FROM READY TO GO
                break;

            case RUNNING:
                // ONLY FROM STARTING
                break;

            case FINISHED:
                // ONLY FROM RUNING
                break;
        }

        return toState;
    }

}
