package de.mindscan.futuresqr.domain.repository.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.jupiter.api.Test;

public class InMemoryCacheRevisionToCodeReviewIdTableImplTest {

    private static final String A_PROJECT = "AProject";
    private static final String REVISION_1111 = "1111";
    private static final String A_PROJECT_REVIEW_1 = "AP-1";

    @Test
    public void testInMemoryCacheRevisionToCodeReviewIdTableImpl() throws Exception {
        // arrange
        // act
        // assert

        InMemoryCacheRevisionToCodeReviewIdTableImpl revisionTable = new InMemoryCacheRevisionToCodeReviewIdTableImpl();
    }

    @Test
    public void testIsCached_CtorOblyTestUnknownProjectRevisionId_returnsFalse() throws Exception {
        // arrange
        InMemoryCacheRevisionToCodeReviewIdTableImpl revisionTable = new InMemoryCacheRevisionToCodeReviewIdTableImpl();

        // act
        boolean result = revisionTable.isCached( A_PROJECT, REVISION_1111 );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsCached_SetupKnownProjectRevisionId_returnsTrue() throws Exception {
        // arrange
        InMemoryCacheRevisionToCodeReviewIdTableImpl revisionTable = new InMemoryCacheRevisionToCodeReviewIdTableImpl();
        revisionTable.putCodeReviewId( A_PROJECT, REVISION_1111, A_PROJECT_REVIEW_1 );

        // act
        boolean result = revisionTable.isCached( A_PROJECT, REVISION_1111 );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testGetCodeReviewId_CtorOblyTestUnknownProjectRevisionId_returnsEmptyString() throws Exception {
        // arrange
        InMemoryCacheRevisionToCodeReviewIdTableImpl revisionTable = new InMemoryCacheRevisionToCodeReviewIdTableImpl();

        // act
        String result = revisionTable.getCodeReviewId( A_PROJECT, REVISION_1111 );

        // assert
        assertThat( result, equalTo( "" ) );
    }

    @Test
    public void testGetCodeReviewId_SetupKnownProjectRevisionId_returnsNonEmptyString() throws Exception {
        // arrange
        InMemoryCacheRevisionToCodeReviewIdTableImpl revisionTable = new InMemoryCacheRevisionToCodeReviewIdTableImpl();
        revisionTable.putCodeReviewId( A_PROJECT, REVISION_1111, A_PROJECT_REVIEW_1 );

        // act
        String result = revisionTable.getCodeReviewId( A_PROJECT, REVISION_1111 );

        // assert
        assertThat( result, not( equalTo( "" ) ) );
    }

    @Test
    public void testPutCodeReviewId_putCodeReviewId_getIdRetursSameValue() throws Exception {
        // arrange
        InMemoryCacheRevisionToCodeReviewIdTableImpl revisionTable = new InMemoryCacheRevisionToCodeReviewIdTableImpl();
        revisionTable.putCodeReviewId( A_PROJECT, REVISION_1111, A_PROJECT_REVIEW_1 );

        // act
        String result = revisionTable.getCodeReviewId( A_PROJECT, REVISION_1111 );

        // assert
        assertThat( result, is( sameInstance( A_PROJECT_REVIEW_1 ) ) );
    }

}
