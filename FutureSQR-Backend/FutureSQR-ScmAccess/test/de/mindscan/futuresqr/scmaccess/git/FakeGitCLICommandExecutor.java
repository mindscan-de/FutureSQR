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

import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

/**
 * The Idea of this fake executor is to be able to record test cases and to replay the output. such that it can be
 * used to create stable tests and to provide tests without access to a repository, but still be able to execute 
 * the unit tests with the output which a real invocation would have produced. We actually do not want to spin
 * up System-Process-Git executions, like updates and such on a repository just for the sake of running unit tests.
 * 
 * the recorded data should be put into a resource directory for tests like "test-resources". 
 */
public class FakeGitCLICommandExecutor extends GitCLICommandExecutor {

    private boolean neverInvokeSuperOnExecute;

    /**
     * 
     */
    public FakeGitCLICommandExecutor( boolean neverInvokeSuperOnExecute ) {
        super();

        this.neverInvokeSuperOnExecute = neverInvokeSuperOnExecute;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public GitCLICommandOutput execute( ScmRepository repository, GitCommand command ) {
        // TODO we can either record the output and store it
        // or we can replay the output generated previously for the repository and the given command.
        // we can do both in case we encounter a new combination, which we don't know.

        GitCLICommandOutput recordedEntryData = super.execute( repository, command );

        return recordedEntryData;
    }
}
