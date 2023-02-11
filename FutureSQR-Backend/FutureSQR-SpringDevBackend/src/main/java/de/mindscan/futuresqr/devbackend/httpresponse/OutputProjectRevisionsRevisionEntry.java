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

import de.mindscan.futuresqr.domain.model.FSqrRevision;

/**
 * 
 */
public class OutputProjectRevisionsRevisionEntry {
    public String shortrev = "";
    public String revisionid = "";

    public String authorname = "";
    public String authorid = "";
    public String date = "";
    public String shortdate = "";
    public String reldate = "";
    public String message = "";

    // TODO: must be reworked in frontend, such that this becomes a list
    public String parents = "";
    // TODO: must be reworked in frontend, such that this becomes a list         
    public String parentsshort = "";

    public String authorUuid = "";

    public boolean hasReview = false;
    public String reviewID = "";

    /**
     * 
     */
    public OutputProjectRevisionsRevisionEntry() {
        // intentionally left blank;
    }

    public OutputProjectRevisionsRevisionEntry( FSqrRevision rev ) {
        this.shortrev = rev.getShortRevisionId();
        this.revisionid = rev.getRevisionId();
        this.authorname = rev.getAuthorName();
        this.authorid = rev.getAuthorId();
        this.authorUuid = rev.getAuthorUuid();

        this.date = rev.getRevisionDate();
        this.shortdate = rev.getRevisionShortDate();
        this.reldate = rev.getRevisionRelativeDate();

        this.message = rev.getCommitMessageFull();
        this.parents = String.join( ",", rev.getParentIds() );
        this.parentsshort = String.join( ",", rev.getShortParentIds() );

        this.hasReview = rev.hasReview();
        this.reviewID = rev.getReviewId();
    }

}
