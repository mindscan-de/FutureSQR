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
        provider.setGitCLICommandExecutor( new FakeGitCLICommandExecutor() );

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

    @Test
    public void testGetFileChangeListForRevision_FileChangesListContainsModificationsAndRenames_() throws Exception {
        // arrange
        GitScmHistoryProvider provider = new GitScmHistoryProvider();
        provider.setGitCLICommandExecutor( new FakeGitCLICommandExecutor() );

        // act
        provider.getFileChangeListForRevision( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "fbf54da649b05f3cdd372e08543435c7d0d30356" );

        // assert
    }

    @Test
    public void testGetFileChangeListForRevision_FileChangesListContainsAddedBinaryFiles_() throws Exception {
        // arrange
        GitScmHistoryProvider provider = new GitScmHistoryProvider();
        provider.setGitCLICommandExecutor( new FakeGitCLICommandExecutor() );

        // act
        provider.getFileChangeListForRevision( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "dec95d157e4df48bd1c112d9b69630ed96c38a88" );

        // assert

    }

    @Test
    public void testGetFileChangeListForRevision_FileChangesListContainsDeletedBinaryFiles_() throws Exception {
        // arrange
        GitScmHistoryProvider provider = new GitScmHistoryProvider();
        provider.setGitCLICommandExecutor( new FakeGitCLICommandExecutor() );

        // act
        provider.getFileChangeListForRevision( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "ce0a59f3635ade74969709f59aa517e7adbc846e" );

        // assert
    }

    @Test
    public void testGetFileChangeListForRevision_FileChangesListContainsDeletedFiles_() throws Exception {
        // arrange
        GitScmHistoryProvider provider = new GitScmHistoryProvider();
        provider.setGitCLICommandExecutor( new FakeGitCLICommandExecutor() );

        // act
        provider.getFileChangeListForRevision( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "fa7cd0b2ae4057f8a0b2acdf057082ffa997a268" );

        // assert
    }

    @Test
    public void testGetFileChangeListForRevision_FileChangesListContainsEmptyNewFiles_() throws Exception {
        // arrange
        GitScmHistoryProvider provider = new GitScmHistoryProvider();
        provider.setGitCLICommandExecutor( new FakeGitCLICommandExecutor() );

        // act
        provider.getFileChangeListForRevision( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "a33ce773920ad08d74cc8bdda0cdafe66fe5f2a8" );

        // assert
    }

    @Test
    public void testGetFileChangeListForRevision_FileChangesListContainsDeletedFilesAndFolders_() throws Exception {
        // arrange
        GitScmHistoryProvider provider = new GitScmHistoryProvider();
        provider.setGitCLICommandExecutor( new FakeGitCLICommandExecutor() );

        // act
        provider.getFileChangeListForRevision( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "f9b185c819c43e8180550603ec9bb7b56c5b184d" );

        // assert
    }

    @Test
    public void testGetFileChangeListForRevision_FileChangesListContainsRenamesAndMovedFiles_() throws Exception {
        // arrange
        GitScmHistoryProvider provider = new GitScmHistoryProvider();
        provider.setGitCLICommandExecutor( new FakeGitCLICommandExecutor() );

        // act
        provider.getFileChangeListForRevision( new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" ), "8516e0496845798b09ebda5623d0371ce5afbe69" );

        // assert
    }

}
