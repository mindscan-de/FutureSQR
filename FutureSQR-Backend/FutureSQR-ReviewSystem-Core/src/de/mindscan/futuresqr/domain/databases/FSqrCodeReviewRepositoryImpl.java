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
package de.mindscan.futuresqr.domain.databases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.model.FSqrCodeReview;
import de.mindscan.futuresqr.domain.model.FSqrCodeReviewFactory;
import de.mindscan.futuresqr.domain.model.FSqrRevision;

/**
 * 
 */

public class FSqrCodeReviewRepositoryImpl {

    private FSqrApplicationServices applicationServices;
    private Map<String, Map<String, FSqrCodeReview>> projectIdReviewIdToCodeReviewRepository;
    private Map<String, Map<String, String>> projectIdReviewIdToCodeReviewIdRepository;

    /**
     * 
     */
    public FSqrCodeReviewRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();
        this.projectIdReviewIdToCodeReviewRepository = new HashMap<>();
        this.projectIdReviewIdToCodeReviewIdRepository = new HashMap<>();
    }

    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;
    }

    public void insertReview( String projectId, FSqrCodeReview review ) {
        getOrCreateCodeReviewMap( projectId ).put( review.getReviewId(), review );
        getOrCreateCodeReviewIdMap( projectId ).put( review.getRevisions().get( 0 ).getRevisionId(), review.getReviewId() );
    }

    // ATTENTION: Actually only call on insert.
    private Map<String, FSqrCodeReview> getOrCreateCodeReviewMap( String projectId ) {
        // ATTENTION this allows for a DOS attack because it forces the repository map to grow.
        return projectIdReviewIdToCodeReviewRepository.computeIfAbsent( projectId, pk -> new HashMap<String, FSqrCodeReview>() );
    }

    private Map<String, String> getOrCreateCodeReviewIdMap( String projectId ) {
        // ATTENTION this allows for a DOS attack because it forces the repository map to grow.
        return projectIdReviewIdToCodeReviewIdRepository.computeIfAbsent( projectId, pk -> new HashMap<String, String>() );
    }

    public FSqrCodeReview getReview( String projectId, String reviewId ) {
        if (projectIdReviewIdToCodeReviewRepository.containsKey( projectId )) {
            if (projectIdReviewIdToCodeReviewRepository.get( projectId ).containsKey( reviewId )) {
                return projectIdReviewIdToCodeReviewRepository.get( projectId ).get( reviewId );
            }
            return null;
        }
        return null;
    }

    public boolean hasReviewForProjectAndRevision( String projectid, String revisionid ) {
        if (projectIdReviewIdToCodeReviewIdRepository.containsKey( projectid )) {
            return projectIdReviewIdToCodeReviewIdRepository.get( projectid ).containsKey( revisionid );
        }

        return false;
    }

    public String getReviewIdForProjectAndRevision( String projectid, String revisionid ) {
        if (hasReviewForProjectAndRevision( projectid, revisionid )) {
            return projectIdReviewIdToCodeReviewIdRepository.get( projectid ).get( revisionid );
        }

        return "";
    }

    // selectOpenReviews( projectid )
    public List<FSqrCodeReview> selectOpenReviews( String projectId ) {
        // TODO NEXT: implement this
        return new ArrayList<>();
    }

    // selectClosedReviews( projectid )
    public List<FSqrCodeReview> selectRecentlyClosedReviews( String projectId ) {
        // TODO NEXT: implement this
        return new ArrayList<>();
    }

    // TODO: add revision to review
    // TODO: remove revision from review

    public FSqrCodeReview createReviewFromRevision( String projectid, String revisionid ) {
        FSqrScmProjectConfigurationRepositoryImpl configurationRepository = applicationServices.getConfigurationRepository();

        FSqrRevision revision = applicationServices.getRevisionRepository().getSimpleRevisionInformation( projectid, revisionid );
        FSqrCodeReview codeReview = FSqrCodeReviewFactory.createReviewFromRevision( projectid, revision, configurationRepository );

        // store this in a local code review repository - we ay not like this right now - but this is good enough for now
        insertReview( projectid, codeReview );

        return codeReview;
    }
}
