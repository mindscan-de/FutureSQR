package de.mindscan.futuresqr.scmaccess.git;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.scmaccess.configuration.HardcodedScmConfigurationProviderImpl;

public class GitScmRepositoryServicesProviderTest {

    @Test
    public void testEmptyTest() throws Exception {
        // intentionally left blank
    }

//    @Test
//    public void testUpdateProjectCache_isSuccessResult() throws Exception {
//        // arrange
//        GitScmRepositoryServicesProvider provider = new GitScmRepositoryServicesProvider( new HardcodedScmConfigurationProviderImpl() );
//        provider.setGitCLICommandExecutor( getFakeGitCommandExecutor() );
//
//        // act
//        ScmUpdateResult result = provider.updateProjectCache( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "main" );
//
//        // assert
//        assertThat( result.getResult(), equalTo( ScmResultType.SUCCESS_RESULT ) );
//    }
//
//    @Test
//    public void testUpdateProjectCache_getFromRevision_resultIsAC96C349() throws Exception {
//        // arrange
//        GitScmRepositoryServicesProvider provider = new GitScmRepositoryServicesProvider( new HardcodedScmConfigurationProviderImpl() );
//        provider.setGitCLICommandExecutor( getFakeGitCommandExecutor() );
//
//        // act
//        ScmUpdateResult result = provider.updateProjectCache( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "main" );
//
//        // assert
//        assertThat( result.getUpdatedFromRevision(), equalTo( "ac96c349" ) );
//    }
//
//    @Test
//    public void testUpdateProjectCache_getToRevision_resultIsB2092B33() throws Exception {
//        // arrange
//        GitScmRepositoryServicesProvider provider = new GitScmRepositoryServicesProvider( new HardcodedScmConfigurationProviderImpl() );
//        provider.setGitCLICommandExecutor( getFakeGitCommandExecutor() );
//
//        // act
//        ScmUpdateResult result = provider.updateProjectCache( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "main" );
//
//        // assert
//        assertThat( result.getUpdatedToRevision(), equalTo( "b2092b33" ) );
//    }

    private FakeGitCLICommandExecutor getFakeGitCommandExecutor() {
        return new FakeGitCLICommandExecutor( new HardcodedScmConfigurationProviderImpl(), true );
    }

}
