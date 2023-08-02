package de.mindscan.futuresqr.core.thread;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

}
