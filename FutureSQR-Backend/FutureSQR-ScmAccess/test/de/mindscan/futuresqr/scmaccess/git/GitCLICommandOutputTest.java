package de.mindscan.futuresqr.scmaccess.git;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

public class GitCLICommandOutputTest {

    @Test
    public void testSetRepository_CtorNullsSetThenGet_expectSameInstance() throws Exception {
        // arrange
        GitCLICommandOutput commandOutput = new GitCLICommandOutput( null, null );
        ScmRepository expectedRepository = createScmRepositiryObject();

        // act
        commandOutput.setRepository( expectedRepository );

        // assert
        ScmRepository result = commandOutput.getRepository();
        assertThat( result, is( sameInstance( expectedRepository ) ) );
    }

    @Test
    public void testGetRepository_CtorNullsSetThenGet_expectSameInstance() throws Exception {
        // arrange
        GitCLICommandOutput commandOutput = new GitCLICommandOutput( null, null );
        ScmRepository expectedRepository = createScmRepositiryObject();
        commandOutput.setRepository( expectedRepository );

        // act
        ScmRepository result = commandOutput.getRepository();

        // assert
        assertThat( result, is( sameInstance( expectedRepository ) ) );
    }

    @Test
    public void testGetRepository_CtorSetExpectedRepo_expectSameInstance() throws Exception {
        // arrange
        ScmRepository expectedRepository = createScmRepositiryObject();
        GitCLICommandOutput commandOutput = new GitCLICommandOutput( expectedRepository, null );

        // act
        ScmRepository result = commandOutput.getRepository();

        // assert
        assertThat( result, is( sameInstance( expectedRepository ) ) );
    }

    @Test
    public void testGetCommand_CtorNullsSetThenGet_expectSameInstance() throws Exception {
        // arrange
        GitCLICommandOutput commandOutput = new GitCLICommandOutput( null, null );
        GitCommand expectedCommand = Mockito.mock( GitCommand.class, "expectedCommand" );

        commandOutput.setCommand( expectedCommand );

        // act
        GitCommand result = commandOutput.getCommand();

        // assert
        assertThat( result, is( sameInstance( expectedCommand ) ) );
    }

    @Test
    public void testSetCommand_CtorNullsSetThenGet_expectSameInstance() throws Exception {
        // arrange
        GitCLICommandOutput commandOutput = new GitCLICommandOutput( null, null );
        GitCommand expectedCommand = Mockito.mock( GitCommand.class, "expectedCommand" );

        // act
        commandOutput.setCommand( expectedCommand );

        // assert
        GitCommand result = commandOutput.getCommand();
        assertThat( result, is( sameInstance( expectedCommand ) ) );
    }

    @Test
    public void testGetCommand_CtorSetExpectedCommand_expectSameInstance() throws Exception {
        // arrange
        GitCommand expectedCommand = Mockito.mock( GitCommand.class, "expectedCommand" );
        GitCLICommandOutput commandOutput = new GitCLICommandOutput( null, expectedCommand );

        commandOutput.setCommand( expectedCommand );

        // act
        GitCommand result = commandOutput.getCommand();

        // assert
        assertThat( result, is( sameInstance( expectedCommand ) ) );
    }

    @Test
    public void testSetProcessOutput_SetThenGet_expectSameInstance() throws Exception {
        // arrange
        GitCLICommandOutput commandOutput = new GitCLICommandOutput( null, null );
        byte[] expectedProcessOutput = new byte[3];

        // act
        commandOutput.setProcessOutput( expectedProcessOutput );

        // assert
        byte[] result = commandOutput.getProcessOutput();
        assertThat( result, is( sameInstance( expectedProcessOutput ) ) );
    }

    @Test
    public void testGetProcessOutput_SetThenGet_expectSameInstance() throws Exception {
        // arrange
        GitCLICommandOutput commandOutput = new GitCLICommandOutput( null, null );
        byte[] expectedProcessOutput = new byte[5];
        commandOutput.setProcessOutput( expectedProcessOutput );

        // act
        byte[] result = commandOutput.getProcessOutput();

        // assert
        assertThat( result, is( sameInstance( expectedProcessOutput ) ) );
    }

    @Test
    public void testTransform__processorTransformIsInvokedWithOutputObject() throws Exception {
        // arrange
        GitCLICommandOutput commandOutput = new GitCLICommandOutput( null, null );
        GitCLICommandOutputProcessor<String> processor = (GitCLICommandOutputProcessor<String>) Mockito.mock( GitCLICommandOutputProcessor.class, "processor" );

        // act
        commandOutput.transform( processor );

        // assert
        Mockito.verify( processor, times( 1 ) ).transform( Mockito.eq( commandOutput ) );
    }

    private ScmRepository createScmRepositiryObject() {
        return new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" );
    }

}
