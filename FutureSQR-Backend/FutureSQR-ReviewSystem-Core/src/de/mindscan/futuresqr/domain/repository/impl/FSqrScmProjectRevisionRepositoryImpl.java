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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.configuration.impl.FSqrScmConfigrationProvider;
import de.mindscan.futuresqr.domain.incubator.UnifiedDiffCalculationV1;
import de.mindscan.futuresqr.domain.model.FSqrCodeReview;
import de.mindscan.futuresqr.domain.model.FSqrCodeReviewLifecycleState;
import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.FSqrRevisionFileChangeList;
import de.mindscan.futuresqr.domain.model.FSqrScmHistory;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectType;
import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;
import de.mindscan.futuresqr.domain.model.content.FSqrFileContentForRevision;
import de.mindscan.futuresqr.domain.model.history.FSqrFileHistory;
import de.mindscan.futuresqr.domain.repository.FSqrScmProjectRevisionRepository;
import de.mindscan.futuresqr.scmaccess.ScmAccessFactory;
import de.mindscan.futuresqr.scmaccess.ScmContentProvider;
import de.mindscan.futuresqr.scmaccess.ScmHistoryProvider;
import de.mindscan.futuresqr.scmaccess.ScmRepositoryServicesProvider;
import de.mindscan.futuresqr.scmaccess.types.ScmBasicRevisionInformation;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContent;
import de.mindscan.futuresqr.scmaccess.types.ScmFileHistory;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmHistory;
import de.mindscan.futuresqr.scmaccess.types.ScmPath;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;
import de.mindscan.futuresqr.scmaccess.types.ScmSingleRevisionFileChangeList;

/**
 * TODO: rework the repository to use a database instead of the in-memory + scm data pull implementation
 */
public class FSqrScmProjectRevisionRepositoryImpl implements FSqrScmProjectRevisionRepository {

    // TODO: we want to get rid of the direct SCM interaction in this repository, and retrieve most of the data
    //       either from in memory storage or from a persistence/database

    private ScmHistoryProvider gitHistoryProvider;
    private ScmContentProvider gitScmContentProvider;
    private ScmRepositoryServicesProvider gitScmRepositoryServicesProvider;

    private FSqrApplicationServices applicationServices;

    public FSqrScmProjectRevisionRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();

