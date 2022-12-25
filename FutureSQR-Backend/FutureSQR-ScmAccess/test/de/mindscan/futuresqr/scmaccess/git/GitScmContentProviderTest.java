package de.mindscan.futuresqr.scmaccess.git;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.scmaccess.types.ScmFileContent;
import de.mindscan.futuresqr.scmaccess.types.ScmPath;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

public class GitScmContentProviderTest {

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

}
