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
package de.mindscan.futuresqr.domain.repository;

import java.util.List;

import de.mindscan.futuresqr.domain.model.FSqrCodeReview;
import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;

/**
 * 
 */
public interface FSqrCodeReviewRepository {

    void concernCodeReview( String projectId, String reviewId, String reviewerId );

    void approveCodeReview( String projectId, String reviewId, String reviewerId );

    void removeReviewerFromCodeReview( String projectId, String reviewId, String reviewerId, String whoRemovedId );

    void addReviewerToCodeReview( String projectId, String reviewId, String reviewerId, String whoAddedId );

    void retractCodeReview( String projectId, String reviewId, String reviewerId );

    void deleteReview( String projectId, String reviewId, String whoDeletedUUID );

    void reopenReview( String projectId, String reviewId, String whoReopenedUUID );

    void closeReview( String projectId, String reviewId, String whoClosedUUID );

    List<FSqrSystemUser> getSuggestedReviewers( String projectId, String reviewId );

    List<FSqrRevision> getRevisionsForReview( String projectId, String reviewId );

    FSqrCodeReview createReviewFromRevision( String projectid, String revisionid );

    void removeRevisionFromReview( String projectId, String reviewId, String revisionId );

    void addRevisionToReview( String projectId, String reviewId, String revisionId );

    List<FSqrCodeReview> selectRecentlyClosedReviews( String projectId );

    List<FSqrCodeReview> selectOpenReviews( String projectId );

}
