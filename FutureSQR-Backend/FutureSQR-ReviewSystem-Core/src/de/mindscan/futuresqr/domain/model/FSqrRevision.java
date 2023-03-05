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
import java.util.List;

import de.mindscan.futuresqr.scmaccess.types.ScmBasicRevisionInformation;

/**
 * 
 */
public class FSqrRevision {

    // ATTENTION: 
    // some of these information can be recovered from the SCM, but some are only known by and part of the code review system
    // so some of these information can be dynamically indexed and be temporarily saved. But what should be done, if the
    // information is slow to retrieve like via SVN?
    // maybe just save/use a reference to the ScmBasicRevisionInformation, to distinguish these?
    // so not every information should be stored in a database entry 

    // revision identifier according to the configured scm for a project
    private String revisionId = "";

    // TODO: actually a revision should have a project id, such it can be referenced and selected during list operations
    // for a database object in a row this is a good filter criteria.

    // short revision identifier according to the configured scm for a project
    // this is for compact representation in the ui, to not provide >32 hex long strings.
    private String shortRevisionId = "";

    private String authorName = "";
    private String authorId = "";
    // calculated through user database and from SCM and authorName and Authorid
    private String authorUuid = "";

    private long revisionTimestamp = 0L;
    private String revisionDate = "";
    private String revisionShortDate = "";
    private String revisionRelativeDate = ""; // dynamic?

    // full commit message, or just stripped of head?
    private String commitMessageFull = "";

    // only first line of commitMessage
    private String commitMessageHead = "";

    private List<String> parentIds = new ArrayList<>();
    private List<String> shortParentIds = new ArrayList<>();

    private boolean hasAttachedReview = false; // dynamic?, check for empty string?
    private String reviewId = "";
    private boolean reviewClosed = false;

    private String branchName;

    // urls to the CI-System?, to see how it built and what state?
    // or ci-references? Are those references from the ci table to the revision?
    // or should they be part of the ui joining this information, because here it is only creating overhead? 
    // actually the buildstate is nice, but not yet in focus.

    /**
     * 
     */
    public FSqrRevision() {
        // intentionally left blank.
    }

    /**
     * 
     */
    public FSqrRevision( ScmBasicRevisionInformation scmbasicInfo ) {
        this.revisionId = scmbasicInfo.revisionId;
        this.shortRevisionId = scmbasicInfo.shortRevisionId;
        this.authorName = scmbasicInfo.authorName;
        this.authorId = scmbasicInfo.authorId;
        this.revisionDate = scmbasicInfo.date;
        this.revisionShortDate = scmbasicInfo.shortDate;
        this.revisionRelativeDate = scmbasicInfo.relDate;
        this.commitMessageFull = scmbasicInfo.message;
        this.commitMessageHead = scmbasicInfo.message;
        this.parentIds = scmbasicInfo.parentIds;
        this.shortParentIds = scmbasicInfo.shortParentIds;
        this.branchName = scmbasicInfo.branchName;
    }

    /**
     * @param authorId the authorId to set
     */
    public void setAuthorId( String authorId ) {
        this.authorId = authorId;
    }

    /**
     * @param authorName the authorName to set
     */
    public void setAuthorName( String authorName ) {
        this.authorName = authorName;
    }

    /**
     * @param authorUuid the authorUuid to set
     */
    public void setAuthorUuid( String authorUuid ) {
        this.authorUuid = authorUuid;
    }

    /**
     * @param commitMessageFull the commitMessageFull to set
     */
    public void setCommitMessageFull( String commitMessageFull ) {
        this.commitMessageFull = commitMessageFull;
    }

    /**
     * @param commitMessageHead the commitMessageHead to set
     */
    public void setCommitMessageHead( String commitMessageHead ) {
        this.commitMessageHead = commitMessageHead;
    }

    /**
    * @param hasAttachedReview the hasAttachedReview to set
    */
    public void setHasAttachedReview( boolean hasAttachedReview ) {
        this.hasAttachedReview = hasAttachedReview;
    }

    /**
     * @param parentIds the parentIds to set
     */
    public void setParentIds( List<String> parentIds ) {
        this.parentIds = parentIds;
    }

    /**
     * @param shortParentIds the shortParentIds to set
     */
    public void setShortParentIds( List<String> shortParentIds ) {
        this.shortParentIds = shortParentIds;
    }

    /**
     * @param reviewId the reviewId to set
     */
    public void setReviewId( String reviewId ) {
        this.reviewId = reviewId;
    }

    /**
     * @param reviewClosed
     */
    public void setReviewClosed( boolean reviewClosed ) {
        this.reviewClosed = reviewClosed;
    }

    /**
     * @param revisionDate the revisionDate to set
     */
    public void setRevisionDate( String revisionDate ) {
        this.revisionDate = revisionDate;
    }

    /**
     * @param revisionId the revisionId to set
     */
    public void setRevisionId( String revisionId ) {
        this.revisionId = revisionId;
    }

    /**
     * @param revisionRelativeDate the revisionRelativeDate to set
     */
    public void setRevisionRelativeDate( String revisionRelativeDate ) {
        this.revisionRelativeDate = revisionRelativeDate;
    }

    /**
     * @param revisionTimestamp the revisionTimestamp to set
     */
    public void setRevisionTimestamp( long revisionTimestamp ) {
        this.revisionTimestamp = revisionTimestamp;
    }

    /**
     * @param shortRevisionId the shortRevisionId to set
     */
    public void setShortRevisionId( String shortRevisionId ) {
        this.shortRevisionId = shortRevisionId;
    }

    /**
     * @return the authorId
     */
    public String getAuthorId() {
        return authorId;
    }

    /**
     * @return the authorName
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * @return the authorUuid
     */
    public String getAuthorUuid() {
        return authorUuid;
    }

    /**
     * @return the commitMessageFull
     */
    public String getCommitMessageFull() {
        return commitMessageFull;
    }

    /**
     * @return the commitMessageHead
     */
    public String getCommitMessageHead() {
        return commitMessageHead;
    }

    /**
     * @return the parentIds
     */
    public List<String> getParentIds() {
        return parentIds;
    }

    /**
     * @return the reviewId
     */
    public String getReviewId() {
        return reviewId;
    }

    /**
     * @return the revisionDate
     */
    public String getRevisionDate() {
        return revisionDate;
    }

    /**
     * @return the revisionId
     */
    public String getRevisionId() {
        return revisionId;
    }

    /**
     * @return the revisionRelativeDate
     */
    public String getRevisionRelativeDate() {
        return revisionRelativeDate;
    }

    /**
     * @return the revisionTimestamp
     */
    public long getRevisionTimestamp() {
        return revisionTimestamp;
    }

    /**
     * @return the shortParentIds
     */
    public List<String> getShortParentIds() {
        return shortParentIds;
    }

    /**
     * @return the shortRevisionId
     */
    public String getShortRevisionId() {
        return shortRevisionId;
    }

    /**
     * @return the revisionShortDate
     */
    public String getRevisionShortDate() {
        return revisionShortDate;
    }

    /**
     * @return
     */
    public boolean hasReview() {
        return hasAttachedReview;
    }

    /**
     * @return the branchName
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * @return the reviewClosed
     */
    public boolean isReviewClosed() {
        return reviewClosed;
    }

}
