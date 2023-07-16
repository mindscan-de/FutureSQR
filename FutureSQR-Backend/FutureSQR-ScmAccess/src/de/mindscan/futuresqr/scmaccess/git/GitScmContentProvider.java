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

import java.util.List;

import de.mindscan.futuresqr.scmaccess.ScmConfigurationProvider;
import de.mindscan.futuresqr.scmaccess.ScmContentProvider;
import de.mindscan.futuresqr.scmaccess.git.command.GitCommands;
import de.mindscan.futuresqr.scmaccess.git.processor.GitOutputProcessors;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContent;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmPath;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

/**
 * 
 */
public class GitScmContentProvider implements ScmContentProvider {

    private GitCLICommandExecutor gitCliExecutor;

    /**
     * 
     */
    public GitScmContentProvider( ScmConfigurationProvider configProvider ) {
        this.gitCliExecutor = new GitCLICommandExecutor( configProvider );
    }

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
    public ScmFileContent getFileContentForRevision( ScmRepository repository, String revisionId, ScmPath filePath ) {
        ScmFileContent scmFileContent = gitCliExecutor // 
                        .execute( repository, GitCommands.createGetFileContentForRevisionCommand( revisionId, filePath ) )
                        .transform( GitOutputProcessors.toScmFileContent() );

        return scmFileContent;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmFullChangeSet getFullChangeSetForRevision( ScmRepository repository, String revisionId ) {
        ScmFullChangeSet fullChangeSet = gitCliExecutor //
                        .execute( repository, GitCommands.createGetDiffForRevisionCommand( revisionId ) )// 
                        .transform( GitOutputProcessors.toScmFullChangeSet() );

        return fullChangeSet;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmFullChangeSet getHeadFullChangeSet( ScmRepository repository ) {
        ScmFullChangeSet fullChangeSet = gitCliExecutor //
                        .execute( repository, GitCommands.createGetHeadChangeSet() )//
                        // TODO maybe implement a map function and the transform function last.
                        .transform( GitOutputProcessors.toScmFullChangeSet() );
        // TODO:
        // basically we want a ScmHistory with one entry, because we don't need the top diff...
        // let's see ....
        // if we have ScmHistory then this content provider is actually the wrong place.

        return fullChangeSet;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public List<ScmFullChangeSet> getFullChangeSetFromRevisionToRevision( ScmRepository repository, String firstRevisionId, String lastRevisionId ) {
        List<ScmFullChangeSet> fullChangeSet = gitCliExecutor //
                        .execute( repository, GitCommands.createGetDiffBetweenRevisionsCommand( firstRevisionId, lastRevisionId ) )//
                        .transform( GitOutputProcessors.toScmFullChangeSetList() );

        return fullChangeSet;
    }

}
