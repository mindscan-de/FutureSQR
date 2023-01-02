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
 * This is the value object, which contains the review result of a reviewer.
 */
public class FSqrReviewResultValue {

    protected String reviewerId = null;
    protected FSqrReviewResultState result = FSqrReviewResultState.Incomplete;
    protected long userAssignedTS = -1L;

    // TODO: 
    // - actually it should be stored, what was reviewed (eg.list of revisionIds) by whom and when, (e.g. for tracability, e.g aspice requirements)
    // - this is needed to figure out to whom to present what needs to be reviewed since last reviewresult (usability)
    // so maybe this needs to be revisited once more.
    protected long userLastDecidedTS = -1L;
    protected long resultLastModifiedTS = -1L;

    /**
     * 
     */
    public FSqrReviewResultValue() {
        // intentionally left blank. Typically this constructor should only be used by unserializers
    }

    public FSqrReviewResultValue( String reviewerId ) {
        this.reviewerId = reviewerId;
    }

    public FSqrReviewResultValue( FSqrReviewResultValue otherValue ) {
        this.reviewerId = otherValue.reviewerId;
        this.result = otherValue.result;
        this.userAssignedTS = otherValue.userAssignedTS;
        this.userLastDecidedTS = otherValue.userLastDecidedTS;
        this.resultLastModifiedTS = otherValue.resultLastModifiedTS;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public FSqrReviewResultState getResult() {
        return result;
    }

    protected void setResult( FSqrReviewResultState result ) {
        this.result = result;
    }

    public long getResultLastModifiedTS() {
        return resultLastModifiedTS;
    }

    protected void updateResultLastModifiedTS( long decisionTimestamp ) {
        this.resultLastModifiedTS = decisionTimestamp;
    }

    public long getUserAssignedTS() {
        return userAssignedTS;
    }

    public long getUserLastDecidedTS() {
        return userLastDecidedTS;
    }

}
