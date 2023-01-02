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
package de.mindscan.futuresqr.domainmodel;

/**
 * This represents a ReviewResult for each Reviewer. 
 */
public class FSqrReviewResult extends FSqrReviewResultValue {

    /**
     * 
     */
    public FSqrReviewResult( String reviewerId ) {
        this.reviewerId = reviewerId;
        this.result = FSqrReviewResultState.Incomplete;
    }

    /**
     * 
     */
    public FSqrReviewResult( FSqrReviewResultValue value ) {
        this.reviewerId = value.reviewerId;
        this.result = value.result;
        this.userAssignedTS = value.userAssignedTS;
        this.userLastDecidedTS = value.userLastDecidedTS;
        this.resultLastModifiedTS = value.resultLastModifiedTS;
    }

    // TODO: 
    // - actually it should be stored, what was reviewed (eg.list of revisionIds) by whom and when, (e.g. for tracability, e.g aspice requirements)
    // - this is needed to figure out to whom to present what needs to be reviewed since last reviewresult (usability)

    public void approveReview( long decisionTimestamp ) {
        setResult( FSqrReviewResultState.Approved );

        this.userLastDecidedTS = decisionTimestamp;
        this.resultLastModifiedTS = decisionTimestamp;
    }

    public void concernsOnRrview( long decisionTimestamp ) {
        setResult( FSqrReviewResultState.Concerns );

        this.userLastDecidedTS = decisionTimestamp;
        this.resultLastModifiedTS = decisionTimestamp;
    }

    public void rollbackReview( long decisionTimestamp ) {
        setResult( FSqrReviewResultState.Incomplete );

        this.userLastDecidedTS = decisionTimestamp;
        this.resultLastModifiedTS = decisionTimestamp;
    }

    public void assignReview( long reviewAssignedTimestamp ) {
        this.userAssignedTS = reviewAssignedTimestamp;
        this.resultLastModifiedTS = reviewAssignedTimestamp;
    }

}
