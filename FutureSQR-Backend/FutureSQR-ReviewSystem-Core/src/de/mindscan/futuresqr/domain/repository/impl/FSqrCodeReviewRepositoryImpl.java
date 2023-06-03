/**
 * 
 * MIT License
 *
 * Copyright (c) 2022, 2023 Maxim Gansert, Mindscan
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
package de.mindscan.futuresqr.domain.repository.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.mindscan.futuresqr.domain.application.ApplicationServicesSetter;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.databases.FSqrCodeReviewTable;
import de.mindscan.futuresqr.domain.databases.FSqrProjectAssignedCodeReviewsTable;
import de.mindscan.futuresqr.domain.databases.impl.FSqrCodeReviewTableImpl;
import de.mindscan.futuresqr.domain.databases.impl.FSqrProjectAssignedCodeReviewsTableImpl;
import de.mindscan.futuresqr.domain.model.FSqrCodeReview;
import de.mindscan.futuresqr.domain.model.FSqrCodeReviewFactory;
import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;
import de.mindscan.futuresqr.domain.repository.FSqrCodeReviewRepository;
import de.mindscan.futuresqr.domain.repository.FSqrScmProjectConfigurationRepository;
import de.mindscan.futuresqr.domain.repository.FSqrScmUserRepository;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheCodeReviewTableImpl;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheRevisionToCodeReviewIdTableImpl;

/**
 * TODO: rework the repository to use a database instead of the in-memory only implementation
 */
public class FSqrCodeReviewRepositoryImpl implements FSqrCodeReviewRepository, ApplicationServicesSetter {

    private FSqrApplicationServices applicationServices;

    // search key: ( projectId:string , reviewId:string ) -> codereview:FSqrCodeReview
    private InMemoryCacheCodeReviewTableImpl codeReviewTableCache;
    // the database access...
    private FSqrCodeReviewTable codeReviewTable;

    // search key: ( projectId:string , revisionId:string ) -> CodeReviewId:string
    private InMemoryCacheRevisionToCodeReviewIdTableImpl codeReviewIdTableCache;
    // database access for the Project x revision -> codeReviewId
    private FSqrProjectAssignedCodeReviewsTable assignedCodeReviewsTable;

