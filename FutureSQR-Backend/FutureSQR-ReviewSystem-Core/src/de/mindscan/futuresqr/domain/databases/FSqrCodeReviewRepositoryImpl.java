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
import de.mindscan.futuresqr.domain.model.FSqrCodeReviewLifecycleState;
import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;

/**
 * 
 */

public class FSqrCodeReviewRepositoryImpl {

    private FSqrApplicationServices applicationServices;
    private Map<String, Map<String, FSqrCodeReview>> projectIdReviewIdToCodeReviewRepository;
    private Map<String, Map<String, String>> projectIdRevisionIdToCodeReviewIdRepository;

    /**
     * 
     */
    public FSqrCodeReviewRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();
        this.projectIdReviewIdToCodeReviewRepository = new HashMap<>();
        this.projectIdRevisionIdToCodeReviewIdRepository = new HashMap<>();
    }

    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;
    }

    public void insertReview( String projectId, FSqrCodeReview review ) {
        getOrCreateCodeReviewMap( projectId ).put( review.getReviewId(), review );
        getOrCreateCodeReviewIdMap( projectId ).put( review.getFirstRevisionId(), review.getReviewId() );
    }

    // ATTENTION: Actually only call on insert.
    private Map<String, FSqrCodeReview> getOrCreateCodeReviewMap( String projectId ) {
        // ATTENTION this allows for a DOS attack because it forces the repository map to grow.
        return projectIdReviewIdToCodeReviewRepository.computeIfAbsent( projectId, pk -> new HashMap<String, FSqrCodeReview>() );
    }

    private Map<String, String> getOrCreateCodeReviewIdMap( String projectId ) {
        // ATTENTION this allows for a DOS attack because it forces the repository map to grow.
        return projectIdRevisionIdToCodeReviewIdRepository.computeIfAbsent( projectId, pk -> new HashMap<String, String>() );
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
        if (projectIdRevisionIdToCodeReviewIdRepository.containsKey( projectid )) {
            return projectIdRevisionIdToCodeReviewIdRepository.get( projectid ).containsKey( revisionid );
        }

        return false;
    }

    public String getReviewIdForProjectAndRevision( String projectid, String revisionid ) {
        if (hasReviewForProjectAndRevision( projectid, revisionid )) {
            return projectIdRevisionIdToCodeReviewIdRepository.get( projectid ).get( revisionid );
        }

        return "";
    }

    public List<FSqrCodeReview> selectOpenReviews( String projectId ) {
        ArrayList<FSqrCodeReview> resultList = new ArrayList<>();

        if (projectIdReviewIdToCodeReviewRepository.containsKey( projectId )) {
            Map<String, FSqrCodeReview> projectMap = projectIdReviewIdToCodeReviewRepository.get( projectId );
            projectMap.values().stream().filter( r -> r.getCurrentReviewState() == FSqrCodeReviewLifecycleState.Open ).forEach( r -> resultList.add( r ) );
        }

        return resultList;
    }

    public List<FSqrCodeReview> selectRecentlyClosedReviews( String projectId ) {
        ArrayList<FSqrCodeReview> resultList = new ArrayList<>();

        if (projectIdReviewIdToCodeReviewRepository.containsKey( projectId )) {
            Map<String, FSqrCodeReview> projectMap = projectIdReviewIdToCodeReviewRepository.get( projectId );
            projectMap.values().stream().filter( r -> r.getCurrentReviewState() == FSqrCodeReviewLifecycleState.Closed ).forEach( r -> resultList.add( r ) );
        }

        return resultList;
    }

    public void closeReview( String projectId, String reviewId, String whoClosedUUID ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            codeReview.closeReview( whoClosedUUID );
        }
    }

    public void reopenReview( String projectId, String reviewId, String whoReopenedUUID ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            codeReview.reopenReview( whoReopenedUUID );
        }
    }

    public void deleteReview( String projectId, String reviewId, String whoDeletedUUID ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            // TODO NEXT: cleanup/unregister all referenced revisions, such they can be reviewed 
            // projectIdRevisionIdToCodeReviewIdRepository
            codeReview.deleteReview( whoDeletedUUID );
        }
    }

    public void addRevisionToReview( String projectId, String reviewId, String revisionId ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            // we need to retrieve this revison ... THEN ADD IT
            // TODO: actually it is more complicated, we need to know where to add it... 
            // TODO: get history firstrevision to revisionid, and count number of elements in list of code review -> that is the insert position. 
            FSqrRevision revisionToAdd = applicationServices.getRevisionRepository().getSimpleRevisionInformation( projectId, revisionId );
            codeReview.addRevision( revisionToAdd );

            // add revision also to (project x revision) table - to associate revision with review. 
            // TODO: this should be done in a separate method
            getOrCreateCodeReviewIdMap( projectId ).put( revisionId, reviewId );
        }
    }

    public void removeRevisionFromReview( String projectId, String reviewId, String revisionId ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            // FSqrRevision revisionToRemove = applicationServices.getRevisionRepository().getSimpleRevisionInformation( projectId, revisionId );

            // remove revision from review
            codeReview.removeRevisionById( revisionId );

            // remove revision from (project x revision) table - to mark it free again.
            getOrCreateCodeReviewIdMap( projectId ).remove( revisionId );
        }
    }

    public FSqrCodeReview createReviewFromRevision( String projectid, String revisionid ) {
        FSqrScmProjectConfigurationRepositoryImpl configurationRepository = applicationServices.getConfigurationRepository();

        FSqrRevision revision = applicationServices.getRevisionRepository().getSimpleRevisionInformation( projectid, revisionid );
        FSqrCodeReview codeReview = FSqrCodeReviewFactory.createReviewFromRevision( projectid, revision, configurationRepository );

        // store this in a local code review repository - we ay not like this right now - but this is good enough for now
        insertReview( projectid, codeReview );

        return codeReview;
    }

    public List<FSqrRevision> getRevisionsForReview( String projectId, String reviewId ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        return codeReview.getRevisions();
    }

    public List<FSqrSystemUser> getSuggestedReviewers( String projectId, String reviewId ) {
        FSqrScmUserRepositoryImpl userRepository = this.applicationServices.getUserRepository();

        // TODO: calculate the suggested reviewers somehow.
        List<String> suggestedReviewers = new ArrayList<>();
        suggestedReviewers.add( "8ce74ee9-48ff-3dde-b678-58a632887e31" );
        suggestedReviewers.add( "f5fc8449-3049-3498-9f6b-ce828515bba2" );

        List<FSqrSystemUser> converted = new ArrayList<>();
        suggestedReviewers.stream() //
                        .filter( uuid -> userRepository.isUserUUIDPresent( uuid ) )//
                        .forEach( uuid -> converted.add( userRepository.getUserByUUID( uuid ) ) );

        return converted;
    }

    public void addReviewerToCodeReview( String projectId, String reviewId, String reviewerId, String whoAddedId ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            codeReview.addReviewer( reviewerId, whoAddedId );
        }
    }

}
