package de.mindscan.futuresqr.domain.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class FSqrCodeReviewTest {

    public final static String REVIEWER_1_UUID = "11111-1111-";
    public final static String REVIEWER_2_UUID = "22222-1111-";

    @Test
    public void testGetCurrentReviewState_CtorOnly_expectReviewStateIsOpen() throws Exception {
        // arrange
        FSqrCodeReview review = new FSqrCodeReview();

        // act
        FSqrCodeReviewLifecycleState result = review.getCurrentReviewState();

        // assert
        assertThat( result, equalTo( FSqrCodeReviewLifecycleState.Open ) );
    }

    @Test
    public void testCloseReview_CtorThenClose_expectReviewStateIsClosed() throws Exception {
        // arrange
        FSqrCodeReview review = new FSqrCodeReview();

        // act
        review.closeReview( REVIEWER_1_UUID );

        // assert
        FSqrCodeReviewLifecycleState result = review.getCurrentReviewState();
        assertThat( result, equalTo( FSqrCodeReviewLifecycleState.Closed ) );
    }

    @Test
    public void testReopenReview_CtorThenCloseThenReopen_expectedReviewStateIsOpen() throws Exception {
        // arrange
        FSqrCodeReview review = new FSqrCodeReview();
        review.closeReview( REVIEWER_1_UUID );

        // act
        review.reopenReview( REVIEWER_2_UUID );

        // assert
        FSqrCodeReviewLifecycleState result = review.getCurrentReviewState();
        assertThat( result, equalTo( FSqrCodeReviewLifecycleState.Open ) );
    }

    @Test
    public void testDeleteReview_CtorThenDelete_expectReviewStateIsDeleted() throws Exception {
        // arrange
        FSqrCodeReview review = new FSqrCodeReview();

        // act
        review.deleteReview( REVIEWER_1_UUID );

        // assert
        FSqrCodeReviewLifecycleState result = review.getCurrentReviewState();
        assertThat( result, equalTo( FSqrCodeReviewLifecycleState.Deleted ) );
    }

    @Test
    public void testReopenReview_CtorThenDeleteThenReopen_expectReviewStateIsOpen() throws Exception {
        // arrange
        FSqrCodeReview review = new FSqrCodeReview();
        review.deleteReview( REVIEWER_1_UUID );

        // act
        review.reopenReview( REVIEWER_2_UUID );

        // assert
        FSqrCodeReviewLifecycleState result = review.getCurrentReviewState();
        assertThat( result, equalTo( FSqrCodeReviewLifecycleState.Open ) );
    }

    @Test
    public void testIsUnassigned_CtorOnly_expectReviewIsUnassigned() throws Exception {
        // arrange
        FSqrCodeReview review = new FSqrCodeReview();

        // act
        boolean result = review.isUnassigned();

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testIsUnassigned_CtorThenAddReviewer_expectReviewIsAssignedToSomeone() throws Exception {
        // arrange
        FSqrCodeReview review = new FSqrCodeReview();
        review.addReviewer( REVIEWER_2_UUID, REVIEWER_2_UUID );

        // act
        boolean result = review.isUnassigned();

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsUnassigned_CtorThenAddReviewerOnceThenRemoveSameReviewer_expectReviewIsNotAssigned() throws Exception {
        // arrange
        FSqrCodeReview review = new FSqrCodeReview();
        review.addReviewer( REVIEWER_2_UUID, REVIEWER_2_UUID );
        review.removeReviewer( REVIEWER_2_UUID, REVIEWER_2_UUID );

        // act
        boolean result = review.isUnassigned();

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testIsUnassigned_CtorThenAddReviewerTwiceThenRemoveSameReviewer_expectReviewIsNotAssigned() throws Exception {
        // arrange
        FSqrCodeReview review = new FSqrCodeReview();
        review.addReviewer( REVIEWER_2_UUID, REVIEWER_2_UUID );
        review.addReviewer( REVIEWER_2_UUID, REVIEWER_2_UUID );
        review.removeReviewer( REVIEWER_2_UUID, REVIEWER_2_UUID );

        // act
        boolean result = review.isUnassigned();

        // assert
        assertThat( result, equalTo( true ) );
    }

}
