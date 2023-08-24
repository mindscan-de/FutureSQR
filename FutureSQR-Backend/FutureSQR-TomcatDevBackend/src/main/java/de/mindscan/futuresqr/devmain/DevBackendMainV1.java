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
import de.mindscan.futuresqr.crawlers.CrawlerTaskFactory;
import de.mindscan.futuresqr.domain.application.FSqrApplication;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.tasks.FSqrBackgroundTaskBase;

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
        CrawlerTaskFactory crawlerTaskFactory = new CrawlerTaskFactory();

        // start :: taskdispatcher
        FSqrThreadPool threadPool = new FSqrWorkerThreadPool( 2, "TaskPool" );
        threadPool.initializeThreadPool();
        TaskDispatcher taskDispatcher = new SimpleTaskDispatcherImpl( threadPool );
        TaskDispatcherThread taskDispatcherThread = new TaskDispatcherThread( taskDispatcher, threadPool );
        taskDispatcherThread.start();

        // start :: eventdispatcher
        EventDispatcher eventDispatcher = new SimpleEventDispatcherImpl();
        EventDispatcherThread eventDispatcherThread = new EventDispatcherThread( eventDispatcher );
        eventDispatcherThread.start();

        // set the taskContext
        crawlerTaskFactory.getTaskContext().setTaskDispatcher( taskDispatcher );
        crawlerTaskFactory.getTaskContext().setEventDispatcher( eventDispatcher );

        // TODO: start application code?
        // problem is now that the webserver and this instance work on the same sqlite database file.
        // so we will need a better database later.
        FSqrApplicationServices services = FSqrApplication.getInstance().getServices();
        // Do we need some additional initialization? 
        // A factory for the Tasks, which initializes the tasks correctly?

        // TODO: next we must make the FSqrTask stuff compatible to the ReviewSystem-Core Background tasks.
        //       such that these reviewsystem core tasks can be run by the event and taskdispatcher system.

        //
        System.out.println( "Test the application stuff here..." );

        // provide some mechanism to let this application run, and be able to quit it, e.g. open / provide some console...

        FSqrBackgroundTaskBase detectTask = crawlerTaskFactory.geDetectNewScmProjectBranchesTask( "futuresqr" );

        taskDispatcher.dispatchTask( detectTask );

        try {
            Thread.sleep( 10000 );
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //
        // TODO: actually we want to dispatch some of the interesting tasks.
        // UpdateProjectCacheTask task = new UpdateProjectCacheTask( "futuresqr" );
        // taskDispatcher.dispatchTask( task );

        // 

        // shutdown 
        taskDispatcherThread.shutdown();
        eventDispatcherThread.shutdown();

        // now killall workers in threadpool - not yet nice, but let's see.
        threadPool.killAll();

        System.out.println( "shutdown invoked." );
    }
}
