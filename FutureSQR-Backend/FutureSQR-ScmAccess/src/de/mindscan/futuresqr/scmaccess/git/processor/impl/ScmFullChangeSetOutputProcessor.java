import de.mindscan.futuresqr.scmaccess.git.processor.impl.parsers.ContentChangeSetParsers;
import de.mindscan.futuresqr.scmaccess.git.processor.impl.parsers.FileChangeSetParsers;
    // TODO NEXT: two newlines are a separator for revision-Data / revision information.
    // TODO NEXT: a new line and a space on the next followed by newline is a newline in the file.

        // TODO: parse the commit + id /Author/date/message andmaybe also provide a list of full changesets .... instead of one.

        // System.out.println( string );
        // TODO NEXT: must split to new commit, we get the diff for each single commit in between. not what I expected.....
        // TODO NEXT: maybe a list of full change sets for each revision one entry in the list.


                // TODO: reinitialize for next revision.?
                // TODO: maybe adding the ScmFileChangeset should be part of this method? not of the parseFileChangeSetEntry.
            String currentLazyDiffLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseGitDiffLineToFileChangeSet( currentLazyDiffLine, currentFileChangeSet );
            String newFileModeLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseNewFileModeLineToFileChangeSet( newFileModeLine, currentFileChangeSet );
            currentFileChangeSet.fileAction = "A";

            currentFileChangeSet.fileAction = "D";
            currentFileChangeSet.fileAction = "R";
            String currentIndexLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseIndexLineToFileChangeSet( currentIndexLine, currentFileChangeSet );
            if (currentFileChangeSet.fileAction.isEmpty()) {
                // set fileAction("M") if not already marked as "R", R is more important than "M")
                currentFileChangeSet.fileAction = "M";
            }
        // MAYBE we want
            String contentChangeSetLineInfo = lineLexer.consumeCurrentLine();
            ContentChangeSetParsers.parseLineInfoIntoContentChangeSet( contentChangeSetLineInfo, contentChangeset );
            else if (currentLine.equals( "" )) {
                // actually we are done with this file.
                break;
            }