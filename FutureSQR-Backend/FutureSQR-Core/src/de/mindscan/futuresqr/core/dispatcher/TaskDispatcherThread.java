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
package de.mindscan.futuresqr.core.dispatcher;

import de.mindscan.futuresqr.core.queue.ThreadBoundArrayDeque;
import de.mindscan.futuresqr.core.task.FSqrNopTask;
import de.mindscan.futuresqr.core.task.FSqrTask;
import de.mindscan.futuresqr.core.thread.FSqrThread;
import de.mindscan.futuresqr.core.thread.FSqrThreadPool;

/**
 * This is a thread running a TaskDispatcher using the TaskDispatcher implementation and a given thread pool.
 */
public class TaskDispatcherThread extends FSqrThread {

    private ThreadBoundArrayDeque<FSqrTask> taskQueue;
    private TaskDispatcher taskDispatcher;
    private FSqrThreadPool threadPool;

    private volatile boolean shutdown = false;

    public TaskDispatcherThread( TaskDispatcher taskDispatcher, FSqrThreadPool threadPool ) {
        super( "FSqr-TaskDispatcher-Thread" );

        this.taskDispatcher = taskDispatcher;
        this.threadPool = threadPool;

        this.taskQueue = new ThreadBoundArrayDeque<FSqrTask>( this );
        this.taskDispatcher.setTaskQueue( this.taskQueue );
    }

    @Override
    public void run() {
        try {
            // UNDECIDED MXM:
            // Not sure whether I like this design/implementation idea, but for now it is just conserving the idea. 
            while (!shutdown) {
                // UNDECIDED MXM:
                // The problem is that the worker threads are a limited resource ...
                // therefore this must wait first for a free thread-worker resource,
                // and if available, we take one task element from the Queue

                // UNDECIDED MXM:
                // I really don't like it but actually it removes quite a lot of 
                // headaches we just look every 200 ms or so, new thread workers 
                // became available again. actually we must resume this thread for 
                // the right reason....

                // because the tasks may not be time critical this is a good enough solution for some time
                // maybe we need something more responsive later.
                while (!taskDispatcher.isTaskWorkerAvailable()) {
                    try {
                        Thread.sleep( 200 );
                    }
                    catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

                // this is a blocking call
                FSqrTask taskToRun = taskQueue.poll();

                if (taskToRun == null) {
                    // because when it was blocked, and no task was available, taskToRuin is a null value and 
                    // then we just restart this loop and collect the task, on the second attempt, which is 
                    // then first in the queue.
                    continue;
                }

                // TODO: maybe provide the thread pool here instead of using the setter (setThreadPool)...
                this.taskDispatcher.runTask( taskToRun );
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.threadPool.gracefulShutdownThreadPool();
            // stop all threads. / maybe wait for completion?
            // this.threadPool.finish/terminateAll threads.
        }
    }

    public void shutdown() {
        FSqrTask nopTask = new FSqrNopTask();
        this.shutdown = true;
        this.taskDispatcher.dispatchTask( nopTask );
    }
}
