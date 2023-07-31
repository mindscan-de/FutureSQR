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
package de.mindscan.futuresqr.core.dispatcher.impl;

import de.mindscan.futuresqr.core.dispatcher.TaskDispatcher;
import de.mindscan.futuresqr.core.queue.ThreadBoundArrayDeque;
import de.mindscan.futuresqr.core.task.FSqrTask;

/**
 * 
 */
public class SimpleTaskDispatcherImpl implements TaskDispatcher {

    private ThreadBoundArrayDeque<FSqrTask> taskQueue;

    // TODO some taskpool....

    /**
     * 
     */
    public SimpleTaskDispatcherImpl() {
        // TODO Auto-generated constructor stub
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setTaskQueue( ThreadBoundArrayDeque<FSqrTask> taskQueue ) {
        this.taskQueue = taskQueue;

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void dispatchTask( FSqrTask taskToDispatch ) {
        if (taskToDispatch == null) {
            return;
        }

        if (this.taskQueue != null) {
            this.taskQueue.add( taskToDispatch );
        }
        else {
            throw new IllegalStateException( "#setTaskQueue was either not invoked or got an illegal taskQueue" );
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isTaskWorkerAvailable() {
        // TODO Auto-generated method stub
        // TODO delegate to threadpool and ask threadpool, whether there is a thread available.
        return false;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void runTask( FSqrTask taskToRun ) {
        // TODO Auto-generated method stub
        // get a workerthread from the worker threadpool
        // then delegate the execution of the task to run to the WorkerThread
        // and the workerthread then returns itself back to the theeadpool, 
        // when either the task throws and exception or is ready.

        // maybe we want to allocate between long tasks and short tasks, such that there is always capacity for short tasks.
        // maybe we just have zwo taskdispatchers, one for long running jobs and one for fast jobs, instead of writing complex allocation logic.

        // workerThread = borrowFrom(threadPool);
        // workerThread knowsHis own threadpool, so workerthread can announce itself
        // workerThread.runTask(taskToRun);
    }

}
