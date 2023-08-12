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
package de.mindscan.futuresqr.devmain;

import de.mindscan.futuresqr.core.dispatcher.EventDispatcher;
import de.mindscan.futuresqr.core.dispatcher.EventDispatcherThread;
import de.mindscan.futuresqr.core.dispatcher.TaskDispatcher;
import de.mindscan.futuresqr.core.dispatcher.TaskDispatcherThread;
import de.mindscan.futuresqr.core.dispatcher.impl.SimpleEventDispatcherImpl;
import de.mindscan.futuresqr.core.dispatcher.impl.SimpleTaskDispatcherImpl;
import de.mindscan.futuresqr.core.thread.FSqrThreadPool;
import de.mindscan.futuresqr.core.thread.FSqrWorkerThreadPool;

/**
 * 
 */
public class DevBackendMainV1 {

    /**
     * @param args
     */
    public static void main( String[] args ) {
        DevBackendMainV1 main = new DevBackendMainV1();
        main.run();
    }

    public void run() {
        // start :: taskdispatcher
        FSqrThreadPool threadPool = new FSqrWorkerThreadPool( 2, "TaskPool" );
        TaskDispatcher taskDispatcher = new SimpleTaskDispatcherImpl( threadPool );
        TaskDispatcherThread taskDispatcherThread = new TaskDispatcherThread( taskDispatcher, threadPool );
        // taskDispatcherThread.start();

        // start :: eventdispatcher
        EventDispatcher eventDispatcher = new SimpleEventDispatcherImpl();
        EventDispatcherThread eventDispatcherThread = new EventDispatcherThread( eventDispatcher );
        eventDispatcherThread.start();

        // TODO: actually we want to dispatch some of the interesting tasks.
        // UpdateProjectCacheTask task = new UpdateProjectCacheTask( "futuresqr" );
        // taskDispatcher.dispatchTask( task );

        //
        System.out.println( "Test" );

        // 
        eventDispatcherThread.shutdown();
        taskDispatcherThread.shutdown();

    }
}
