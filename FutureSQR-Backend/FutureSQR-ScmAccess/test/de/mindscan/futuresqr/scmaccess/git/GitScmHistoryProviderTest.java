package de.mindscan.futuresqr.scmaccess.git;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.scmaccess.types.ScmFileHistory;
import de.mindscan.futuresqr.scmaccess.types.ScmPath;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

public class GitScmHistoryProviderTest {

    @Test
    public void testGetFilePathHistory() throws Exception {
        // arrange
        GitScmHistoryProvider provider = new GitScmHistoryProvider();

        // act
        ScmFileHistory result = provider.getFilePathHistory( (ScmRepository) null, new ScmPath(
                        "FutureSQR-Frontend/src/app/views/single-file-revision-page/other-file-revisions-panel/other-file-revisions-panel.component.html" ) );

        // assert

    }

}