        this.gitHistoryProvider = ScmAccessFactory.getEmptyHistoryProvider();
        this.gitScmContentProvider = ScmAccessFactory.getEmptyContentProvider();
        this.gitScmRepositoryServicesProvider = ScmAccessFactory.getEmptyRepositoryServicesProvider();
    }

    @Override
    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;

        this.gitHistoryProvider = ScmAccessFactory.getGitHistoryProvider( new FSqrScmConfigrationProvider( services.getSystemConfiguration() ) );
        this.gitScmContentProvider = ScmAccessFactory.getGitContentProvider( new FSqrScmConfigrationProvider( services.getSystemConfiguration() ) );
        this.gitScmRepositoryServicesProvider = ScmAccessFactory
                        .getGitRepositoryServicesProvider( new FSqrScmConfigrationProvider( services.getSystemConfiguration() ) );
    }

    @Override
    public FSqrScmHistory getRecentRevisionHistory( String projectId ) {
        // TODO: this should be tested, whether it is already cached in-memory database, and if the maximum age is reached
        // TODO: retrieve the current values from database.
        // TODO: the crawler will put that info into the database.

        // -----------------------------------------
        // TODO: refactor this to Database retrieval
        // -----------------------------------------
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {

            ScmHistory nRecentHistory = gitHistoryProvider.getNRecentRevisions( toScmRepository( scmConfiguration ), 75 );
            return translate( nRecentHistory, projectId );
        }

        FSqrScmHistory result = new FSqrScmHistory();

        return result;
    }

    @Override
    public FSqrScmHistory getRecentRevisionHistoryStartingFrom( String projectId, String fromRevision ) {
        // TODO: this should be tested, whether it is already cached in-memory database, and if the maximum age is reached
        // TODO: retrieve the current values from database.
        // TODO: the crawler will put that info into the database.

        // -----------------------------------------
        // TODO: refactor this to Database retrieval
        // -----------------------------------------
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmHistory nRecentHistory = gitHistoryProvider.getRecentRevisionsFromStartingRevision( toScmRepository( scmConfiguration ), fromRevision );
            return translate( nRecentHistory, projectId );
        }

        FSqrScmHistory result = new FSqrScmHistory();
        return result;
    }

    private FSqrScmHistory translate( ScmHistory nRecentHistory, String projectId ) {
        FSqrScmHistory result = new FSqrScmHistory();

        nRecentHistory.revisions.stream().forEach( x -> result.addRevision( translate( x, projectId ) ) );

        return result;
    }

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
    public FSqrRevision getSimpleRevisionInformation( String projectId, String revisionId ) {
        // TODO: this should be tested, whether it is already cached in-memory database.
        // TODO: if not, we should have an information, whether we can retrieve this from the Database
        // TODO: only if not in the database, retrieve from SCM, then update the database, then update the cache.

        // -----------------------------------------
        // TODO: refactor this to Database retrieval
        // -----------------------------------------
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );
            ScmHistory scmHistory = gitHistoryProvider.getSimpleRevisionInformation( scmRepository, revisionId );
            FSqrScmHistory result = translate( scmHistory, projectId );

            return result.getRevisions().get( 0 );
        }

        return new FSqrRevision();
    }

    @Override
    public FSqrRevisionFileChangeList getRevisionFileChangeList( String projectId, String revisionId ) {
        // TODO: this should be tested, whether it is already cached in-memory database.
        // TODO: if not, we should have an information, whether we can retrieve this from the Database
        // TODO: only if not in the database, retrieve from SCM, then update the database, then update the cache.

        // -----------------------------------------
        // TODO: refactor this to Database retrieval
        // -----------------------------------------
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );
            ScmSingleRevisionFileChangeList fileChangeList = gitHistoryProvider.getFileChangeListForRevision( scmRepository, revisionId );

            return translate( fileChangeList, projectId );
        }

        return new FSqrRevisionFileChangeList();
    }

    @Override
    public FSqrRevisionFileChangeList getAllRevisionsFileChangeList( String projectId, List<FSqrRevision> revisions ) {
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );

            ArrayList<FSqrRevision> revisionCopy = new ArrayList<>( revisions );
            // build list of lists for each revision
            List<FSqrRevisionFileChangeList> allchanges = revisionCopy.stream().map( r -> this.getRevisionFileChangeList( projectId, r.getRevisionId() ) )
                            .collect( Collectors.toList() );

            String firstRevisionId = revisionCopy.get( 0 ).getRevisionId();
            Set<String[]> rawComprehension = new TreeSet<>( new Comparator<String[]>() {

                @Override
                public int compare( String[] o1, String[] o2 ) {
                    if (o1 == null) {
                        return o2 == null ? 0 : 1;
                    }
                    if (o2 == null) {
                        return -1;
                    }

                    String o1String = String.join( "|", o1 );
                    String o2String = String.join( "|", o2 );

                    return o1String.compareTo( o2String );
                }
            } );

            for (FSqrRevisionFileChangeList fileChangeList : allchanges) {
                rawComprehension.addAll( fileChangeList.getFileChangeList() );
            }

            FSqrRevisionFileChangeList result = new FSqrRevisionFileChangeList( firstRevisionId, rawComprehension );
            return result;
        }
        return new FSqrRevisionFileChangeList();
    }

    private FSqrRevisionFileChangeList translate( ScmSingleRevisionFileChangeList fileChangeList, String projectId ) {
        return new FSqrRevisionFileChangeList( fileChangeList );
    }

    @Override
    public FSqrRevisionFullChangeSet getRevisionFullChangeSet( String projectId, List<FSqrRevision> revisionList ) {
        if (revisionList.size() == 0) {
            return new FSqrRevisionFullChangeSet();
        }

        if (revisionList.size() == 1) {
            return getRevisionFullChangeSet( projectId, revisionList.get( 0 ).getRevisionId() );
        }

        // TODO: refactor this later, combine connected and unconnected diffs, because the same logic

        // check if the revisions are all on one direct line, or split that lines up
        UnifiedDiffCalculationV1 diffCalculator = new UnifiedDiffCalculationV1();
        if (isLiningUp( revisionList )) {
            String firstRevisionId = revisionList.get( 0 ).getRevisionId();
            String lastRevisionId = revisionList.get( revisionList.size() - 1 ).getRevisionId();

            List<FSqrRevisionFullChangeSet> revisionFullChangeset = getRevisionFullChangeset( projectId, firstRevisionId, lastRevisionId );
            List<String> revisionListIds = revisionList.stream().map( r -> r.getRevisionId() ).collect( Collectors.toList() );

            // squash connected diff into one
            return diffCalculator.squashDiffs( revisionFullChangeset, revisionListIds, revisionListIds );
        }

        String firstRevisionId = revisionList.get( 0 ).getRevisionId();
        String lastRevisionId = revisionList.get( revisionList.size() - 1 ).getRevisionId();

        List<FSqrRevisionFullChangeSet> revisionFullChangeset = getRevisionFullChangeset( projectId, firstRevisionId, lastRevisionId );
        List<String> revisionListIds = revisionList.stream().map( r -> r.getRevisionId() ).collect( Collectors.toList() );

        // squash unconnected diff into one
        return diffCalculator.squashDiffs( revisionFullChangeset, revisionListIds, revisionListIds );
    }

    private boolean isLiningUp( List<FSqrRevision> revisionList ) {
        for (int i = revisionList.size() - 1; i > 0; i--) {
            FSqrRevision currentRevision = revisionList.get( i );
            FSqrRevision previousRevision = revisionList.get( i - 1 );

            // check if previousElement.revisionId is in currentElement.parent : if not return false -> they don't line up.
            if (!currentRevision.getParentIds().contains( previousRevision.getRevisionId() )) {
                return false;
            }
        }
        return true;
    }

    @Override
    public FSqrRevisionFullChangeSet getRevisionFullChangeSet( String projectId, String revisionId ) {

        // -----------------------------------------
        // TODO: refactor this to Database retrieval
        // -----------------------------------------
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );
            ScmFullChangeSet fullChangeSet = gitScmContentProvider.getFullChangeSetForRevision( scmRepository, revisionId );

            return new FSqrRevisionFullChangeSet( fullChangeSet );
        }
        return new FSqrRevisionFullChangeSet();
    }

    private List<FSqrRevisionFullChangeSet> getRevisionFullChangeset( String projectId, String firstRevisionId, String lastRevisionId ) {
        List<FSqrRevisionFullChangeSet> result = new ArrayList<>();

        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );

            List<ScmFullChangeSet> fullChangeSet = gitScmContentProvider.getFullChangeSetFromRevisionToRevision( scmRepository, firstRevisionId,
                            lastRevisionId );

            fullChangeSet.stream().forEach( changeSet -> result.add( new FSqrRevisionFullChangeSet( changeSet ) ) );
            return result;
        }
        return result;
    }

    @Override
    public FSqrFileContentForRevision getFileContentForRevision( String projectId, String revisionId, String filePath ) {

        // -----------------------------------------
        // TODO: refactor this to Database retrieval
        // -----------------------------------------
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );
            ScmFileContent fileContent = gitScmContentProvider.getFileContentForRevision( scmRepository, revisionId, new ScmPath( filePath ) );

            return new FSqrFileContentForRevision( fileContent );
        }

        return new FSqrFileContentForRevision();
    }

    @Override
    public FSqrFileHistory getParticularFileHistory( String projectId, String revisionId, String filePath ) {
        // TODO: test in in-memory database
        // TODO: get this from databse
        // TODO: use the crawler to put file history into the database

        // -----------------------------------------
        // TODO: refactor this to Database retrieval
        // -----------------------------------------
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );
            ScmFileHistory filePathHistory = gitHistoryProvider.getFilePathHistory( scmRepository, new ScmPath( filePath ) );

            // TODO: actually add the reviews for each revision, here from somewhere else.
            // TODO: also add author info.
            // TODO: actually we currently want to make things just run, let's see how far we get.

            return new FSqrFileHistory( filePathHistory );
        }

        return new FSqrFileHistory();
    }

    private FSqrScmProjectConfiguration toScmConfiguration( String projectId ) {
        // this is already cached...
        return applicationServices.getConfigurationRepository().getProjectConfiguration( projectId );
    }

    private ScmRepository toScmRepository( FSqrScmProjectConfiguration scmConfiguration ) {

        // TDOO: 
        // - we need A factory, which can translate some FSqrScmProjectConfiguration to a useful ScmRepository Object
        // - we should not do this here....
        // depending on the type, we might want to distinguish between different needs according to the repository type.

        String repoCachePath = applicationServices.getSystemConfiguration().getSystemRepoCachePath();

        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            if (scmConfiguration.hasLocalRepoPath()) {
                // not yet nice but better than before.
                return new ScmRepository( repoCachePath + scmConfiguration.getScmGitAdminConfiguration().localPath );
            }
        }

        // TODO: this has to be fixed soon...
        ScmRepository result = new ScmRepository( repoCachePath + "FutureSQR" );

        return result;
    }

}
