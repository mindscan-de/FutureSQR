package de.mindscan.futuresqr.scmaccess.git;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.scmaccess.configuration.HardcodedScmConfigurationProviderImpl;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;
import de.mindscan.futuresqr.scmaccess.types.ScmResultType;
import de.mindscan.futuresqr.scmaccess.types.ScmUpdateResult;

public class GitScmRepositoryServicesProviderTest {

    @Test
    public void testUpdateProjectCache_isSuccessResult() throws Exception {
        // arrange
        GitScmRepositoryServicesProvider provider = new GitScmRepositoryServicesProvider( new HardcodedScmConfigurationProviderImpl() );
        provider.setGitCLICommandExecutor( getFakeGitCommandExecutor() );

        // act
        ScmUpdateResult result = provider.updateProjectCache( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "main" );

        // assert
        assertThat( result.getResult(), equalTo( ScmResultType.SUCCESS_RESULT ) );
    }

    @Test
    public void testUpdateProjectCache_getFromRevision_resultIsAC96C349() throws Exception {
        // arrange
        GitScmRepositoryServicesProvider provider = new GitScmRepositoryServicesProvider( new HardcodedScmConfigurationProviderImpl() );
        provider.setGitCLICommandExecutor( getFakeGitCommandExecutor() );

        // act
        ScmUpdateResult result = provider.updateProjectCache( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "main" );

        // assert
        assertThat( result.getUpdatedFromRevision(), equalTo( "ac96c349" ) );
    }

    @Test
    public void testUpdateProjectCache_getToRevision_resultIsB2092B33() throws Exception {
        // arrange
        GitScmRepositoryServicesProvider provider = new GitScmRepositoryServicesProvider( new HardcodedScmConfigurationProviderImpl() );
        provider.setGitCLICommandExecutor( getFakeGitCommandExecutor() );

        // act
        ScmUpdateResult result = provider.updateProjectCache( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "main" );

        // assert
        assertThat( result.getUpdatedToRevision(), equalTo( "b2092b33" ) );
    }

    private FakeGitCLICommandExecutor getFakeGitCommandExecutor() {
        return new FakeGitCLICommandExecutor( new HardcodedScmConfigurationProviderImpl(), true );
    }

}
