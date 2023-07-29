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
import de.mindscan.futuresqr.domain.databases.FSqrScmRevisionsTable;
import de.mindscan.futuresqr.domain.databases.impl.FSqrScmRevisionsTableImpl;
import de.mindscan.futuresqr.domain.incubator.UnifiedDiffCalculationV1;
import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.FSqrRevisionFileChangeList;
import de.mindscan.futuresqr.domain.model.FSqrScmHistory;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectType;
import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;
import de.mindscan.futuresqr.domain.model.content.FSqrFileContentForRevision;
import de.mindscan.futuresqr.domain.model.history.FSqrFileHistory;
import de.mindscan.futuresqr.domain.model.m2m.ScmRepositoryFactory;
import de.mindscan.futuresqr.domain.repository.FSqrScmProjectRevisionRepository;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheRevisionFileChangeListTable;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheRevisionFullChangeSetTable;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheSimpleRevisionInformationTable;
import de.mindscan.futuresqr.scmaccess.ScmAccessFactory;
import de.mindscan.futuresqr.scmaccess.ScmContentProvider;
import de.mindscan.futuresqr.scmaccess.ScmHistoryProvider;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContent;
import de.mindscan.futuresqr.scmaccess.types.ScmFileHistory;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmPath;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

/**
 * TODO: rework the repository to use a database instead of the in-memory + scm data pull implementation
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
 * [/ATTN]
 */
public class FSqrScmProjectRevisionRepositoryImpl implements FSqrScmProjectRevisionRepository {

    // TODO: we want to get rid of the direct SCM interaction in this repository, and retrieve most of the data
    //       either from in memory storage or from a persistence/database

    @Deprecated
    private ScmHistoryProvider gitHistoryProvider;
    @Deprecated
    private ScmContentProvider gitScmContentProvider;

    // Table and cache
    private FSqrScmRevisionsTable revisionInfoTable;
    private InMemoryCacheSimpleRevisionInformationTable revisionInfoCache;

    private InMemoryCacheRevisionFileChangeListTable fileChangeListCache;
    private InMemoryCacheRevisionFullChangeSetTable fullChangeSetCache;

    private FSqrApplicationServices applicationServices;

    public FSqrScmProjectRevisionRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();

        this.gitHistoryProvider = ScmAccessFactory.getEmptyHistoryProvider();
        this.gitScmContentProvider = ScmAccessFactory.getEmptyContentProvider();

        // search key: ( projectId:string , reviewId:string ) -> RevisionInfo
        this.revisionInfoCache = new InMemoryCacheSimpleRevisionInformationTable();
        this.revisionInfoTable = new FSqrScmRevisionsTableImpl();

        this.fileChangeListCache = new InMemoryCacheRevisionFileChangeListTable();
        // TODO SQL_Table.... access

