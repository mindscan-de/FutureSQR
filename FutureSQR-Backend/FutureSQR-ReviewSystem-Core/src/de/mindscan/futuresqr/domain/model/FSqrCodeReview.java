/**
 * 
 * MIT License
 *
 * Copyright (c) 2022, 2023 Maxim Gansert, Mindscan
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
import java.util.stream.Collectors;

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

    // getInformation

    // TODO: maybe use a set?
    private List<String> revisionAuthorUUIDs = new ArrayList<>();

    // add reviewer ( userid reviewer, userid whoadded ) 
    public void addReviewer( String reviewerId, String whoAdded ) {
        if (!hasReviewer( reviewerId )) {
            FSqrReviewResult reviewResult = new FSqrReviewResult( reviewerId );
            long now = this.getCurrentTimestamp();
            reviewResult.assignReview( now );
            addReviewResult( reviewResult );
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
            long now = this.getCurrentTimestamp();
            getReviewResultFor( reviewerId ).approveReview( now );
        }
    }

    // concernReview ( userid who )
    public void concernReview( String reviewerId ) {
        if (hasReviewer( reviewerId )) {
            long now = this.getCurrentTimestamp();
            getReviewResultFor( reviewerId ).concernOnReview( now );
        }
    }

    // rollbackReview
    public void rollbackReview( String reviewerId ) {
        if (hasReviewer( reviewerId )) {
            long now = this.getCurrentTimestamp();
            getReviewResultFor( reviewerId ).rollbackReview( now );
        }
    }

    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    public boolean isReadyToClose() {
        // it can be closed if no revision is set in review.
        if (getRevisions().isEmpty()) {
            return true;
        }

        if (isUnassigned()) {
            return false;
        }

        List<FSqrReviewResult> nonApprovers = getReviewerResultsMap().values().stream().filter( result -> result.result != FSqrReviewResultState.Approved )
                        .collect( Collectors.toList() );

        // derive boolean (ready_to_close state) from reviewerlist
        return nonApprovers.size() == 0;
    }

    // closeReview ( userid who )
    public void closeReview( String whoClosed ) {
        this.updateCurrenReviewState( FSqrCodeReviewLifecycleState.Closed );
        setClosedTimestamp( getCurrentTimestamp() );
    }

    // reopenReview( userid who )
    public void reopenReview( String whoReopened ) {
        this.updateCurrenReviewState( FSqrCodeReviewLifecycleState.Open );
        setOpenedTimestamp( getCurrentTimestamp() );
    }

    public void deleteReview( String whoDeleted ) {
        // TODO: clear revisions from review, clear reviewers, clear authors, etc.
        this.updateCurrenReviewState( FSqrCodeReviewLifecycleState.Deleted );
        setDeletedTimestamp( getCurrentTimestamp() );
    }

    public void addRevision( FSqrRevision revisionToAdd ) {
        // TODO: should figure out, at which position to add, but let someone else decide? 
        // for now just assume, the revisions are in correct order, from oldest to newest.
        super.addRevision( revisionToAdd );

        updateAuthors();
    }

    public void removeRevision( FSqrRevision revisionToRemove ) {
        super.removeRevision( revisionToRemove );

        updateAuthors();
    }

    public void removeRevisionById( String revisionToRemoveId ) {
        super.removeRevisionById( revisionToRemoveId );

        updateAuthors();
    }

    public void addFirstRevision( FSqrRevision firstRevision ) {
        super.addFirstRevision( firstRevision );

        updateAuthors();
    }

    private void updateAuthors() {
        this.revisionAuthorUUIDs = new ArrayList<>( getRevisions().stream().map( rev -> rev.getAuthorUuid() ).collect( Collectors.toSet() ) );
    }

    public List<String> getRevisionAuthorUUIDs() {
        return revisionAuthorUUIDs;
    }

    public boolean isClosedCodeReview( ) {
        return getCurrentReviewState() == FSqrCodeReviewLifecycleState.Closed;
    }

    public boolean isOpenCodeReview( ) {
        return getCurrentReviewState() == FSqrCodeReviewLifecycleState.Open;
    }

}
