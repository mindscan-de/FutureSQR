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
package de.mindscan.futuresqr.scmaccess.git;

import de.mindscan.futuresqr.scmaccess.ScmConfigurationProvider;
import de.mindscan.futuresqr.scmaccess.ScmRepositoryServicesProvider;
import de.mindscan.futuresqr.scmaccess.git.command.GitCommands;
import de.mindscan.futuresqr.scmaccess.git.processor.GitOutputProcessors;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;
import de.mindscan.futuresqr.scmaccess.types.ScmUpdateResult;

/**
 * 
 */
public class GitScmRepositoryServicesProvider implements ScmRepositoryServicesProvider {

    private GitCLICommandExecutor gitCliExecutor;

    /**
     * @param configProvider
     */
    public GitScmRepositoryServicesProvider( ScmConfigurationProvider configProvider ) {
        this.gitCliExecutor = new GitCLICommandExecutor( configProvider );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmUpdateResult updateProjectCache( ScmRepository repository, String branchName ) {
        ScmUpdateResult result = gitCliExecutor // 
                        .execute( repository, GitCommands.createUpdateLocalRepositoryCommand( branchName ) ) //
                        .transform( GitOutputProcessors.toScmUpdateResult() );
        return result;
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

}
