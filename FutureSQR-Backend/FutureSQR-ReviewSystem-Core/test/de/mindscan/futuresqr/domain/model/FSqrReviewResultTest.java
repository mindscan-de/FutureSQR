package de.mindscan.futuresqr.domain.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.domain.model.FSqrReviewResult;
import de.mindscan.futuresqr.domain.model.FSqrReviewResultState;
import de.mindscan.futuresqr.domain.model.FSqrReviewResultValue;

public class FSqrReviewResultTest {

    public final static String REVIEWER_1_UUID = "11111-1111-";
    public final static String REVIEWER_2_UUID = "22222-1111-";

    @Test
    public void testFSqrReviewResultString_Ctor_expectReviewResultHasIncompleteState() throws Exception {
        // arrange
        // act
        FSqrReviewResult reviewResult = new FSqrReviewResult( REVIEWER_1_UUID );

        // assert
        FSqrReviewResultState result = reviewResult.getResult();
        assertThat( result, equalTo( FSqrReviewResultState.Incomplete ) );
    }

    @Test
    public void testFSqrReviewResultString_CTorWithReviewer1_expectSameReviewerId() throws Exception {
        // arrange
        // act
        FSqrReviewResult reviewResult = new FSqrReviewResult( REVIEWER_1_UUID );

        // assert
        String result = reviewResult.getReviewerId();
        assertThat( result, equalTo( REVIEWER_1_UUID ) );
    }

    @Test
    public void testFSqrReviewResultString_CTorWithReviewer2_expectSameReviewerId() throws Exception {
        // arrange
        // act
        FSqrReviewResult reviewResult = new FSqrReviewResult( REVIEWER_2_UUID );

        // assert
        String result = reviewResult.getReviewerId();
        assertThat( result, equalTo( REVIEWER_2_UUID ) );
    }

    @Test
    public void testApproveReview_CreateReviewResultApprove_expectReviewResultHasApprovedState() throws Exception {
        // arrange
        FSqrReviewResult reviewResult = new FSqrReviewResult( REVIEWER_1_UUID );

        // act
        reviewResult.approveReview( 0L );

        // assert
        FSqrReviewResultState result = reviewResult.getResult();
        assertThat( result, equalTo( FSqrReviewResultState.Approved ) );
    }

    @Test
    public void testConcernOnReview_CreateReviewResultHaveConcerns_expectReviewResultHasConcernedState() throws Exception {
        // arrange
        FSqrReviewResult reviewResult = new FSqrReviewResult( REVIEWER_1_UUID );

        // act
        reviewResult.concernOnReview( 0L );

        // assert
        FSqrReviewResultState result = reviewResult.getResult();
        assertThat( result, equalTo( FSqrReviewResultState.Concerns ) );
    }

    @Test
    public void testRollbackReview_ApprovedReviewThenRollback_expectReviewResultIsIncomplete() throws Exception {
        // arrange
        FSqrReviewResult reviewResult = new FSqrReviewResult( REVIEWER_1_UUID );
        reviewResult.approveReview( 0L );

        // act
        reviewResult.rollbackReview( 1L );

        // assert
        FSqrReviewResultState result = reviewResult.getResult();
        assertThat( result, equalTo( FSqrReviewResultState.Incomplete ) );
    }

    @Test
    public void testRollbackReview_ConcernsReviewThenRollback_expectReviewResultIsIncomplete() throws Exception {
        // arrange
        FSqrReviewResult reviewResult = new FSqrReviewResult( REVIEWER_1_UUID );
        reviewResult.concernOnReview( 0L );

        // act
        reviewResult.rollbackReview( 1L );

        // assert
        FSqrReviewResultState result = reviewResult.getResult();
        assertThat( result, equalTo( FSqrReviewResultState.Incomplete ) );
    }

    @Test
    public void testFSqrReviewResultFSqrReviewResultValue_InitializeWithReviewer1_expectReviewer1() throws Exception {
        // arrange
        FSqrReviewResult reviewResult = new FSqrReviewResult( REVIEWER_1_UUID );
        FSqrReviewResultValue persistedReviewResultValue = (FSqrReviewResultValue) reviewResult;

        // act
        FSqrReviewResult preinitialized = new FSqrReviewResult( persistedReviewResultValue );

        // assert
        String result = preinitialized.getReviewerId();
        assertThat( result, equalTo( REVIEWER_1_UUID ) );
    }

    @Test
    public void testFSqrReviewResultFSqrReviewResultValue_InitializeWithReviewer2_expectReviewer2() throws Exception {
        // arrange
        FSqrReviewResult reviewResult = new FSqrReviewResult( REVIEWER_2_UUID );
        FSqrReviewResultValue persistedReviewResultValue = (FSqrReviewResultValue) reviewResult;

        // act
        FSqrReviewResult preinitialized = new FSqrReviewResult( persistedReviewResultValue );

        // assert
        String result = preinitialized.getReviewerId();
        assertThat( result, equalTo( REVIEWER_2_UUID ) );
    }

    @Test
    public void testFSqrReviewResultFSqrReviewResultValue_InitializeWithApprovedReviewState_expectReviewResultApproved() throws Exception {
        // arrange
        FSqrReviewResult reviewResult = new FSqrReviewResult( REVIEWER_1_UUID );
        reviewResult.approveReview( 0L );
        FSqrReviewResultValue persistedReviewResultValue = (FSqrReviewResultValue) reviewResult;

        // act
        FSqrReviewResult preinitialized = new FSqrReviewResult( persistedReviewResultValue );

        // assert
        FSqrReviewResultState result = preinitialized.getResult();
        assertThat( result, equalTo( FSqrReviewResultState.Approved ) );
    }

    @Test
    public void testFSqrReviewResultFSqrReviewResultValue_InitializeWithConcernsReviewState_expectReviewResultConcerns() throws Exception {
        // arrange
        FSqrReviewResult reviewResult = new FSqrReviewResult( REVIEWER_1_UUID );
        reviewResult.concernOnReview( 0L );
        FSqrReviewResultValue persistedReviewResultValue = (FSqrReviewResultValue) reviewResult;

        // act
        FSqrReviewResult preinitialized = new FSqrReviewResult( persistedReviewResultValue );

        // assert
        FSqrReviewResultState result = preinitialized.getResult();
        assertThat( result, equalTo( FSqrReviewResultState.Concerns ) );
    }

}