        this.fullChangeSetCache = new InMemoryCacheRevisionFullChangeSetTable();
    }

    @Override
    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;

        this.gitHistoryProvider = ScmAccessFactory.getGitHistoryProvider( new FSqrScmConfigrationProvider( services.getSystemConfiguration() ) );
        this.gitScmContentProvider = ScmAccessFactory.getGitContentProvider( new FSqrScmConfigrationProvider( services.getSystemConfiguration() ) );
    }

    @Override
    public FSqrScmHistory getRecentRevisionHistory( String projectId ) {
        FSqrScmHistory recentRevisionHistory = retriveRecentRevisionHistoryFromDatabaseTable( projectId, 75 );

        if (recentRevisionHistory == null) {
            return new FSqrScmHistory();
        }

        return recentRevisionHistory;
    }

    @Override
    public FSqrScmHistory getRecentRevisionHistoryStartingFrom( String projectId, String fromRevision ) {
        FSqrScmHistory recentRevisionHistorySinceRevision = retrieveRecentRevisionsFromStartingRevisionFromDatabaseTable( projectId, fromRevision );

        if (recentRevisionHistorySinceRevision == null) {
            return new FSqrScmHistory();
        }

        return recentRevisionHistorySinceRevision;
    }

    @Override
    public FSqrRevision getSimpleRevisionInformation( String projectId, String revisionId ) {
        FSqrRevision revision = this.revisionInfoCache.getFSqrRevisionOrComputeIfAbsent( projectId, revisionId, this::retrieveSimpleRevisionFromDatabaseTable );

        if (revision == null) {
            return new FSqrRevision();
        }

        return revision;
    }

    @Override
    public FSqrRevisionFileChangeList getRevisionFileChangeList( String projectId, String revisionId ) {
        FSqrRevisionFileChangeList revisionFileChangeList = this.fileChangeListCache.getFSqrRevisionFileChangeListOrComputIfAbsent( projectId, revisionId,
                        this::retrieveRevisionFileChangeListFromDatabaseTable );

        if (revisionFileChangeList == null) {
            return new FSqrRevisionFileChangeList();
        }

        return revisionFileChangeList;
    }

    @Override
    public FSqrRevisionFileChangeList getAllRevisionsFileChangeList( String projectId, List<FSqrRevision> revisions ) {
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {

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

            List<FSqrRevisionFullChangeSet> revisionFullChangeset = getRevisionFullChangeSetList( projectId, firstRevisionId, lastRevisionId );
            List<String> revisionListIds = revisionList.stream().map( r -> r.getRevisionId() ).collect( Collectors.toList() );

            // squash connected diff into one
            return diffCalculator.squashDiffs( revisionFullChangeset, revisionListIds, revisionListIds );
        }

        String firstRevisionId = revisionList.get( 0 ).getRevisionId();
        String lastRevisionId = revisionList.get( revisionList.size() - 1 ).getRevisionId();

        List<FSqrRevisionFullChangeSet> revisionFullChangeset = getRevisionFullChangeSetList( projectId, firstRevisionId, lastRevisionId );
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
        FSqrRevisionFullChangeSet fullChangeSet = this.fullChangeSetCache.getFSqrRevisionFullChangeSetOrComputeIfAbsent( projectId, revisionId,
                        // TODO: actually we want to retrieve this from database
                        this::retrieveRevisionFullChangeSetFromScm );

        if (fullChangeSet == null) {
            return new FSqrRevisionFullChangeSet();
        }

        return fullChangeSet;

    }

    private List<FSqrRevisionFullChangeSet> getRevisionFullChangeSetList( String projectId, String firstRevisionId, String lastRevisionId ) {
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
        return applicationServices.getConfigurationRepository().getProjectConfiguration( projectId );
    }

    private ScmRepository toScmRepository( FSqrScmProjectConfiguration scmConfiguration ) {
        return ScmRepositoryFactory.toScmRepository( applicationServices.getSystemConfiguration(), scmConfiguration );
    }

    // =======================================================
    // --- Move this to the new database table implementations
    // =======================================================

    private FSqrRevision retrieveSimpleRevisionFromDatabaseTable( String projectId, String revisionId ) {
        FSqrRevision revisionInfo = this.revisionInfoTable.selectScmRevision( projectId, revisionId );

        if (revisionInfo != null) {
            return revisionInfo;
        }

        // FIXME later: only if not in the database, retrieve from SCM, then insert in database.
        // the insert operation should be part of the crawler mechanism, here it is just a kind of lazy indexing operation.
        // TODO: inserting stuff into the database is responsibility of the crawler.

        FSqrRevision revisionInfoFromScm = applicationServices.getScmRepositoryServices().getSimpleRevisionFromScm( projectId, revisionId );

        this.revisionInfoTable.insertScmRevision( projectId, revisionInfoFromScm );

        return revisionInfoFromScm;
    }

    private FSqrRevisionFileChangeList retrieveRevisionFileChangeListFromDatabaseTable( String projectId, String revisionId ) {
        // TODO refactor from scm retrieval to sql table retrieval.
        // TODO: only if not in the database, retrieve from SCM, then update the database, then update the cache.

        return applicationServices.getScmRepositoryServices().getRevisionFileChangeListFromScm( projectId, revisionId );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrRevision retrieveHeadRevision( String projectId, String projectBranch ) {
        // TODO later also support branches.... / maybe different call
        // HEAD ist im prinzip head auf default branch...
        // vielleicht sollte diese methode public als getter kommen, aber die interne private methode sollte retrieve lauten.... 
        FSqrRevision headRevision = this.revisionInfoTable.selectScmHeadRevision( projectId, projectBranch );

        return headRevision;
    }

    private FSqrScmHistory retriveRecentRevisionHistoryFromDatabaseTable( String projectId, int count ) {
        // TODO: this should be tested, whether it is already cached in-memory database, and if the maximum age is reached
        // TODO: retrieve the current values from database.
        // TODO: the crawler will put that info into the database.
        return applicationServices.getScmRepositoryServices().getRecentRevisionHistoryFromScm( projectId, count );
    }

    private FSqrScmHistory retrieveRecentRevisionsFromStartingRevisionFromDatabaseTable( String projectId, String fromRevision ) {
        // TODO: this should be tested, whether it is already cached in-memory database, and if the maximum age is reached
        // TODO: retrieve the current values from database.
        // TODO: the crawler will put that info into the database.

        return applicationServices.getScmRepositoryServices().getRecentRevisionHistoryStartingFrom( projectId, fromRevision );
    }

    // ================================================
    // ---- Move this to crawler and Database inserter.
    // ================================================    

    private FSqrRevisionFullChangeSet retrieveRevisionFullChangeSetFromScm( String projectId, String revisionId ) {
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );
            ScmFullChangeSet fullChangeSet = gitScmContentProvider.getFullChangeSetForRevision( scmRepository, revisionId );

            return new FSqrRevisionFullChangeSet( fullChangeSet );
        }

        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void reinitDatabaseTables() {
        // intentionally left blank
        // this.revisionInfoTable - doesn't need re-initialization right now. 

    }

}
