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
package de.mindscan.futuresqr.scmaccess.git.command;

import java.nio.file.Paths;

import de.mindscan.futuresqr.scmaccess.git.GitCommand;
import de.mindscan.futuresqr.scmaccess.git.command.impl.CloneToLocalRepositoryCommand;
import de.mindscan.futuresqr.scmaccess.git.command.impl.GetAllLocalRemoteBranchesCommand;
import de.mindscan.futuresqr.scmaccess.git.command.impl.GetDiffBetweenRevisionsCommand;
import de.mindscan.futuresqr.scmaccess.git.command.impl.GetDiffForRevisionCommand;
import de.mindscan.futuresqr.scmaccess.git.command.impl.GetFileChangesListForRevisionCommand;
import de.mindscan.futuresqr.scmaccess.git.command.impl.GetFileContentForRevisionCommand;
import de.mindscan.futuresqr.scmaccess.git.command.impl.GetFileHistoryCommand;
import de.mindscan.futuresqr.scmaccess.git.command.impl.GetNRecentRevisionsCommand;
import de.mindscan.futuresqr.scmaccess.git.command.impl.GetRecentRevisionsCommand;
import de.mindscan.futuresqr.scmaccess.git.command.impl.GetRecentRevisionsCommandFromParticularRevision;
import de.mindscan.futuresqr.scmaccess.git.command.impl.GetSimpleCommitInformationForRevisionCommand;
import de.mindscan.futuresqr.scmaccess.git.command.impl.UpdateLocalRepositoryCommand;
import de.mindscan.futuresqr.scmaccess.types.ScmPath;

/**
 * 
 */
public class GitCommands {

    public static GitCommand createGetFileContentForRevisionCommand( String revisionId, ScmPath filePath ) {
        return new GetFileContentForRevisionCommand( revisionId, filePath.getPath() );
    }

    public static GitCommand createGetFileHistoryCommand( ScmPath filePath ) {
        return new GetFileHistoryCommand( filePath.getPath() );
    }

    public static GitCommand createUpdateLocalRepositoryCommand( String branchName ) {
        return new UpdateLocalRepositoryCommand( branchName );
    }

    public static GitCommand createGetRecentRevisionsCommand() {
        return new GetRecentRevisionsCommand();
    }

    public static GitCommand createGetNRecentRevisionsCommand( int limit ) {
        return new GetNRecentRevisionsCommand( limit );
    }

    public static GitCommand createGetRecentRevisionsCommandFromParticularRevision( String fromRevision ) {
        return new GetRecentRevisionsCommandFromParticularRevision( fromRevision );
    }

    public static GitCommand createGetFileChangesListForRevisionCommand( String revisionId ) {
        return new GetFileChangesListForRevisionCommand( revisionId );
    }

    public static GitCommand createGetSimpleCommitInfoForRevisionCommand( String revisionId ) {
        return new GetSimpleCommitInformationForRevisionCommand( revisionId );
    }

    public static GitCommand createGetDiffForRevisionCommand( String revisionId ) {
        return new GetDiffForRevisionCommand( revisionId );
    }

    public static GitCommand createGetDiffBetweenRevisionsCommand( String startRevisionId, String endRevisionId ) {
        return new GetDiffBetweenRevisionsCommand( startRevisionId, endRevisionId );
    }

    public static GitCommand createGetAllLocalRemoteBranchesCommand() {
        return new GetAllLocalRemoteBranchesCommand();
    }

    public static GitCommand createCloneToRepoCacheCommand( String systemRepositoryCache, String projectId, String gitCloneURL ) {
        return new CloneToLocalRepositoryCommand( Paths.get( systemRepositoryCache ), projectId, gitCloneURL );
    }
}
