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
 * 
 * this Fake executor is injectable into the test process, e.g. by initializing the providers with this executor.
 * 
 * This same mechanism implemented here can be used to implement a fast in-memory-cache for accessing the repository, 
 * where different cache strategies can be adopted. Also the cache mechanism can be used to combine it with this 
 * FakeGitCLICommandExecutor, to speed up tests with a cache.
 *  
 */
public class FakeGitCLICommandExecutor extends GitCLICommandExecutor {

    private boolean neverInvokeSuperOnExecute;

    /**
     * 
     */
    public FakeGitCLICommandExecutor() {
        this( true );
    }

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
        if (isRecordingPresent( repository, command )) {
            GitCLICommandOutput result = new GitCLICommandOutput( repository, command );

            result.setProcessOutput( buildCliCommandProcessOutputFromRecording( repository, command ) );

            return result;
        }

        if (neverInvokeSuperOnExecute) {
            throw new RuntimeException( "There is no recording for this repository and command available." );
        }

        // TODO we can either record the output and store it
        // or we can replay the output generated previously for the repository and the given command.
        // we can do both in case we encounter a new combination, which we don't know.

        GitCLICommandOutput recordedEntryData = super.execute( repository, command );

        // create a new record in the test-resources folder for this request. and then return 
        buildRecordingFromCliCommandProcessOutput( recordedEntryData );

        return recordedEntryData;
    }

    /**
     * returns if a recording for this particular combination of command and repository is available.
     */
    private boolean isRecordingPresent( ScmRepository repository, GitCommand command ) {
        return false;
    }

    /**
     * @param repository
     * @param command
     * @return
     */
    private byte[] buildCliCommandProcessOutputFromRecording( ScmRepository repository, GitCommand command ) {

        // read and return the byte array from the recording.

        return new byte[0];
    }

    /**
     * @param recordedEntryData
     */
    private void buildRecordingFromCliCommandProcessOutput( GitCLICommandOutput recordedEntryData ) {
        // TODO Auto-generated method stub

    }

//    void foo() {
//
//        // access the test resource 
//        String resourcename = "foo.foo";
//        ClassLoader classLoader = getClass().getClassLoader();
//        File file = new File( classLoader.getResource( resourcename ).getFile() );
//
//        // actually this will point to a compiled version of the class path resource /target/test-classes .... etc.
//    }

//  void foo2() {
//
//      // access the test resource 

//      Path testResourceDirectory = Paths.get("test-resources")
//      File file = testResourceDirectoy.toFile();
//
//  }

}
