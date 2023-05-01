import de.mindscan.futuresqr.scmaccess.git.processor.impl.parsers.GitOutputParsingConstants;
import de.mindscan.futuresqr.scmaccess.types.ScmFileAction;
    private static final String GIT_DIFF_FILENAMEINFO_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_FILENAMEINFO_IDENTIFIER;
    private static final String GIT_DIFF_NEW_FILE_MODE = GitOutputParsingConstants.GIT_DIFF_NEW_FILE_MODE;
    private static final String GIT_DIFF_DELETED_FILE_MODE = GitOutputParsingConstants.GIT_DIFF_DELETED_FILE_MODE;
    private static final String GIT_DIFF_RENAME_SIMILARITY_INDEX = GitOutputParsingConstants.GIT_DIFF_RENAME_SIMILARITY_INDEX;
    private static final String GIT_DIFF_RENAME_FROM = GitOutputParsingConstants.GIT_DIFF_RENAME_FROM;
    private static final String GIT_DIFF_RENAME_TO = GitOutputParsingConstants.GIT_DIFF_RENAME_TO;
    private static final String GIT_DIFF_BINARY_FILES_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_BINARY_FILES_IDENTIFIER;
    private static final String GIT_DIFF_LEFT_FILEPATH_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_LEFT_FILEPATH_IDENTIFIER;
    private static final String GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER;
    private static final String GIT_DIFF_NO_NEWLINE_AT_END_OF_FILE_INDICATOR = GitOutputParsingConstants.GIT_DIFF_NO_NEWLINE_AT_END_OF_FILE_INDICATOR;
    private static final String GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER;
    private static final String GIT_DIFF_NEWCOMMIT_AUTHOR_LINE_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_NEWCOMMIT_AUTHOR_LINE_IDENTIFIER;
    private static final String GIT_DIFF_NEWCOMMIT_DATE_LINE_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_NEWCOMMIT_DATE_LINE_IDENTIFIER;
    private static final String GIT_DIFF_NEWCOMMIT_FOUR_SAPCE_INDENT = GitOutputParsingConstants.GIT_DIFF_NEWCOMMIT_FOUR_SAPCE_INDENT;

        // links on this page are outdated but provides a good overview. http://www.i18nguy.com/unicode/codepages.html
        //-
        // byte to unicode tables e.g. for windows codepages
        // https://www.unicode.org/Public/MAPPINGS/VENDORS/MICSFT/WINDOWS/
        // ....
        // http://www.unicode.org/Public/MAPPINGS/VENDORS/MICSFT/WINDOWS/CP1251.TXT
        // http://www.unicode.org/Public/MAPPINGS/VENDORS/MICSFT/WINDOWS/CP1252.TXT
        // ....
                if (commentline.startsWith( GIT_DIFF_NEWCOMMIT_FOUR_SAPCE_INDENT )) {
                    // truncate 4 space indent from new commit comment line
                    commitLines.add( commentline.substring( GIT_DIFF_NEWCOMMIT_FOUR_SAPCE_INDENT.length() ) );
            currentFileChangeSet.fileAction = ScmFileAction.FILEACTION_ADD;
            currentFileChangeSet.fileAction = ScmFileAction.FILEACTION_DELETE;
            currentFileChangeSet.fileAction = ScmFileAction.FILEACTION_RENAME;
            String renameFromLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseRenameFrom( renameFromLine, currentFileChangeSet );
            String renameToLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseRenameTo( renameToLine, currentFileChangeSet );
            if (ScmFileAction.FILEACTION_UNKNOWN.equals( currentFileChangeSet.fileAction )) {
                currentFileChangeSet.fileAction = ScmFileAction.FILEACTION_MODIFY;
            String binaryFileInfoLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseBinaryFileInfoLine( binaryFileInfoLine, currentFileChangeSet );
            String leftFilePathLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseLeftFilePath( leftFilePathLine, currentFileChangeSet );
            String rightFilePathLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseRightFilePath( rightFilePathLine, currentFileChangeSet );