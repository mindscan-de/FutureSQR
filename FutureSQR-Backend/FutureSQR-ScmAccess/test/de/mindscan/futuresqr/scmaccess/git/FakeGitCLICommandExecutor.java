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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.mindscan.futuresqr.scmaccess.ScmConfigurationProvider;
import de.mindscan.futuresqr.scmaccess.configuration.HardcodedScmConfigurationProviderImpl;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

/**
 * The {@link FakeGitCLICommandExecutor} serves multiple purposes. It can record the output for a particular
 * command and a particular repository and save the result to the test-resources folder, such that the recorded
 * output can be captured once and replayed again, whenever it is required.
 * 
 * Think in cases of repository operations, a call on a git repository may alter future requests. The basic idea
 * is to provide the same test-cases and replay results, such that regression tests can be implemented. This 
 * will create a possibility to provide all sorts of test cases, and the test cases will be executable and 
 * running correctly, even if they run them on a different machine or with no repositories at all.
 * 
 * We are now able to still execute unit tests, with output from a former real invocation. While executing unit
 * tests we neither want to execute system processes, nor do we want to alter the results. This whole class 
 * replaces a system-call to "git", with a read operation on the test-resources folder and with de-serializing 
 * the results. 
 *
 * This GitCLICommandExecutor can be injected into the provider(s) using a setter.
 * 
 * Also this seems to be a nice proof-of-concept, for a fast in-memory-cache for accessing scm repositories,
 * because this class already is essentially a cache for scm executions, even if they took a quite long time. 
 * Such a system will be necessary for scm's which require internet access, which might not be feasible for 
 * tests or at the rate, which the server would query them. 
 * 
 */
public class FakeGitCLICommandExecutor extends GitCLICommandExecutor {

    private Gson gson = new Gson();
    private boolean neverInvokeSuperOnExecute;

    private Type gitCLICommandOutputType = new TypeToken<GitCLICommandOutput>() {
    }.getType();

    /**
     * 
     */
    public FakeGitCLICommandExecutor() {
        this( new HardcodedScmConfigurationProviderImpl(), true );
    }

    /**
     * 
     */
    public FakeGitCLICommandExecutor( ScmConfigurationProvider configProvider, boolean neverInvokeSuperOnExecute ) {
        super( configProvider );

        this.neverInvokeSuperOnExecute = neverInvokeSuperOnExecute;
    }

    /** 
     * {@inheritDoc}
     */
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

        return new BigInteger( 1, commandDigest ).toString( 16 ).substring( 0, 12 );
    }

    private byte[] loadRecordingProcessOutput( ScmRepository scmRepository, GitCommand command ) {
        String repositorySignature = calculateRepositorySignature( scmRepository );
        String commandSignature = calculateCommandSignature( command );

        Path inputFile = getCommandPathInFakeRepoPath( repositorySignature, commandSignature );

        try (FileReader fileReader = new FileReader( inputFile.toAbsolutePath().toString() )) {
            GitCLICommandOutput result = (GitCLICommandOutput) gson.fromJson( fileReader, gitCLICommandOutputType );
            return result.getProcessOutput();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    private void saveRecording( GitCLICommandOutput recordedEntryData ) {
        String repositorySignature = calculateRepositorySignature( recordedEntryData.getRepository() );
        String commandSignature = calculateCommandSignature( recordedEntryData.getCommand() );

        Path outputFile = getCommandPathInFakeRepoPath( repositorySignature, commandSignature );

        try (FileWriter writer = new FileWriter( outputFile.toString() )) {
            String jsonString = gson.toJson( recordedEntryData );
            writer.write( jsonString );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
