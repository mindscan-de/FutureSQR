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

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    // TODO we can either record the output and store it
    // or we can replay the output generated previously for the repository and the given command.
    // we can do both in case we encounter a new combination, which we don't know.
    @Override
    public GitCLICommandOutput execute( ScmRepository repository, GitCommand command ) {
        if (isRecordingPresent( repository, command )) {
            GitCLICommandOutput result = new GitCLICommandOutput( repository, command );

            result.setProcessOutput( loadRecordingProcessOutput( repository, command ) );

            return result;
        }

        if (neverInvokeSuperOnExecute) {
            throw new RuntimeException( "There is no recording for this repository and command available." );
        }

        GitCLICommandOutput recordedEntryData = super.execute( repository, command );

        // create a new record in the test-resources folder for this request. and then return 
        saveRecording( recordedEntryData );

        return recordedEntryData;
    }

    /**
     * returns if a recording for this particular combination of command and repository is available.
     */
    private boolean isRecordingPresent( ScmRepository scmRepository, GitCommand command ) {
        String respositorySignature = calculateRepositorySignature( scmRepository );
        if (hasFakeRepositoryPath( respositorySignature )) {
            String commandSignature = calculateCommandSignature( command );
            if (hasGitCommandSignatureInRepo( respositorySignature, commandSignature )) {
                return true;
            }
        }
        else {
            if (!neverInvokeSuperOnExecute) {
                Path path = getFakeRepositoryPath( respositorySignature );
                try {
                    Files.createDirectories( path.toAbsolutePath() );
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    private String calculateRepositorySignature( ScmRepository scmRepository ) {
        byte[] repoDigest = { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff };

        try {
            repoDigest = MessageDigest.getInstance( "MD5" ).digest( scmRepository.getLocalRepositoryPath().getBytes() );
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // convert to hex and only use first 7 hex nibbles
        return new BigInteger( 1, repoDigest ).toString( 16 ).substring( 0, 7 );
    }

    private boolean hasFakeRepositoryPath( String reposignature ) {
        Path testResourceDirectory = getFakeRepositoryPath( reposignature );
        System.out.println( testResourceDirectory.toAbsolutePath().toString() );
        return Files.isDirectory( testResourceDirectory );
    }

    private Path getFakeRepositoryPath( String respositorySignature ) {
        return Paths.get( "test-resources/repo." + respositorySignature );
    }

    private boolean hasGitCommandSignatureInRepo( String respositorySignature, String commandSignature ) {
        Path commandFilePath = getCommandPathInFakeRepoPath( respositorySignature, commandSignature );
        return Files.isRegularFile( commandFilePath );
    }

    private Path getCommandPathInFakeRepoPath( String respositorySignature, String commandSignature ) {
        return getFakeRepositoryPath( respositorySignature ).resolve( "cmd." + commandSignature + ".json" );
    }

    private String calculateCommandSignature( GitCommand command ) {
        byte[] commandDigest = { (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe };

        try {
            commandDigest = MessageDigest.getInstance( "MD5" ).digest( String.join( "|", command.getArguments() ).getBytes() );
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // convert to hex and only use first 12 hex nibbles
        return new BigInteger( 1, commandDigest ).toString( 16 ).substring( 0, 12 );
    }

    /**
     * @param repository
     * @param command
     * @return
     */
    private byte[] loadRecordingProcessOutput( ScmRepository scmRepository, GitCommand command ) {
        String repositorySignature = calculateRepositorySignature( scmRepository );
        String commandSignature = calculateCommandSignature( command );

        Path inputFile = getCommandPathInFakeRepoPath( repositorySignature, commandSignature );

        // read and but only return the byte array from the recording.

        return new byte[0];
    }

    /**
     * @param recordedEntryData
     */
    private void saveRecording( GitCLICommandOutput recordedEntryData ) {
        String repositorySignature = calculateRepositorySignature( recordedEntryData.getRepository() );
        String commandSignature = calculateCommandSignature( recordedEntryData.getCommand() );

        Path outputFile = getCommandPathInFakeRepoPath( repositorySignature, commandSignature );

        // save the recorded entry data to file.
    }

}
