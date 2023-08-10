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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.futuresqr.core.task.FSqrTask;

/**
 * 
 */
class FSqrWorkerThreadTest {

    @Test
    public void testFSqrWorkerThread_CtorOnly_noExceptions() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = Mockito.mock( FSqrWorkerThreadPool.class, "threadPool" );
        String theadName = "TestThread";

        // act
        // assert
        FSqrWorkerThread thread = new FSqrWorkerThread( threadPool, theadName );
    }

    @Test
    public void testQuitWorker_CtorOnlyRunAndShutdown_() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = Mockito.mock( FSqrWorkerThreadPool.class, "threadPool" );
        String theadName = "TestThread";
        FSqrWorkerThread thread = new FSqrWorkerThread( threadPool, theadName );

        FSqrTask taskToRun = Mockito.mock( FSqrTask.class, "taskToRun" );
        thread.start();

        // act
        // assert
        Thread.sleep( 1000 );
        thread.assignTask( taskToRun );
        Thread.sleep( 1000 );
        thread.startAssignedTask();
        Thread.sleep( 1000 );

        thread.quitWorker();
    }

}
