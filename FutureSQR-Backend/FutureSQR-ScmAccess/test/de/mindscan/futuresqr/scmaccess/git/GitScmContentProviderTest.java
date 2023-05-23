package de.mindscan.futuresqr.scmaccess.git;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.scmaccess.configuration.HardcodedScmConfigurationProviderImpl;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContent;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmPath;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

public class GitScmContentProviderTest {

    private static final String REVISION_WITH_ONE_MODIFIED_FILE = "606744052ab47c43f44449f1c98b1a7ee6510ae8";
    private static final String REVISION_WITH_ONE_ADDED_FILE = "2b5816635999dad0de0f280d370180a9a04ab25a";
    private static final String REVISION_WITH_TWO_ADDED_FILES = "62ee2c96087686cc72500a86984e0fd977d88aa0";

    @Test
    public void testGetFileContentForRevision() throws Exception {
        // arrange
        GitScmContentProvider provider = new GitScmContentProvider();
        provider.setGitCLICommandExecutor( new FakeGitCLICommandExecutor() );

        // act
        ScmFileContent result = provider.getFileContentForRevision( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ),
                        "3c6666b2bc5a868baa802d8879781e4acca1d7c6",
                        new ScmPath( "FutureSQR-Frontend/src/app/views/single-file-revision-page/other-file-revisions-panel/other-file-revisions-panel.component.html" ) );

        // assert
        System.out.println( result.fileContent );
    }

    @Test
    public void testGetFullChangeSetForRevision_RevOneModifiedFile_hasOneFileInChangeSet() throws Exception {
        // arrange
        GitScmContentProvider provider = new GitScmContentProvider();
        provider.setGitCLICommandExecutor( getFakeGitExecutor() );

        // act
        ScmFullChangeSet result = provider.getFullChangeSetForRevision( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ),
                        REVISION_WITH_ONE_MODIFIED_FILE );

        // assert
        assertThat( result.fileChangeSet, Matchers.hasSize( 1 ) );
    }

    @Test
    public void testGetFullChangeSetForRevision_RevOneAddedFile_hasOneFileInChangeSet() throws Exception {
        // arrange
        GitScmContentProvider provider = new GitScmContentProvider();
        provider.setGitCLICommandExecutor( getFakeGitExecutor() );

        // act
        ScmFullChangeSet result = provider.getFullChangeSetForRevision( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ),
                        REVISION_WITH_ONE_ADDED_FILE );

        // assert
        assertThat( result.fileChangeSet, Matchers.hasSize( 1 ) );
    }

    @Test
    public void testGetFullChangeSetForRevision_RevTwoAddedFiles_hasTwoFilesInChangeSet() throws Exception {
        // arrange
        GitScmContentProvider provider = new GitScmContentProvider();
        provider.setGitCLICommandExecutor( getFakeGitExecutor() );

        // act
        ScmFullChangeSet result = provider.getFullChangeSetForRevision( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ),
                        REVISION_WITH_TWO_ADDED_FILES );

        // assert
        assertThat( result.fileChangeSet, Matchers.hasSize( 2 ) );
    }

    @Test
    public void testGetFullChangeSetFromRevisionToRevision_TwoRevisionsInARow_hasTwoElementsInRevisionList() throws Exception {
        // arrange  ed0abce8fa030c9a29f821c4961d2fd8ba171869  to 76553c9b515e700227753731cb8c2266b8965aa0
        GitScmContentProvider provider = new GitScmContentProvider();
        provider.setGitCLICommandExecutor( getFakeGitExecutor() );

        // act
        List<ScmFullChangeSet> result = provider.getFullChangeSetFromRevisionToRevision( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ),
                        "ed0abce8fa030c9a29f821c4961d2fd8ba171869", "76553c9b515e700227753731cb8c2266b8965aa0" );

        // assert
        assertThat( result, Matchers.hasSize( 2 ) );
    }

    private FakeGitCLICommandExecutor getFakeGitExecutor() {
        return new FakeGitCLICommandExecutor( new HardcodedScmConfigurationProviderImpl(), true );
    }

}
