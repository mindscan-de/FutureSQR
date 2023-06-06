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
package de.mindscan.futuresqr.domain.databases;

/**
 * 
 */
public interface FSqrProjectAssignedCodeReviewsTable extends FSqrDatabaseTable {

    String selectCodeReviewId( String projectId, String revisionId );

    /**
     * insert code review id to project and revision
     * @param projectId
     * @param revisionId
     * @param reviewId
     */
    void insertCodeReviewId( String projectId, String revisionId, String reviewId );

    /**
     * delete single revision for code review id from project
     * 
     * @param projectId
     * @param revisionId
     */
    void removeCodeReviewId( String projectId, String revisionId );

    /**
     * remove all revisions for a code review. 
     * @param projectId
     * @param reviewId
     */
    void removeAllCodeRevisionsForReview( String projectId, String reviewId );

}
