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

import de.mindscan.futuresqr.core.task.FSqrTask;

/**
 * actually the worker threads are started, then they wait until a task is assigned
 * and the will run that assigned task until it finishes, then 
 */
public class FSqrWorkerThread extends FSqrThread {

    // TODO: maybe implement this as a composition instead of inheritance from Thread...

    private FSqrWorkerThreadLifecylce workerThreadState;
    private FSqrWorkerThreadPool threadPool;

    private Object runAssignedTaskMonitor = new Object();
    volatile boolean runAssignedTask = false;

    volatile FSqrTask fsqrTask;
    private boolean shutdownWorker = false;

    /**
     * @param threadName
     */
    public FSqrWorkerThread( FSqrWorkerThreadPool threadPool, String threadName ) {
        super( threadName );

        this.workerThreadState = FSqrWorkerThreadLifecylce.CREATED;
        this.threadPool = threadPool;
        this.fsqrTask = null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void run() {
        while (!shutdownWorker) {

            // we wait until a task is assigned 
            while (!runAssignedTask && !shutdownWorker) {
                try {
                    runAssignedTaskMonitor.wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (shutdownWorker) {
                return;
            }

            // TODO: ready to go....

            if (fsqrTask != null) {
                runWorkload();
            }
        }
    }

    public void runWorkload() {
        // TODO: Switch Lifecycle to started

        // PHASE PREPARE TASK.
        try {

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: switch Lifeclycle to running

        // PHASE RUN
        try {
            // execute the task... / external code may throw exceptions, we need to catch these, such that the
            // worker can be recycled.
            this.fsqrTask.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // PHASE CLEANUP TASK / FINISH TASK 
        try {

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        finished();
    }

    void finished() {
        stopAssignedTask();
        this.workerThreadState = FSqrWorkerThreadLifecylce.checkTransition( this.workerThreadState, FSqrWorkerThreadLifecylce.FINISHED );

        try {
            onFinished();
        }
        catch (Exception e) {
            // TODO: handle exception
        }
    }

    public FSqrWorkerThreadLifecylce getWorkerThreadState() {
        return workerThreadState;
    }

    public void assignTask( FSqrTask taskToRun ) {
        this.fsqrTask = taskToRun;
    }

    public void startAssignedTask() {
        this.runAssignedTask = true;
        runAssignedTaskMonitor.notify();
    }

    public void stopAssignedTask() {
        this.runAssignedTask = false;
        runAssignedTaskMonitor.notify();
    }

    public void quitWorker() {
        this.shutdownWorker = true;
        runAssignedTaskMonitor.notify();
    }

    /**
     * inform worker thread, that this thread is now pooled. 
     */
    void pooled() {
        this.workerThreadState = FSqrWorkerThreadLifecylce.checkTransition( this.workerThreadState, FSqrWorkerThreadLifecylce.POOLED );

        // onPooled()
    }

    /**
     * inform worker thread,  
     */
    void borrowed() {
        this.workerThreadState = FSqrWorkerThreadLifecylce.checkTransition( this.workerThreadState, FSqrWorkerThreadLifecylce.BORROWED );

        // onBorrowed()
    }

    void terminated() {
        this.workerThreadState = FSqrWorkerThreadLifecylce.checkTransition( this.workerThreadState, FSqrWorkerThreadLifecylce.TERMINATED );

        // TODO we have to clean up this worker, such as weith resetWorkerState and interrupt the run method...
        // maybe terminated should issue the join command on this thread.
    }

    /**
     * 
     */
    protected void onFinished() {
        // cleanup and reset ourselves
        this.resetWorkerThread();

        // inform the Thread pool to accept this thread back into the worker thread pool
        threadPool.workerComplete( this );
    }

    public void resetWorkerThread() {
        this.runAssignedTask = false;
        this.assignTask( null );
    }

}
