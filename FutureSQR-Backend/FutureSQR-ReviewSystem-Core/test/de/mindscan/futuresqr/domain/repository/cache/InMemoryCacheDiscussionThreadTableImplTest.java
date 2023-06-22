package de.mindscan.futuresqr.domain.repository.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThread;

public class InMemoryCacheDiscussionThreadTableImplTest {

    private static final String DISCUSSION_11111_111111_11111 = "11111-111111-11111";
    private static final String DISCUSSION_22222_222222_22222 = "22222-222222-22222";

    @Test
    public void testInMemoryCacheDiscussionThreadTableImpl() throws Exception {
        // arrange
        // act
        // assert
        InMemoryCacheDiscussionThreadTableImpl threadTable = new InMemoryCacheDiscussionThreadTableImpl();
    }

    @Test
    public void testIsCached_CtorOnlyRequestUnknownDiscussionThread_returnsFalseForUnknownThread() throws Exception {
        // arrange
        InMemoryCacheDiscussionThreadTableImpl threadTable = new InMemoryCacheDiscussionThreadTableImpl();

        // act
        boolean result = threadTable.isCached( DISCUSSION_11111_111111_11111 );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsCached_SetupDiscussionThreadAndRequested_returnsTrueForKnownThread() throws Exception {
        // arrange
        InMemoryCacheDiscussionThreadTableImpl threadTable = new InMemoryCacheDiscussionThreadTableImpl();
        FSqrDiscussionThread newThread = Mockito.mock( FSqrDiscussionThread.class, "newThread" );

        threadTable.putDiscussionThread( DISCUSSION_11111_111111_11111, newThread );

        // act
        boolean result = threadTable.isCached( DISCUSSION_11111_111111_11111 );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testGetDiscussionThread_SetupDiscussionThreadAndRequest_returnSameInstance() throws Exception {
        // arrange
        InMemoryCacheDiscussionThreadTableImpl threadTable = new InMemoryCacheDiscussionThreadTableImpl();
        FSqrDiscussionThread expectedThread = Mockito.mock( FSqrDiscussionThread.class, "newThread" );

        threadTable.putDiscussionThread( DISCUSSION_11111_111111_11111, expectedThread );

        // act
        FSqrDiscussionThread result = threadTable.getDiscussionThread( DISCUSSION_11111_111111_11111 );

        // assert
        assertThat( result, is( sameInstance( expectedThread ) ) );
    }

    @Test
    public void testLookupThreads_SetupTwoThreadsRequestLookupWithOneThread_expectSameThread() throws Exception {
        // arrange
        InMemoryCacheDiscussionThreadTableImpl threadTable = new InMemoryCacheDiscussionThreadTableImpl();
        FSqrDiscussionThread expectedThread = Mockito.mock( FSqrDiscussionThread.class, "expectedThread" );
        FSqrDiscussionThread nonExpectedThread = Mockito.mock( FSqrDiscussionThread.class, "nonExpectedThread" );

        threadTable.putDiscussionThread( DISCUSSION_11111_111111_11111, expectedThread );
        threadTable.putDiscussionThread( DISCUSSION_22222_222222_22222, nonExpectedThread );

        List<String> lookupList = Arrays.asList( DISCUSSION_11111_111111_11111 );

        // act
        Collection<FSqrDiscussionThread> results = threadTable.lookupThreads( lookupList );

        // assert
        assertThat( results, contains( expectedThread ) );
    }

    @Test
    public void testLookupThreads_SetupTwoThreadsRequestLookupWithTwoThreads_expectBothThreads() throws Exception {
        // arrange
        InMemoryCacheDiscussionThreadTableImpl threadTable = new InMemoryCacheDiscussionThreadTableImpl();
        FSqrDiscussionThread expectedThread1 = Mockito.mock( FSqrDiscussionThread.class, "expectedThread1" );
        FSqrDiscussionThread expectedThread2 = Mockito.mock( FSqrDiscussionThread.class, "expectedThread2" );

        threadTable.putDiscussionThread( DISCUSSION_11111_111111_11111, expectedThread1 );
        threadTable.putDiscussionThread( DISCUSSION_22222_222222_22222, expectedThread2 );

        List<String> lookupList = Arrays.asList( DISCUSSION_11111_111111_11111, DISCUSSION_22222_222222_22222 );

        // act
        Collection<FSqrDiscussionThread> results = threadTable.lookupThreads( lookupList );

        // assert
        assertThat( results, contains( expectedThread1, expectedThread2 ) );
    }

}
