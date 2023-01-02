package de.mindscan.futuresqr.domainmodel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class FSqrReviewResultTest {

    public final static String REVIEWER_1_UUID = "11111-1111-";

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

}
