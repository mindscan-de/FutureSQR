/**
 * 
 * MIT License
 *
 * Copyright (c) 2023 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.futuresqr.scmaccess.git.processor.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutput;
import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutputProcessor;
import de.mindscan.futuresqr.scmaccess.git.processor.impl.parsers.ContentChangeSetParsers;
import de.mindscan.futuresqr.scmaccess.git.processor.impl.parsers.FileChangeSetParsers;
import de.mindscan.futuresqr.scmaccess.git.processor.impl.parsers.FullChangeSetParsers;
import de.mindscan.futuresqr.scmaccess.git.processor.impl.parsers.GitOutputParsingConstants;
import de.mindscan.futuresqr.scmaccess.types.ScmDiffLine;
import de.mindscan.futuresqr.scmaccess.types.ScmDiffLineType;
import de.mindscan.futuresqr.scmaccess.types.ScmFileAction;
import de.mindscan.futuresqr.scmaccess.types.ScmFileChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContentChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;

/**
 * 
 */
public class ScmFullChangeSetListOutputProcessor implements GitCLICommandOutputProcessor<List<ScmFullChangeSet>> {

    private static final String GIT_DIFF_FILENAMEINFO_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_FILENAMEINFO_IDENTIFIER;
    private static final String GIT_DIFF_NEW_FILE_MODE = GitOutputParsingConstants.GIT_DIFF_NEW_FILE_MODE;
    private static final String GIT_DIFF_DELETED_FILE_MODE = GitOutputParsingConstants.GIT_DIFF_DELETED_FILE_MODE;

    private static final String GIT_DIFF_RENAME_SIMILARITY_INDEX = GitOutputParsingConstants.GIT_DIFF_RENAME_SIMILARITY_INDEX;
    private static final String GIT_DIFF_RENAME_FROM = GitOutputParsingConstants.GIT_DIFF_RENAME_FROM;
    private static final String GIT_DIFF_RENAME_TO = GitOutputParsingConstants.GIT_DIFF_RENAME_TO;

    private static final String GIT_DIFF_INDEX_IDENTIFIER = "index";
    private static final String GIT_DIFF_BINARY_FILES_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_BINARY_FILES_IDENTIFIER;

    private static final String GIT_DIFF_LEFT_FILEPATH_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_LEFT_FILEPATH_IDENTIFIER;
    private static final String GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER;

    private static final String GIT_DIFF_CONTENT_CHANGESET_IDENTIFIER = "@@ ";

    private static final String GIT_DIFF_NO_NEWLINE_AT_END_OF_FILE_INDICATOR = GitOutputParsingConstants.GIT_DIFF_NO_NEWLINE_AT_END_OF_FILE_INDICATOR;

    private static final String GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER;
    private static final String GIT_DIFF_NEWCOMMIT_AUTHOR_LINE_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_NEWCOMMIT_AUTHOR_LINE_IDENTIFIER;
    private static final String GIT_DIFF_NEWCOMMIT_DATE_LINE_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_NEWCOMMIT_DATE_LINE_IDENTIFIER;
    private static final String GIT_DIFF_NEWCOMMIT_FOUR_SAPCE_INDENT = GitOutputParsingConstants.GIT_DIFF_NEWCOMMIT_FOUR_SAPCE_INDENT;
    private static final String GIT_DIFF_EMPTY_LINE = "";

