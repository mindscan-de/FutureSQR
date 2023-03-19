/**
 * 
 * MIT License
 *
 * Copyright (c) 2023 Maxim Gansert, Mindscan
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public class FSqrCodeReviewValue {

    // the short name of the project - for administrative reasons we should also have a project uuid.
    private String projectId;

    // each review has a id - uuid? with translation to a review identifier?
    // should this be simply a number...
    private String reviewId;

    // each review has a current review state.
    private FSqrCodeReviewLifecycleState state = FSqrCodeReviewLifecycleState.Open;

    private Map<String, FSqrReviewResult> reviewerResults = new HashMap<>();

    private String reviewTitle = "";

    private String reviewDescription = "";

    protected List<FSqrRevision> revisions = new ArrayList<>();

    public Map<String, FSqrReviewResult> getReviewerResultsMap() {
        return reviewerResults;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId( String projectId ) {
        this.projectId = projectId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId( String reviewId ) {
        this.reviewId = reviewId;
    }

    public FSqrCodeReviewLifecycleState getCurrentReviewState() {
        return state;
    }

    public void updateCurrenReviewState( FSqrCodeReviewLifecycleState newState ) {
        this.state = newState;
    }

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

    public List<FSqrRevision> getRevisions() {
        return revisions;
    }

    public void addRevision( FSqrRevision revisionToAdd ) {
        this.revisions.add( revisionToAdd );
    }

    public void addFirstRevision( FSqrRevision firstRevision ) {
        this.revisions.add( 0, firstRevision );
    }

    public void removeRevision( FSqrRevision revisionToRemove ) {
        this.revisions.removeIf( r -> revisionToRemove.getRevisionId().equals( r.getRevisionId() ) );
    }

    public void removeRevisionById( String revisionToRemoveId ) {
        this.revisions.removeIf( r -> revisionToRemoveId.equals( r.getRevisionId() ) );
    }
}
