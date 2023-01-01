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
package de.mindscan.futuresqr.domainmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the model used for the CodeReview Model.
 */
public class FSqrCodeReview {

    // the short name of the project - for administrative reasons we should also have a project uuid.
    private String projectId;

    // each review has a id - uuid? with translation to a review identifier?
    private String reviewId;

    // each review has a current review state.
    private FSqrCodeReviewLifecycleState state;

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
    // remove reviewer ( userid reviewer, userid whoremoved ) 
    // approveReview ( userid who )
    // concernReview ( userid who )
    private List<FSqrReviewResult> reviewerResults = new ArrayList<>();

    // TODO derive boolean (unassigned state) from reviwerlist
    // TODO derive boolean (ready_to_close state) from reviewerlist

    // closeReview ( userid who )
    // reopenReview( userid who )
    // getReviewState

    // getDiscussionThreads
    // discussionthreadids?
}