    /**
     * 
     */
    public ScmFullChangeSetListOutputProcessor() {
        // intentionally left blank.
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public List<ScmFullChangeSet> transform( GitCLICommandOutput output ) {

        // TODO: analyze the standard changesets, UTF_8 ...
        // TODO: Actually each file can have it's own encoding, such that we must provide a scanner with different charset modes
        // this needs to be fixed longer term.

        // links on this page are outdated but provides a good overview. http://www.i18nguy.com/unicode/codepages.html
        //-
        // byte to unicode tables e.g. for windows codepages
        // https://www.unicode.org/Public/MAPPINGS/VENDORS/MICSFT/WINDOWS/
        // ....
        // http://www.unicode.org/Public/MAPPINGS/VENDORS/MICSFT/WINDOWS/CP1251.TXT
        // http://www.unicode.org/Public/MAPPINGS/VENDORS/MICSFT/WINDOWS/CP1252.TXT
        // ....
        List<ScmFullChangeSet> resultList = parseFullChangeSetList( new String( output.getProcessOutput(), StandardCharsets.UTF_8 ) );

        return resultList;
    }

    private List<ScmFullChangeSet> parseFullChangeSetList( String string ) {
        List<ScmFullChangeSet> resultList = new ArrayList<>();

        // For debug purposes 
        System.out.println( string );

        // prepare line lexxer
        GitScmLineBasedLexer lineLexer = new GitScmLineBasedLexer( string.split( "\\R" ) );

        while (lineLexer.hasNextLine()) {
            String currentLine = lineLexer.peekCurrentLine();

            // peek at current line
            if (currentLine.startsWith( GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER )) {
                // for each commit identifier
                System.out.println( "[parseFullChangeSetList][p]: '" + currentLine + "'" );
                resultList.add( parseFullChangeSet( lineLexer ) );
            }
            else {
                // This will either skip some header or unparsed stuff from the inner parsers.
                System.out.println( "[parseFullChangeSetList][u]: '" + currentLine + "'" );
                lineLexer.advanceToNextLine();
            }
        }

        return resultList;
    }

    private ScmFullChangeSet parseFullChangeSet( GitScmLineBasedLexer lineLexer ) {
        ScmFullChangeSet scmFullChangeSet = new ScmFullChangeSet();
        Consumer<ScmFileChangeSet> fileChangeSetConsumer = scmFullChangeSet.fileChangeSet::add;

        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER )) {
            String commitRevisionIdLine = lineLexer.consumeCurrentLine();
            FullChangeSetParsers.parseCommitRevisionLineToFullChangeSet( commitRevisionIdLine, scmFullChangeSet );
        }