    /**
     * 
     */
    public FSqrCodeReviewRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();
        this.codeReviewTableCache = new InMemoryCacheCodeReviewTableImpl();
        this.codeReviewTable = new FSqrCodeReviewTableImpl();
        this.codeReviewIdTableCache = new InMemoryCacheRevisionToCodeReviewIdTableImpl();
        this.assignedCodeReviewsTable = new FSqrProjectAssignedCodeReviewsTableImpl();
    }

    @Override
    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;
        this.codeReviewTable.setDatbaseConnection( services.getDatabaseConnection() );
    }

    @Override
    public FSqrCodeReview getReview( String projectId, String reviewId ) {
        return this.codeReviewTableCache.getCodeReviewOrComputeIfAbsent( projectId, reviewId, codeReviewTable::selectCodeReview );
    }

    @Override
    public boolean hasReviewForProjectAndRevision( String projectid, String revisionid ) {
        return !this.codeReviewIdTableCache.getCodeReviewIdOrComputeIfAbsent( projectid, revisionid, assignedCodeReviewsTable::selectCodeReviewId ).isEmpty();
    }

    @Override
    public FSqrCodeReview getReviewForProjectAndRevision( String projectid, String revisionid ) {
        String reviewId = this.getReviewIdForProjectAndRevision( projectid, revisionid );

        if (reviewId.isEmpty()) {
            return null;
        }

        return this.getReview( projectid, reviewId );
    }

    private String getReviewIdForProjectAndRevision( String projectid, String revisionid ) {
        return this.codeReviewIdTableCache.getCodeReviewIdOrComputeIfAbsent( projectid, revisionid, assignedCodeReviewsTable::selectCodeReviewId );
    }

    @Override
    public List<FSqrCodeReview> selectOpenReviews( String projectId ) {
        // TODO: is it a good idea to filter on the in memory database?
        // for the open reviews it should be done from database (restart scenario?)
        // for now: good enough
        List<FSqrCodeReview> resultList = codeReviewTableCache.filterCodeReviewsByProject( projectId, FSqrCodeReview::isOpenCodeReview );

        Comparator<FSqrCodeReview> comparing = Comparator.comparing( FSqrCodeReview::getReviewId );
        resultList.sort( comparing );

        return resultList;
    }

    @Override
    public List<FSqrCodeReview> selectRecentlyClosedReviews( String projectId ) {
        // TODO is it a good idea to filter on the in memory database?
        // i guess for closed reviews it should be good enough.
        // for now: good enough        
        List<FSqrCodeReview> resultList = codeReviewTableCache.filterCodeReviewsByProject( projectId, FSqrCodeReview::isClosedCodeReview );

        Comparator<FSqrCodeReview> comparing = Comparator.comparing( FSqrCodeReview::getReviewId );
        resultList.sort( comparing );

        return resultList;
    }

    @Override
    public void closeReview( String projectId, String reviewId, String whoClosedUUID ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            codeReview.closeReview( whoClosedUUID );
            codeReviewTable.updateCodeReview( codeReview );
        }
    }

    @Override
    public void reopenReview( String projectId, String reviewId, String whoReopenedUUID ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            codeReview.reopenReview( whoReopenedUUID );
            codeReviewTable.updateCodeReview( codeReview );
        }
    }

    @Override
    public void deleteReview( String projectId, String reviewId, String whoDeletedUUID ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            // TODO NEXT: cleanup/unregister all referenced revisions, such they can again be reviewed 
            // projectIdRevisionIdToCodeReviewIdRepository
            codeReview.deleteReview( whoDeletedUUID );
            codeReviewTable.updateCodeReview( codeReview );
        }
    }

    @Override
    public void addRevisionToReview( String projectId, String reviewId, String revisionId, String userId ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            // we need to retrieve this revison ... THEN ADD IT
            // TODO: actually it is more complicated, we need to know where to add it... 
            // TODO: get history firstrevision to revisionid, and count number of elements in list of code review -> that is the insert position. 
            FSqrRevision revisionToAdd = applicationServices.getRevisionRepository().getSimpleRevisionInformation( projectId, revisionId );
            codeReview.addRevision( revisionToAdd );

            // add revision also to (project x revision) table - to associate revision with review. 
            this.codeReviewIdTableCache.putCodeReviewId( projectId, revisionId, reviewId );

            // TODO update assignedCodeReviewsTable
        }
    }

    @Override
    public void removeRevisionFromReview( String projectId, String reviewId, String revisionId, String userId ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            // FSqrRevision revisionToRemove = applicationServices.getRevisionRepository().getSimpleRevisionInformation( projectId, revisionId );

            // remove revision from review
            codeReview.removeRevisionById( revisionId );

            // remove revision from (project x revision) table - to mark it free again.
            this.codeReviewIdTableCache.removeCodeReviewId( projectId, revisionId );

            // TODO update assignedCodeReviewsTable
        }
    }

    @Override
    public FSqrCodeReview createReviewFromRevision( String projectid, String revisionid, String userid ) {
        FSqrScmProjectConfigurationRepository configurationRepository = applicationServices.getConfigurationRepository();

        FSqrRevision revision = applicationServices.getRevisionRepository().getSimpleRevisionInformation( projectid, revisionid );
        FSqrCodeReview codeReview = FSqrCodeReviewFactory.createReviewFromRevision( projectid, revision, configurationRepository );

        // store this in a local code review repository - we ay not like this right now - but this is good enough for now
        insertReview( projectid, codeReview );

        return codeReview;
    }

    public void insertReview( String projectId, FSqrCodeReview review ) {
        this.codeReviewTableCache.putCodeReview( projectId, review.getReviewId(), review );
        this.codeReviewTable.insertNewCodeReview( review );

        this.codeReviewIdTableCache.putCodeReviewId( projectId, review.getFirstRevisionId(), review.getReviewId() );
        // TODO: assignedCodeReviewsTable
    }

    @Override
    public List<FSqrRevision> getRevisionsForReview( String projectId, String reviewId ) {
        // get From assignedCodeReviewsTable?
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        return codeReview.getRevisions();
    }

    @Override
    public List<FSqrSystemUser> getSuggestedReviewers( String projectId, String reviewId ) {
        FSqrScmUserRepository userRepository = this.applicationServices.getUserRepository();

        // TODO: calculate the suggested reviewers somehow.

        // userlist, who edited these files filtered fr visibility?
        // or just users in the group, which can access a certain repo, regardless of their contributions...

        List<String> suggestedReviewers = new ArrayList<>();
        suggestedReviewers.add( "8ce74ee9-48ff-3dde-b678-58a632887e31" );
        suggestedReviewers.add( "f5fc8449-3049-3498-9f6b-ce828515bba2" );

        List<FSqrSystemUser> converted = new ArrayList<>();
        suggestedReviewers.stream() //
                        .filter( uuid -> userRepository.isUserUUIDPresent( uuid ) )//
                        .forEach( uuid -> converted.add( userRepository.getUserByUUID( uuid ) ) );

        return converted;
    }

    @Override
    public void addReviewerToCodeReview( String projectId, String reviewId, String reviewerId, String whoAddedId ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            codeReview.addReviewer( reviewerId, whoAddedId );
            this.codeReviewTable.updateCodeReview( codeReview );
        }
    }

    @Override
    public void removeReviewerFromCodeReview( String projectId, String reviewId, String reviewerId, String whoRemovedId ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            codeReview.removeReviewer( reviewerId, whoRemovedId );
            this.codeReviewTable.updateCodeReview( codeReview );
        }
    }

    @Override
    public void approveCodeReview( String projectId, String reviewId, String reviewerId ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            codeReview.approveReview( reviewerId );
            this.codeReviewTable.updateCodeReview( codeReview );
        }
    }

    @Override
    public void concernCodeReview( String projectId, String reviewId, String reviewerId ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            codeReview.concernReview( reviewerId );
            this.codeReviewTable.updateCodeReview( codeReview );
        }
    }

    @Override
    public void retractCodeReview( String projectId, String reviewId, String reviewerId ) {
        FSqrCodeReview codeReview = getReview( projectId, reviewId );
        if (codeReview != null) {
            codeReview.rollbackReview( reviewerId );
            codeReviewTable.updateCodeReview( codeReview );
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void reinitDatabaseTables() {
        this.codeReviewTable.createTable();
    }

}
