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

    // volatile Runnable fsqrTask;
    volatile FSqrTask fsqrTask;

    /**
     * @param threadName
     */
    public FSqrWorkerThread( FSqrWorkerThreadPool threadPool, String threadName ) {
        super( threadName );

        this.workerThreadState = FSqrWorkerThreadLifecylce.CREATED;
        this.threadPool = threadPool;
        this.fsqrTask = null;
    }

    public void resetWorkerThread() {
        // TODO: clear assigned task.
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

    void finished() {
        this.workerThreadState = FSqrWorkerThreadLifecylce.checkTransition( this.workerThreadState, FSqrWorkerThreadLifecylce.FINISHED );

        try {
            onFinished();
        }
        catch (Exception e) {
            // TODO: handle exception
        }
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

        // inform the Threadpool to accept this thread back into the worker thread pool
        threadPool.workerComplete( this );
    }

    // TODO assign workload, and the workload is wrapped in the run function.

    /** 
     * {@inheritDoc}
     */
    @Override
    public void run() {
        // TODO: Decide we either use resume / suspend... or wait and notify....
        // 
        // TODO: SLEEP and DREAM AND Wait until you receive a workload... and you are good to go.
        // {
        //   then stay awake and alive and run this workload (FSqrTask)
        // }
    }

    public void runWorkload() {
        // TODO: Switch Lifecycle to started
        // TODO: Prepare the task

        // TODO: switch Lifeclycle to running
        // TODO: execute the task...

        finished();
    }

    public FSqrWorkerThreadLifecylce getWorkerThreadState() {
        return workerThreadState;
    }

    /**
     * @param taskToRun
     */
    public void assignTask( FSqrTask taskToRun ) {
        this.fsqrTask = taskToRun;
    }

    /**
     * 
     */
    public void startAssignedTask() {
        // TODO Auto-generated method stub
    }

}