        // read and parse Author
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_NEWCOMMIT_AUTHOR_LINE_IDENTIFIER )) {
            String commitAuthorLine = lineLexer.consumeCurrentLine();
            FullChangeSetParsers.parseCommitAuthorToFullChangeSet( commitAuthorLine, scmFullChangeSet );
        }

        // read and parse DATE
        if ((lineLexer.peekCurrentLine().startsWith( GIT_DIFF_NEWCOMMIT_DATE_LINE_IDENTIFIER ))) {
            String commitDateLine = lineLexer.consumeCurrentLine();
            FullChangeSetParsers.parseCommitDateToFullChangeSet( commitDateLine, scmFullChangeSet );
        }

        // read commit message.
        if (GIT_DIFF_EMPTY_LINE.equals( lineLexer.peekCurrentLine() )) {
            List<String> commitLines = new ArrayList<>();

            // consume empty line
            lineLexer.consumeCurrentLine();

            while (!GIT_DIFF_EMPTY_LINE.equals( lineLexer.peekCurrentLine() )) {
                String commentline = lineLexer.consumeCurrentLine();

                if (commentline.startsWith( GIT_DIFF_NEWCOMMIT_FOUR_SAPCE_INDENT )) {
                    // truncate 4 space indent from new commit comment line
                    commitLines.add( commentline.substring( GIT_DIFF_NEWCOMMIT_FOUR_SAPCE_INDENT.length() ) );
                }
            }

            // consume empty line
            lineLexer.consumeCurrentLine();

            String commitMessage = String.join( "\n", commitLines );
            FullChangeSetParsers.parseCommitMessageToFullChangeSet( commitMessage, scmFullChangeSet );
        }

        while (lineLexer.hasNextLine() && !lineLexer.peekCurrentLine().startsWith( GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER )) {
            String currentLine = lineLexer.peekCurrentLine();

            // peek at current line
            if (currentLine.startsWith( GIT_DIFF_FILENAMEINFO_IDENTIFIER )) {
                // for each file file entry
                System.out.println( "[parseFullChangeSet][p]: '" + currentLine + "'" );
                fileChangeSetConsumer.accept( parseFileChangeSetEntry( lineLexer ) );
            }
            else {
                // This will either skip some header or unparsed stuff from the inner parsers.
                System.out.println( "[parseFullChangeSet][u]: '" + currentLine + "'" );
                lineLexer.advanceToNextLine();
            }
        }

        // first menge for file entry will trigger parseFileChangeSetEntry
        return scmFullChangeSet;
    }

    private ScmFileChangeSet parseFileChangeSetEntry( GitScmLineBasedLexer lineLexer ) {
        // create a new file entry
        ScmFileChangeSet currentFileChangeSet = new ScmFileChangeSet();

        // Parse the file info
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_FILENAMEINFO_IDENTIFIER )) {
            String currentLazyDiffLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseGitDiffLineToFileChangeSet( currentLazyDiffLine, currentFileChangeSet );
        }

        // ---------------------
        // parse file mode
        // ---------------------

        // Parse new file mode
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_NEW_FILE_MODE )) {
            String newFileModeLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseNewFileModeLineToFileChangeSet( newFileModeLine, currentFileChangeSet );
            currentFileChangeSet.fileAction = ScmFileAction.FILEACTION_ADD;
        }

        // Parse deleted file mode
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_DELETED_FILE_MODE )) {
            String deleteFileModeLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseDeleteFileModeLineToFileChangeSet( deleteFileModeLine, currentFileChangeSet );
            currentFileChangeSet.fileAction = ScmFileAction.FILEACTION_DELETE;
        }

        // ---------------------
        // parse renames
        // ---------------------

        // parse similarity of rename / move operation 
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_RENAME_SIMILARITY_INDEX )) {
            String similarityLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseRenameSimilarityToFileChangeSet( similarityLine, currentFileChangeSet );
            currentFileChangeSet.fileAction = ScmFileAction.FILEACTION_RENAME;
        }

        // parse from name / from directory
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_RENAME_FROM )) {
            String renameFromLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseRenameFrom( renameFromLine, currentFileChangeSet );
        }

        // parse to name / to directory
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_RENAME_TO )) {
            String renameToLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseRenameTo( renameToLine, currentFileChangeSet );
        }

        // ----------------------------
        // index line... / binary files
        // ----------------------------

        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_INDEX_IDENTIFIER )) {
            String currentIndexLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseIndexLineToFileChangeSet( currentIndexLine, currentFileChangeSet );
            if (ScmFileAction.FILEACTION_UNKNOWN.equals( currentFileChangeSet.fileAction )) {
                currentFileChangeSet.fileAction = ScmFileAction.FILEACTION_MODIFY;
            }
        }

        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_BINARY_FILES_IDENTIFIER )) {
            String binaryFileInfoLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseBinaryFileInfoLine( binaryFileInfoLine, currentFileChangeSet );
        }

        // ----------------------------
        // left and right file path
        // ----------------------------
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_LEFT_FILEPATH_IDENTIFIER )) {
            String leftFilePathLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseLeftFilePath( leftFilePathLine, currentFileChangeSet );
        }

        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER )) {
            String rightFilePathLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseRightFilePath( rightFilePathLine, currentFileChangeSet );
        }

        if (!lineLexer.hasNextLine()) {
            return currentFileChangeSet;
        }

        if (currentFileChangeSet.isBinaryFile) {
            return currentFileChangeSet;
        }

        // content changeset identifier is @@ at start
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_CONTENT_CHANGESET_IDENTIFIER )) {

            // while a first menge of a content changeset is found, we will trigger parseContentChangeSetEntry
            while (lineLexer.hasNextLine() && lineLexer.peekCurrentLine().startsWith( GIT_DIFF_CONTENT_CHANGESET_IDENTIFIER )) {
                parseContentChangeSetEntry( lineLexer, currentFileChangeSet.fileContentChangeSet::add );
            }
        }

        return currentFileChangeSet;
    }

    private void parseContentChangeSetEntry( GitScmLineBasedLexer lineLexer, Consumer<ScmFileContentChangeSet> contentChangeSetConsumer ) {
        // this is the parsing of a single
        ScmFileContentChangeSet contentChangeset = new ScmFileContentChangeSet();

        // double check in case we change the outer parser.
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_CONTENT_CHANGESET_IDENTIFIER )) {
            String contentChangeSetLineInfo = lineLexer.consumeCurrentLine();
            ContentChangeSetParsers.parseLineInfoIntoContentChangeSet( contentChangeSetLineInfo, contentChangeset );
        }

        // we must stop at end of input, start at new file info 
        while (lineLexer.hasNextLine() && !lineLexer.peekCurrentLine().startsWith( GIT_DIFF_FILENAMEINFO_IDENTIFIER )
                        && !lineLexer.peekCurrentLine().startsWith( GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER )
                        && !lineLexer.peekCurrentLine().startsWith( GIT_DIFF_CONTENT_CHANGESET_IDENTIFIER )) {

            String currentLine = lineLexer.consumeCurrentLine();
            if (currentLine.startsWith( GIT_DIFF_NO_NEWLINE_AT_END_OF_FILE_INDICATOR )) {
                //intentionally left blank;
            }
            else if (GIT_DIFF_EMPTY_LINE.equals( currentLine )) {
                // actually we are done with this file.
                break;
            }
            else {
                contentChangeset.unifiedDiffLines.add( toScmDiffLine( currentLine ) );
            }
        }

        contentChangeSetConsumer.accept( contentChangeset );
    }

    private ScmDiffLine toScmDiffLine( String currentLine ) {
        String lineContent = currentLine.length() > 0 ? currentLine.substring( 1 ) : currentLine;

        return new ScmDiffLine( ScmDiffLineType.toType( currentLine ), lineContent );
    }

}
