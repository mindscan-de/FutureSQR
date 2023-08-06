package de.mindscan.futuresqr.core.dispatcher.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.futuresqr.core.queue.ThreadBoundArrayDeque;
import de.mindscan.futuresqr.core.task.FSqrTask;
import de.mindscan.futuresqr.core.thread.FSqrThreadPool;

public class SimpleTaskDispatcherImplTest {

    @Test
    public void testSimpleTaskDispatcherImpl_CtorOnly_expectNoException() throws Exception {
        // arrange
        SimpleTaskDispatcherImpl taskDispatcher = new SimpleTaskDispatcherImpl( null );

        // act
        // assert
    }

    @Test
    public void testIsTaskWorkerAvailable_CtorOnlyAndInvokeQueueTestWithFalse_returnsFalse() throws Exception {
        // arrange
        FSqrThreadPool threadPool = Mockito.mock( FSqrThreadPool.class, "threadPool" );
        Mockito.doReturn( false ).when( threadPool ).isWorkerThreadAvailable();

        SimpleTaskDispatcherImpl taskDispatcher = new SimpleTaskDispatcherImpl( threadPool );

        // act
        boolean result = taskDispatcher.isTaskWorkerAvailable();

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsTaskWorkerAvailable_CtorOnlyAndInvokeQueueTestWithTrue_returnsTrue() throws Exception {
        // arrange
        FSqrThreadPool threadPool = Mockito.mock( FSqrThreadPool.class, "threadPool" );
        Mockito.doReturn( true ).when( threadPool ).isWorkerThreadAvailable();

        SimpleTaskDispatcherImpl taskDispatcher = new SimpleTaskDispatcherImpl( threadPool );

        // act
        boolean result = taskDispatcher.isTaskWorkerAvailable();

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testDispatchTask_NoTaskQueueSetNonNullTask_throwsIllegalStateException() throws Exception {
        // arrange
        FSqrThreadPool threadPool = Mockito.mock( FSqrThreadPool.class, "threadPool" );
        SimpleTaskDispatcherImpl taskDispatcher = new SimpleTaskDispatcherImpl( threadPool );
        FSqrTask task = Mockito.mock( FSqrTask.class, "task" );

        // act
        // assert
        Assertions.assertThrows( IllegalStateException.class, () -> taskDispatcher.dispatchTask( task ) );
    }

    @Test
    public void testDispatchTask_SetTaskQueueAddNonNullTask_addsTheExpectedTask() throws Exception {
        // arrange
        FSqrThreadPool threadPool = Mockito.mock( FSqrThreadPool.class, "threadPool" );
        ThreadBoundArrayDeque<FSqrTask> taskQueue = Mockito.mock( ThreadBoundArrayDeque.class, "taskQueue" );
        SimpleTaskDispatcherImpl taskDispatcher = new SimpleTaskDispatcherImpl( threadPool );
        taskDispatcher.setTaskQueue( taskQueue );
        FSqrTask expectedTask = Mockito.mock( FSqrTask.class, "expectedTask" );

        // act
        taskDispatcher.dispatchTask( expectedTask );

        // assert
        Mockito.verify( taskQueue, times( 1 ) ).add( expectedTask );
    }

    @Test
    public void testRunTask__invokesBorrowThreadOnPool() throws Exception {
        // cpxuaaa
    }

    @Test
    public void testRunTask__assignsTaskToWorkerThreadFromPool() throws Exception {
        // cpxuaaa
    }

    @Test
    public void testRunTask__startAssignedTaskOnWorkerThreadFromPool() throws Exception {
        // cpxuaaa
    }

}
