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
package de.mindscan.futuresqr.scmaccess.git;

import de.mindscan.futuresqr.scmaccess.HardCodedConstants;
import de.mindscan.futuresqr.scmaccess.ScmHistoryProvider;
import de.mindscan.futuresqr.scmaccess.git.command.GitCommands;
import de.mindscan.futuresqr.scmaccess.git.processor.GitOutputProcessors;
import de.mindscan.futuresqr.scmaccess.types.ScmFileHistory;
import de.mindscan.futuresqr.scmaccess.types.ScmHistory;
import de.mindscan.futuresqr.scmaccess.types.ScmPath;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;
import de.mindscan.futuresqr.scmaccess.types.ScmSingleRevisionFileChangeList;

/**
 * 
 */
public class GitScmHistoryProvider implements ScmHistoryProvider {

    private GitCLICommandExecutor gitCliExecutor = new GitCLICommandExecutor( HardCodedConstants.GIT_EXECUTABLE_PATH );

    /**
     * This method is available to replace the default GitCLUCommandExecutor with a different one, e.g. for tests
     * or with other configurations later.
     * 
     * @param alternateExecutor
     */
    void setGitCLICommandExecutor( GitCLICommandExecutor alternateExecutor ) {
        this.gitCliExecutor = alternateExecutor;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmFileHistory getFilePathHistory( ScmRepository repository, ScmPath filePath ) {
        ScmFileHistory scmFileHistory = gitCliExecutor//
                        .execute( repository, GitCommands.createGetFileHistoryCommand( filePath ) )//
                        .transform( GitOutputProcessors.toScmFileHistory() );

        return scmFileHistory;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmHistory getRecentRevisions( ScmRepository repository ) {
        ScmHistory scmHistory = gitCliExecutor //
                        .execute( repository, GitCommands.createGetRecentRevisionsCommand() ) //
                        .transform( GitOutputProcessors.toScmHistory() );

        return scmHistory;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmHistory getNRecentRevisions( ScmRepository repository, int limit ) {
        ScmHistory scmHistory = gitCliExecutor //
                        .execute( repository, GitCommands.createGetNRecentRevisionsCommand( limit ) ) //
                        .transform( GitOutputProcessors.toScmHistory() );

        return scmHistory;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmHistory getRecentRevisionsFromStartingRevision( ScmRepository repository, String fromRevision ) {
        ScmHistory scmHistory = gitCliExecutor //
                        .execute( repository, GitCommands.createGetRecentRevisionsCommandFromParticularRevision( fromRevision ) ) //
                        .transform( GitOutputProcessors.toScmHistory() );

        return scmHistory;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmSingleRevisionFileChangeList getFileChangeListForRevision( ScmRepository repository, String revisionId ) {
        ScmSingleRevisionFileChangeList scmFileChangesList = gitCliExecutor// 
                        .execute( repository, GitCommands.createGetFileChangesListForRevisionCommand( revisionId ) ) //
                        .transform( GitOutputProcessors.toScmSingleRevisionFileChangeList() );

        return scmFileChangesList;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmHistory getSimpleRevisionInformation( ScmRepository repository, String revisionId ) {
        ScmHistory scmHistory = gitCliExecutor //
                        .execute( repository, GitCommands.createGetSimpleCommitInfoForRevisionCommand( revisionId ) ) //
                        .transform( GitOutputProcessors.toScmHistory() );

        return scmHistory;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void updateProjectCache( ScmRepository repository, String branchName ) {
        @SuppressWarnings( "unused" )
        Void scmVoid = gitCliExecutor // 
                        .execute( repository, GitCommands.createUpdateLocalRepositoryCommand( branchName ) ) //
                        .transform( GitOutputProcessors.toScmVoid() );
    }

}
