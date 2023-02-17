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
package de.mindscan.futuresqr.devbackend.httpresponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.mindscan.futuresqr.domain.model.FSqrCodeReview;
import de.mindscan.futuresqr.domain.model.FSqrReviewResult;

/**
 * 
 */
public class OutputReviewModel {
    public String reviewId = "";
    public String reviewTitle = "";
    public String reviewDescription = "";

    public List<String> reviewRevisions = new ArrayList<>();
    // actually a list of uuid in case of a known user or the unresolved author
    public List<String> reviewAuthors = new ArrayList<>();

    public boolean reviewReadyToClose = false;
    public boolean reviewUnassigned = true;

    public Map<String, OutputReviewResultEntry> reviewReviewersResults = new HashMap<>();
    public String reviewFkProjectId = "";

    // Either "Open", "Closed", "Deleted"
    public String reviewLifecycleState = "";

    /**
     * 
     */
    public OutputReviewModel() {
        // intentionally left blank
    }

    public OutputReviewModel( FSqrCodeReview codeReview ) {
        this.reviewId = codeReview.getReviewId();
        this.reviewTitle = codeReview.getReviewTitle();
        this.reviewDescription = codeReview.getReviewDescription();

        // convert the reviewRevisions to revision ids only
        this.reviewRevisions = new ArrayList<>();
        codeReview.getRevisions().stream().forEach( r -> this.reviewRevisions.add( r.getRevisionId() ) );

        // convert review Authors
        this.reviewAuthors = codeReview.getRevisionAuthorUUIDs();

        // TODO: this.reviewReadyToClose = codeReview.

        this.reviewUnassigned = codeReview.isUnassigned();
        convertReviewerResults( codeReview.getReviewerResultsMap().entrySet() );
        // TODO: this.reviewReviewersResults = codeReview.

        this.reviewFkProjectId = codeReview.getProjectId();
        this.reviewLifecycleState = codeReview.getCurrentReviewState().name();
    }

    private void convertReviewerResults( Set<Entry<String, FSqrReviewResult>> results ) {
        for (Entry<String, FSqrReviewResult> entry : results) {
            String key = entry.getKey();
            FSqrReviewResult internalResult = entry.getValue();

            reviewReviewersResults.put( key, new OutputReviewResultEntry( internalResult ) );
        }
    }

}
