package de.mindscan.futuresqr.core.thread;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FSqrWorkerThreadPoolTest {

    @Test
    public void testFSqrWorkerThreadPool_CtorOnly_noException() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );

        // act
        // assert
    }

    @Test
    public void testGetThreadPoolSize_CtorThreadPoolSizeIsOne_expectOne() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );

        // act
        int result = threadPool.getThreadPoolSize();

        // assert
        assertThat( result, equalTo( 1 ) );
    }

    @Test
    public void testGetThreadPoolSize_CtorThreadPoolSizeIsTwo_expectTwo() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 2, "Test" );

        // act
        int result = threadPool.getThreadPoolSize();

        // assert
        assertThat( result, equalTo( 2 ) );
    }

    @Test
    public void testInitializeThreadPool_CtorPoolSizeOneNoInitialization_PooledSizeIsZero() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );

        // act

        // assert
        int result = threadPool.getNumberOfPooledThreads();
        assertThat( result, equalTo( 0 ) );
    }

    @Test
    public void testInitializeThreadPool_CtorPoolSizeOneInitializeThreadPool_PooledSizeIsOne() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );

        // act
        threadPool.initializeThreadPool();

        // assert
        int result = threadPool.getNumberOfPooledThreads();
        assertThat( result, equalTo( 1 ) );
    }

    @Test
    public void testBorrowThread_Uninitialized_throwsIllegalStateException() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );

        // act
        // assert
        Assertions.assertThrows( IllegalStateException.class, () -> threadPool.borrowThread() );
    }

    @Test
    public void testBorrowThread_Initialized_threadIsNotNull() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );
        threadPool.initializeThreadPool();

        // act
        FSqrWorkerThread result = threadPool.borrowThread();

        // assert
        assertThat( result, not( nullValue() ) );
    }

    @Test
    public void testBorrowThread_initialized_threadIsInBorrowedCondition() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );
        threadPool.initializeThreadPool();

        // act
        FSqrWorkerThread borrowedThread = threadPool.borrowThread();

        // assert
        FSqrWorkerThreadLifecylce result = borrowedThread.getWorkerThreadState();
        assertThat( result, equalTo( FSqrWorkerThreadLifecylce.BORROWED ) );
    }

    @Test
    public void testGetNumberOfBorrowedThreads_borrowOneThread_returnOne() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );
        threadPool.initializeThreadPool();
        FSqrWorkerThread borrowedThread = threadPool.borrowThread();

        // act
        int result = threadPool.getNumberOfBorrowedThreads();

        // assert
        assertThat( result, equalTo( 1 ) );
    }

    @Test
    public void testGetNumberOfPooledThreads_borrowOneThread_returnsZeroPooledThreads() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );
        threadPool.initializeThreadPool();
        FSqrWorkerThread borrowedThread = threadPool.borrowThread();

        // act
        int result = threadPool.getNumberOfPooledThreads();

        // assert
        assertThat( result, equalTo( 0 ) );

    }

    @Test
    public void testGetNumberOfBorrowedThreads_borrowTwoThread_returnOne() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 3, "Test" );
        threadPool.initializeThreadPool();

        FSqrWorkerThread borrowedThread1 = threadPool.borrowThread();
        FSqrWorkerThread borrowedThread2 = threadPool.borrowThread();

        // act
        int result = threadPool.getNumberOfBorrowedThreads();

        // assert
        assertThat( result, equalTo( 2 ) );
    }

    @Test
    public void testIsWorkerThreadAvailable_NotInitialized_returnFalse() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 3, "Test" );

        // act
        boolean result = threadPool.isWorkerThreadAvailable();

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsWorkerThreadAvailable_Initialized_returnTrue() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 3, "Test" );
        threadPool.initializeThreadPool();

        // act
        boolean result = threadPool.isWorkerThreadAvailable();

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testIsWorkerThreadAvailable_InitialitedAndInShutDown_returnFalse() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );
        threadPool.initializeThreadPool();
        threadPool.gracefulShutdownThreadPool();

        // act
        boolean result = threadPool.isWorkerThreadAvailable();

        // assert
        assertThat( result, equalTo( false ) );

    }

    @Test
    public void testWorkerComplete_Null_throwsIllegalArgumentException() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );
        threadPool.initializeThreadPool();

        // act
        // assert
        Assertions.assertThrows( IllegalArgumentException.class, () -> threadPool.workerComplete( null ) );
    }

    @Test
    public void testWorkerComplete_InitializeAndBorrowOneThreadAndCompleteThread_BorrorwsThreadListIsEmpty() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );
        threadPool.initializeThreadPool();
        FSqrWorkerThread borrowedThread1 = threadPool.borrowThread();

        // act
        threadPool.workerComplete( borrowedThread1 );

        // assert
        int result = threadPool.getNumberOfBorrowedThreads();
        assertThat( result, equalTo( 0 ) );
    }

    @Test
    public void testGetNumberOfFinishedThreads_InitializeBorrowAndCompleteThread_FinishedListSizeIsOne() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );
        threadPool.initializeThreadPool();
        FSqrWorkerThread borrowedThread1 = threadPool.borrowThread();
        threadPool.workerComplete( borrowedThread1 );

        // act
        int result = threadPool.getNumberOfFinishedThreads();

        // assert
        assertThat( result, equalTo( 1 ) );
    }

    @Test
    public void testGetNumberOfFinishedThreadsInitializeBorrowButDontFinishThread_FinishedListSizeIsZero() throws Exception {
        // arrange
        FSqrWorkerThreadPool threadPool = new FSqrWorkerThreadPool( 1, "Test" );
        threadPool.initializeThreadPool();
        FSqrWorkerThread borrowedThread1 = threadPool.borrowThread();

        // act
        int result = threadPool.getNumberOfFinishedThreads();

        // assert
        assertThat( result, equalTo( 0 ) );
    }

}
