package de.mindscan.futuresqr.domain.repository.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.futuresqr.domain.model.FSqrCodeReview;

public class InMemoryCacheCodeReviewTableImplTest {

    private static final String A_PROJECT_KEY = "AProject";
    private static final String A_PROJECT_REVIEW_1 = "AP-1";
    private static final String A_PROJECT_REVIEW_2 = "AP-2";

    @Test
    public void testInMemoryCacheCodeReviewTableImpl_createAnInstance_expectNoException() throws Exception {
        // arrange
        // act
        // assert
        InMemoryCacheCodeReviewTableImpl reviewTable = new InMemoryCacheCodeReviewTableImpl();
    }

    @Test
    public void testIsCached_CtorOnlyTestUnknownProjectReviewId_returnsFalse() throws Exception {
        // arrange
        InMemoryCacheCodeReviewTableImpl reviewTable = new InMemoryCacheCodeReviewTableImpl();

        // act
        boolean result = reviewTable.isCached( A_PROJECT_KEY, A_PROJECT_REVIEW_1 );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsCached_SetupKnownProjectAndReviewId_returnsTrue() throws Exception {
        // arrange
        FSqrCodeReview codeReview = Mockito.mock( FSqrCodeReview.class, "codeReview" );
        InMemoryCacheCodeReviewTableImpl reviewTable = new InMemoryCacheCodeReviewTableImpl();
        reviewTable.putCodeReview( A_PROJECT_KEY, A_PROJECT_REVIEW_1, codeReview );

        // act
        boolean result = reviewTable.isCached( A_PROJECT_KEY, A_PROJECT_REVIEW_1 );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testGetCodeReview_CtorOnlyUnknownProjectReviewId_returnsNull() throws Exception {
        // arrange
        InMemoryCacheCodeReviewTableImpl reviewTable = new InMemoryCacheCodeReviewTableImpl();

        // act
        FSqrCodeReview result = reviewTable.getCodeReview( A_PROJECT_KEY, A_PROJECT_REVIEW_1 );

        // assert
        assertThat( result, nullValue() );
    }

    @Test
    public void testGetCodeReview_setupKnownProjectAndReviewUd_returnsNonNullValue() throws Exception {
        // arrange
        FSqrCodeReview codeReview = Mockito.mock( FSqrCodeReview.class, "codeReview" );
        InMemoryCacheCodeReviewTableImpl reviewTable = new InMemoryCacheCodeReviewTableImpl();
        reviewTable.putCodeReview( A_PROJECT_KEY, A_PROJECT_REVIEW_1, codeReview );

        // act
        FSqrCodeReview result = reviewTable.getCodeReview( A_PROJECT_KEY, A_PROJECT_REVIEW_1 );

        // assert
        assertThat( result, not( nullValue() ) );
    }

    @Test
    public void testPutCodeReview_putCodeReview_getReturnsSameValue() throws Exception {
        // arrange
        InMemoryCacheCodeReviewTableImpl reviewTable = new InMemoryCacheCodeReviewTableImpl();
        FSqrCodeReview codeReview = Mockito.mock( FSqrCodeReview.class, "codeReview" );

        // act
        reviewTable.putCodeReview( A_PROJECT_KEY, A_PROJECT_REVIEW_1, codeReview );

        // assert
        FSqrCodeReview result = reviewTable.getCodeReview( A_PROJECT_KEY, A_PROJECT_REVIEW_1 );
        assertThat( result, is( sameInstance( codeReview ) ) );
    }

    @Test
    public void testGetCodeReviewOrComputeIfAbsent_CtorOnlyProvideACodeReview_expectSameProvidedValue() throws Exception {
        // arrange
        InMemoryCacheCodeReviewTableImpl reviewTable = new InMemoryCacheCodeReviewTableImpl();
        FSqrCodeReview codeReview = Mockito.mock( FSqrCodeReview.class, "codeReview" );

        // act
        FSqrCodeReview result = reviewTable.getCodeReviewOrComputeIfAbsent( A_PROJECT_KEY, A_PROJECT_REVIEW_1, ( k1, k2 ) -> codeReview );

        // assert
        assertThat( result, is( sameInstance( codeReview ) ) );
    }

    @Test
    public void testGetCodeReviewOrComputeIfAbsent_SetupAKnownCodeReview_expectSameAsInSetup() throws Exception {
        // arrange
        FSqrCodeReview expectedCodeReview = Mockito.mock( FSqrCodeReview.class, "codeReview" );
        FSqrCodeReview differentCodeReview = Mockito.mock( FSqrCodeReview.class, "differentCodeReview" );

        InMemoryCacheCodeReviewTableImpl reviewTable = new InMemoryCacheCodeReviewTableImpl();
        reviewTable.putCodeReview( A_PROJECT_KEY, A_PROJECT_REVIEW_1, expectedCodeReview );

        // act
        FSqrCodeReview result = reviewTable.getCodeReviewOrComputeIfAbsent( A_PROJECT_KEY, A_PROJECT_REVIEW_1, ( k1, k2 ) -> differentCodeReview );

        // assert
        assertThat( result, is( sameInstance( expectedCodeReview ) ) );
    }

}
