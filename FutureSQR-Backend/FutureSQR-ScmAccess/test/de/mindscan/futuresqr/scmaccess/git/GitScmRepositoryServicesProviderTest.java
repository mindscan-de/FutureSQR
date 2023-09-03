package de.mindscan.futuresqr.scmaccess.git;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.scmaccess.configuration.HardcodedScmConfigurationProviderImpl;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;
import de.mindscan.futuresqr.scmaccess.types.ScmUpdateResult;

public class GitScmRepositoryServicesProviderTest {

    @Test
    public void testUpdateProjectCache() throws Exception {
        // arrange
        GitScmRepositoryServicesProvider provider = new GitScmRepositoryServicesProvider( new HardcodedScmConfigurationProviderImpl() );
        provider.setGitCLICommandExecutor( getFakeGitCommandExecutor() );

        // act
        ScmUpdateResult result = provider.updateProjectCache( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "main" );

        // assert

    }

    private FakeGitCLICommandExecutor getFakeGitCommandExecutor() {
        return new FakeGitCLICommandExecutor( new HardcodedScmConfigurationProviderImpl(), true );
    }

}
