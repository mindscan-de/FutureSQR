package de.mindscan.futuresqr.domain.repository.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import java.util.Collection;

import org.junit.jupiter.api.Test;

public class InMemoryCacheDiscussionThreadIdsTableImplTest {

    private static final String CODEREVIEWID_AP_CR_1 = "AP-CR-1";
    private static final String A_PROJECT = "AProject";
    private static final String THREADID_11111_11111_111111 = "11111-11111-111111";
    private static final String THREADID_22222_22222_222222 = "22222-22222-222222";

    @Test
    public void testInMemoryCacheDiscussionThreadIdsTableImpl() throws Exception {
        // arrange
        // act
        // assert
        InMemoryCacheDiscussionThreadIdsTableImpl threadIdsTable = new InMemoryCacheDiscussionThreadIdsTableImpl();
    }

    @Test
    public void testIsCached_CtorOnlyRequestIncachedElement_returnsFalse() throws Exception {
        // arrange
        InMemoryCacheDiscussionThreadIdsTableImpl threadIdsTable = new InMemoryCacheDiscussionThreadIdsTableImpl();

        // act
        boolean result = threadIdsTable.isCached( A_PROJECT, CODEREVIEWID_AP_CR_1 );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsCached_CtorOnlyRequestIncachedElement_returnsTrue() throws Exception {
        // arrange
        InMemoryCacheDiscussionThreadIdsTableImpl threadIdsTable = new InMemoryCacheDiscussionThreadIdsTableImpl();
        threadIdsTable.addDiscussionThread( A_PROJECT, CODEREVIEWID_AP_CR_1, THREADID_11111_11111_111111 );

        // act
        boolean result = threadIdsTable.isCached( A_PROJECT, CODEREVIEWID_AP_CR_1 );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testHasDiscussionThreadUUID_CtorOnlyRequestUncachedElement_returnsFalse() throws Exception {
        // arrange
        InMemoryCacheDiscussionThreadIdsTableImpl threadIdsTable = new InMemoryCacheDiscussionThreadIdsTableImpl();

        // act
        boolean result = threadIdsTable.hasDiscussionThreadUUID( A_PROJECT, CODEREVIEWID_AP_CR_1, THREADID_11111_11111_111111 );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testHasDiscussionThreadUUID_CtorOnlyRequestAddDifferentThreadRequestUncachedElement_returnsFalse() throws Exception {
        // arrange
        InMemoryCacheDiscussionThreadIdsTableImpl threadIdsTable = new InMemoryCacheDiscussionThreadIdsTableImpl();
        threadIdsTable.addDiscussionThread( A_PROJECT, CODEREVIEWID_AP_CR_1, THREADID_11111_11111_111111 );

        // act
        boolean result = threadIdsTable.hasDiscussionThreadUUID( A_PROJECT, CODEREVIEWID_AP_CR_1, THREADID_22222_22222_222222 );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testHasDiscussionThreadUUID_AddThreadRequestSameThreadId_returnsTrue() throws Exception {
        // arrange
        InMemoryCacheDiscussionThreadIdsTableImpl threadIdsTable = new InMemoryCacheDiscussionThreadIdsTableImpl();
        threadIdsTable.addDiscussionThread( A_PROJECT, CODEREVIEWID_AP_CR_1, THREADID_11111_11111_111111 );

        // act
        boolean result = threadIdsTable.hasDiscussionThreadUUID( A_PROJECT, CODEREVIEWID_AP_CR_1, THREADID_11111_11111_111111 );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testGetDiscussionThreadUUIDs_AddThreadRequestSameThreadId_returnedListContainsSameThreadId() throws Exception {
        // arrange
        InMemoryCacheDiscussionThreadIdsTableImpl threadIdsTable = new InMemoryCacheDiscussionThreadIdsTableImpl();
        threadIdsTable.addDiscussionThread( A_PROJECT, CODEREVIEWID_AP_CR_1, THREADID_11111_11111_111111 );

        // act
        Collection<String> result = threadIdsTable.getDiscussionThreadUUIDs( A_PROJECT, CODEREVIEWID_AP_CR_1 );

        // assert
        assertThat( result, contains( THREADID_11111_11111_111111 ) );
    }

    @Test
    public void testGetDiscussionThreadUUIDs_CtorOnlyRequestUnknowbThreadId_returnsEmptyList() throws Exception {
        // arrange
        InMemoryCacheDiscussionThreadIdsTableImpl threadIdsTable = new InMemoryCacheDiscussionThreadIdsTableImpl();

        // act
        Collection<String> result = threadIdsTable.getDiscussionThreadUUIDs( A_PROJECT, CODEREVIEWID_AP_CR_1 );

        // assert
        assertThat( result, empty() );
    }

}
