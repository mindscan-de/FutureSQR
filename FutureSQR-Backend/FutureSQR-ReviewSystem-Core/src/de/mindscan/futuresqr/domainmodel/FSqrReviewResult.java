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
 * 
 */
public class FSqrReviewResult {

    private String reviewerId = "";

    private FSqrReviewResultState result;

    // when was this user added
    private long userAddedTS;
    // TODO added by whom? -> maybe just use a logging table for this detail?

    // when was a decision made. -> this could be converted into a map, where the timestamps are the keys and the
    // values are the revisionlists
    private long userLastDecidedTS;

    // when last modification was made to result(e.g. when a revision was added) the result must be updated
    private long resultLastModifiedTS;

    // TODO: 
    // - actually it should be stored, what was reviewed (eg.list of revisionIds) by whom and when, (e.g. for tracability, e.g aspice requirements)
    // - this is needed to figure out to whom to present what needs to be reviewed since last reviewresult (usability)
}
