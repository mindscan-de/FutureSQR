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
import de.mindscan.futuresqr.core.task.FSqrTask;
import de.mindscan.futuresqr.core.thread.FSqrThread;
import de.mindscan.futuresqr.core.thread.FSqrThreadPool;

/**
 * 
 */
public class TaskDispatcherThread extends FSqrThread {

    private ThreadBoundArrayDeque<FSqrTask> taskQueue;
    private TaskDispatcher taskDispatcher;
    private FSqrThreadPool threadPool;

    // TODO: threadPool?

    /**
     * 
     */
    public TaskDispatcherThread( TaskDispatcher taskDispatcher, FSqrThreadPool threadPool ) {
        super( "FSqr-TaskDispatcher-Thread" );

        this.threadPool = threadPool;
        this.taskQueue = new ThreadBoundArrayDeque<FSqrTask>( this );

        this.taskDispatcher.setThreadPool( this.threadPool );
        this.taskDispatcher.setTaskQueue( this.taskQueue );
    }

    @Override
    public void run() {
        try {

            // TODO: Not sure whether i like this design/implementation idea, but for now it is just 
            //       conserving the idea. 
            while (true) {
                // The problem is that the worker threads are a limited resource....
                // therefore we should wait at first for a free thread-worker resource,
                // and if available, we take one element from the Queue

                // we will lock this thread in wait mode, or do we the same thing as the idea in the task Queue....
                // actually we must resume this thread for the right reason....

                // 
                // while(! taskDispatcher.isRunnerAvailable())) 
                // {
                // }

                FSqrTask taskToRun = taskQueue.poll();

                // because it was blocked, and no task was available, we just restart this loop and collect the task, 
                // which is the first in the queue.

                if (taskToRun == null) {
                    continue;
                }

                // get the first free Runner, or should that be part of the taskDispatcher..
                this.taskDispatcher.runTask( taskToRun );
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // TODO shutdown the taskdispatcher;
        }
    }
}
