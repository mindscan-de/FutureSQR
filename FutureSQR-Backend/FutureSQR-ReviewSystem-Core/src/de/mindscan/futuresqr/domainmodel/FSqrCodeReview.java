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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the model used for the CodeReview Model.
 * 
 * A Code Review is only kept in memory as long as it is used / requested. It is a model for a code review, but there
 * is a different life cycle according to the data linked is present in the memory.  
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
    private Map<String, FSqrReviewResult> reviewerResults = new HashMap<>();

    // TODO derive boolean (unassigned state) from reviewerlist
    // TODO derive boolean (ready_to_close state) from reviewerlist

    // closeReview ( userid who )
    // reopenReview( userid who )
    // getReviewState

    // getDiscussionThreads
    // discussionthreadids?
}
