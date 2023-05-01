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
import de.mindscan.futuresqr.scmaccess.types.ScmFileChangeMode;
import de.mindscan.futuresqr.scmaccess.types.ScmFileChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContentChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;

/**
 * 
 */
public class ScmFullChangeSetOutputProcessor implements GitCLICommandOutputProcessor<ScmFullChangeSet> {

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

    // TODO NEXT: a new line and a space on the next followed by newline is a newline in the file.

    /**
     * 
     */
    public ScmFullChangeSetOutputProcessor() {
        // intentionally left blank.
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmFullChangeSet transform( GitCLICommandOutput output ) {
        // TODO: parse the commit + id /Author/date/message andmaybe also provide a list of full changesets .... instead of one.

        // TODO: analyze the standard changesets, UTF_8 ...
        // TODO: Actually each file can have it's own encoding, such that we must provide a scanner with different charset modes
        // this needs to be fixed longer term.
        ScmFullChangeSet result = parseFullChangeSet( new String( output.getProcessOutput(), StandardCharsets.UTF_8 ) );

        return result;
    }

    private ScmFullChangeSet parseFullChangeSet( String string ) {

        ScmFullChangeSet scmFullChangeSet = new ScmFullChangeSet();
        Consumer<ScmFileChangeSet> fileChangeSetConsumer = scmFullChangeSet.fileChangeSet::add;

        // TODO: Actually the split tokens should not be consumed from the string
        //       can to this later.
        GitScmLineBasedLexer lineLexer = new GitScmLineBasedLexer( string.split( "\\R" ) );

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
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_NEWCOMMIT_DATE_LINE_IDENTIFIER )) {
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
                    // remove 4 space indent
                    commitLines.add( commentline.substring( GIT_DIFF_NEWCOMMIT_FOUR_SAPCE_INDENT.length() ) );
                }
            }

            // consume empty line
            lineLexer.consumeCurrentLine();

            String commitMessage = String.join( "\n", commitLines );
            FullChangeSetParsers.parseCommitMessageToFullChangeSet( commitMessage, scmFullChangeSet );
        }

        while (lineLexer.hasNextLine()) {
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
            currentFileChangeSet.fileAction = ScmFileChangeMode.FILEMODE_ADD;
        }

        // Parse deleted file mode
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_DELETED_FILE_MODE )) {
            // TODO: parse and consume this info and add info to current file change set.
            String deleteFileModeLine = lineLexer.consumeCurrentLine();
            currentFileChangeSet.fileAction = ScmFileChangeMode.FILEMODE_DELETE;
        }

        // ---------------------
        // parse renames
        // ---------------------

        // parse similarity of rename / move operation 
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_RENAME_SIMILARITY_INDEX )) {
            String similarityLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseRenameSimilarityToFileChangeSet( similarityLine, currentFileChangeSet );
            currentFileChangeSet.fileAction = ScmFileChangeMode.FILEMODE_RENAME;
        }

        // parse from name / from directory
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_RENAME_FROM )) {
            // TODO: parse and consume this info and add info to current file change set.
            currentFileChangeSet.renamed_from = lineLexer.consumeCurrentLine();
        }

        // parse to name / to directory
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_RENAME_TO )) {
            // TODO: parse and consume this info and add info to current file change set.
            currentFileChangeSet.renamed_to = lineLexer.consumeCurrentLine();
        }

        // ----------------------------
        // index line... / binary files
        // ----------------------------

        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_INDEX_IDENTIFIER )) {
            String currentIndexLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseIndexLineToFileChangeSet( currentIndexLine, currentFileChangeSet );
            if (ScmFileChangeMode.FILEMODE_UNKNOWN.equals( currentFileChangeSet.fileAction )) {
                currentFileChangeSet.fileAction = ScmFileChangeMode.FILEMODE_MODIFY;
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
