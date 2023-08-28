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
package de.mindscan.futuresqr.domain.repository.impl;

import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.configuration.impl.FSqrScmConfigrationProvider;
import de.mindscan.futuresqr.domain.model.FSqrCodeReview;
import de.mindscan.futuresqr.domain.model.FSqrCodeReviewLifecycleState;
import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.FSqrRevisionFileChangeList;
import de.mindscan.futuresqr.domain.model.FSqrScmHistory;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectType;
import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;
import de.mindscan.futuresqr.domain.model.m2m.ScmRepositoryFactory;
import de.mindscan.futuresqr.domain.repository.FSqrScmRepositoryServices;
import de.mindscan.futuresqr.scmaccess.ScmAccessFactory;
import de.mindscan.futuresqr.scmaccess.ScmContentProvider;
import de.mindscan.futuresqr.scmaccess.ScmHistoryProvider;
import de.mindscan.futuresqr.scmaccess.types.ScmBasicRevisionInformation;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmHistory;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;
import de.mindscan.futuresqr.scmaccess.types.ScmSingleRevisionFileChangeList;

/**
 * This content of this repository will be moved. And then this repository is 
 * re-implemented using the event mechanism, which is developed for the crawler. 
 * 
 * [ATTN]
 * 
 * Actually this repository implementation should coordinate a cache and the sqldatabase.
 *  
 * Problem right now is that we don't have a database table for this data. So the next
 * step is to extract a crawler an insert mechanism and on the other side a retrieval 
 * and a cache mechanism on the read side for this FSqrScmProjectRevisionRepositoryImpl.
 * 
 * So basically we need to implement a database table and a read cache first, where we store
 * retrieved data. And then we split this implementation and refactor it to a crawler, where
 * we can have a job queue for the crawler which will fill the database. the crawler will be 
 * a separate thread and observe the scm repositories.
 * 
 * This may be deferred to some later time, but this will be not a trivial part. The Update/
 * Synchronize process (update cache in the ui.) can be used to substitute the crawler and
 * extra thread.
 * 
 * {@link #updateProjectCache(String)} must insert the data into the database tables. For 
 * now a job queue is directly executed. Dequeue (atomic?). The Job executor takes over the 
 * insertion part. Also the request to update the project cache is something which a JobExecutor
 * must do.
 * 
 * [/ATTN]
 *
 */
public class FSqrScmRepositoryServicesImpl implements FSqrScmRepositoryServices {

    private FSqrApplicationServices applicationServices;
    private ScmContentProvider gitScmContentProvider;
    private ScmHistoryProvider gitScmHistoryProvider;

    /**
     * 
     */
    public FSqrScmRepositoryServicesImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();

        this.gitScmContentProvider = ScmAccessFactory.getEmptyContentProvider();
        this.gitScmHistoryProvider = ScmAccessFactory.getEmptyHistoryProvider();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;

        this.gitScmContentProvider = ScmAccessFactory.getGitContentProvider( new FSqrScmConfigrationProvider( services.getSystemConfiguration() ) );
        this.gitScmHistoryProvider = ScmAccessFactory.getGitHistoryProvider( new FSqrScmConfigrationProvider( services.getSystemConfiguration() ) );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void updateProjectCache( String projectId ) {
        // TODO: This is just a helper as long as we have no crawler, which retrieves the data from the SCM and puts 
        //       these information into a database.

        // TODO: either schedule the task directly, or request via event dispatcher.
        // taskscheduler.schedule(new UpdateProjectCacheTask(projectId));
        // eventdispatcher.dispatch(new UpdateProjectCacheRequestedEvent(projectId));
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrRevisionFullChangeSet getHeadRevisionFullChangeSetFromScm( String projectId ) {
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );

            ScmFullChangeSet fullChangeSet = gitScmContentProvider.getHeadFullChangeSet( scmRepository );

            return new FSqrRevisionFullChangeSet( fullChangeSet );
        }

        return null;
    }

    private FSqrScmProjectConfiguration toScmConfiguration( String projectId ) {
        return applicationServices.getConfigurationRepository().getProjectConfiguration( projectId );
    }

    private ScmRepository toScmRepository( FSqrScmProjectConfiguration scmConfiguration ) {
        return ScmRepositoryFactory.toScmRepository( applicationServices.getSystemConfiguration(), scmConfiguration );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void cloneCheckoutToProjectCache( String projectId ) {
        // TODO Auto-generated method stub
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmHistory getRecentRevisionHistoryStartingFrom( String projectId, String fromRevision ) {
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmHistory nRecentHistory = gitScmHistoryProvider.getRecentRevisionsFromStartingRevision( toScmRepository( scmConfiguration ), fromRevision );
            return translate( nRecentHistory, projectId );
        }

        return null;
    }

    private FSqrScmHistory translate( ScmHistory nRecentHistory, String projectId ) {
        FSqrScmHistory result = new FSqrScmHistory();

        nRecentHistory.revisions.stream().forEach( x -> result.addRevision( translate( x, projectId ) ) );

        return result;
    }

    // TODO: 
    // this does queries, on the revision info and adds review infos about the revision - this
    // calculation can not be cached, because the code review state may change for some time. 
    private FSqrRevision translate( ScmBasicRevisionInformation x, String projectId ) {
        FSqrRevision result = new FSqrRevision( x );

        String authorUUID = applicationServices.getUserRepository().getUserUUID( x.authorName );
        result.setAuthorUuid( authorUUID );

        // calculate whether a review is known for this 
        if (applicationServices.getReviewRepository().hasReviewForProjectAndRevision( projectId, x.revisionId )) {
            result.setHasAttachedReview( true );
            FSqrCodeReview review = applicationServices.getReviewRepository().getReviewForProjectAndRevision( projectId, x.revisionId );
            result.setReviewId( review.getReviewId() );
            result.setReviewClosed( review.getCurrentReviewState() == FSqrCodeReviewLifecycleState.Closed );
        }

        return result;
    }

    @Override
    public FSqrRevisionFileChangeList getRevisionFileChangeListFromScm( String projectId, String revisionId ) {
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );
            ScmSingleRevisionFileChangeList fileChangeList = gitScmHistoryProvider.getFileChangeListForRevision( scmRepository, revisionId );

            return translate( fileChangeList, projectId );
        }

        return null;
    }

    private FSqrRevisionFileChangeList translate( ScmSingleRevisionFileChangeList fileChangeList, String projectId ) {
        return new FSqrRevisionFileChangeList( fileChangeList );
    }

    @Override
    public FSqrRevision getSimpleRevisionFromScm( String projectId, String revisionId ) {
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );
            ScmHistory scmHistory = gitScmHistoryProvider.getSimpleRevisionInformation( scmRepository, revisionId );
            FSqrScmHistory result = translate( scmHistory, projectId );

            return result.getRevisions().get( 0 );
        }
        return null;
    }

    @Override
    public FSqrScmHistory getRecentRevisionHistoryFromScm( String projectId, int count ) {
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmHistory nRecentHistory = gitScmHistoryProvider.getNRecentRevisions( toScmRepository( scmConfiguration ), count );

            return translate( nRecentHistory, projectId );
        }

        return null;
    }

}
