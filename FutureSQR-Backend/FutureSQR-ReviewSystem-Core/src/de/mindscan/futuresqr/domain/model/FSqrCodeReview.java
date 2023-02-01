/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.futuresqr.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the model used for the CodeReview Model.
 * 
 * A Code Review is only kept in memory as long as it is used / requested. It is a model for a code review, but there
 * is a different life cycle according to the data linked is present in the memory. There is no need to keep a code
 * review in memory which is not accessed any more.
 * 
 */
// TODO: think about composition over inheritance, so we can have observable ValueObjects  
public class FSqrCodeReview extends FSqrCodeReviewValue {

    private String reviewTitle = "";
    private String reviewDescription = "";

    // getInformation

    // getRevisions
    // addRevision
    // removeRevison
    private List<FSqrRevision> revisions = new ArrayList<>();

    // these revisions have authors, they should be calculated once the revisions are added/removed
    private List<String> revisionAuthorUUIDs = new ArrayList<>();

    // add reviewer ( userid reviewer, userid whoadded ) 
    public void addReviewer( String reviewerId, String whoAdded ) {
        if (!hasReviewer( reviewerId )) {
            FSqrReviewResult reviewResult = new FSqrReviewResult( reviewerId );
            long now = 0L;
            reviewResult.assignReview( now );
            this.getReviewerResultsMap().put( reviewerId, reviewResult );
        }
    }

    // remove reviewer ( userid reviewer, userid whoremoved ) 
    public void removeReviewer( String reviewerId, String whoRemoved ) {
        if (hasReviewer( reviewerId )) {
            this.getReviewerResultsMap().remove( reviewerId );
        }
    }

    // approveReview ( userid who )
    public void approveReview( String reviewerId ) {
        if (hasReviewer( reviewerId )) {
            // TODO: calculate correct current timestamp;
            long now = 0L;
            this.getReviewerResultsMap().get( reviewerId ).approveReview( now );
        }
    }

    // concernReview ( userid who )
    public void concernReview( String reviewerId ) {
        if (hasReviewer( reviewerId )) {
            // TODO: calculate correct current timestamp;
            long now = 0L;
            this.getReviewerResultsMap().get( reviewerId ).concernOnReview( now );
        }
    }

    // rollbackReview
    public void rollbackReview( String reviewerId ) {
        if (hasReviewer( reviewerId )) {
            // TODO: calculate correct current timestamp;
            long now = 0L;
            this.getReviewerResultsMap().get( reviewerId ).rollbackReview( now );
        }
    }

    boolean hasReviewer( String reviewerId ) {
        return getReviewerResultsMap().containsKey( reviewerId );
    }

    // derive boolean (unassigned state) from reviewerlist
    boolean isUnassigned() {
        return getReviewerResultsMap().isEmpty();
    }

    // derive boolean (ready_to_close state) from reviewerlist
    boolean isReadyToClose() {
        return false;
    }

    // closeReview ( userid who )
    public void closeReview( String whoClosed ) {
        this.updateCurrenReviewState( FSqrCodeReviewLifecycleState.Closed );
    }

    // reopenReview( userid who )
    public void reopenReview( String whoReopened ) {
        this.updateCurrenReviewState( FSqrCodeReviewLifecycleState.Open );
    }

    public void deleteReview( String whoDeleted ) {
        // TODO: clear revisions from review, clear reviewers, clear authors, etc.
        this.updateCurrenReviewState( FSqrCodeReviewLifecycleState.Deleted );
    }

    // TODO: test these

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle( String reviewTitle ) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription( String reviewDescription ) {
        this.reviewDescription = reviewDescription;
    }

    // getDiscussionThreads
    // discussionthreadids?
}
