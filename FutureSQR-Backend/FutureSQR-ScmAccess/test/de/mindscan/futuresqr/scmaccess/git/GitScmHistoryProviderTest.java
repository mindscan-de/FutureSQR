package de.mindscan.futuresqr.scmaccess.git;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.scmaccess.types.ScmBasicRevisionInformation;
import de.mindscan.futuresqr.scmaccess.types.ScmFileHistory;
import de.mindscan.futuresqr.scmaccess.types.ScmHistory;
import de.mindscan.futuresqr.scmaccess.types.ScmPath;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

public class GitScmHistoryProviderTest {

    @Test
    public void testGetFilePathHistory() throws Exception {
        // arrange
        GitScmHistoryProvider provider = new GitScmHistoryProvider();
        provider.setGitCLICommandExecutor( new GitCLICommandExecutor() );

        // act
        ScmFileHistory result = provider.getFilePathHistory( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), new ScmPath(
                        "FutureSQR-Frontend/src/app/views/single-file-revision-page/other-file-revisions-panel/other-file-revisions-panel.component.html" ) );

        // assert

    }

    @Test
    public void testGetNRecentRevisions() throws Exception {
        // arrange
        GitScmHistoryProvider provider = new GitScmHistoryProvider();
        provider.setGitCLICommandExecutor( new FakeGitCLICommandExecutor() );

        // act
        ScmHistory result = provider.getNRecentRevisions( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), 8 );

        // assert
        List<ScmBasicRevisionInformation> revisions = result.revisions;

        for (ScmBasicRevisionInformation revision : revisions) {
            System.out.println( revision.message );
        }
    }

}
