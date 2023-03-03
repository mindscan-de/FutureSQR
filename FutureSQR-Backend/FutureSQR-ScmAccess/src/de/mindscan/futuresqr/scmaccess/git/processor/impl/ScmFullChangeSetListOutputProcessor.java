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
import de.mindscan.futuresqr.scmaccess.types.ScmFileChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContentChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;

/**
 * 
 */
public class ScmFullChangeSetListOutputProcessor implements GitCLICommandOutputProcessor<List<ScmFullChangeSet>> {

    private static final String GIT_DIFF_FILENAMEINFO_IDENTIFIER = "diff --git ";
    private static final String GIT_DIFF_NEW_FILE_MODE = "new file mode ";
    private static final String GIT_DIFF_DELETED_FILE_MODE = "deleted file mode";

    private static final String GIT_DIFF_RENAME_SIMILARITY_INDEX = "similarity index";
    private static final String GIT_DIFF_RENAME_FROM = "rename from";
    private static final String GIT_DIFF_RENAME_TO = "rename to";

    private static final String GIT_DIFF_INDEX_IDENTIFIER = "index";
    private static final String GIT_DIFF_BINARY_FILES_IDENTIFIER = "Binary files";

    private static final String GIT_DIFF_LEFT_FILEPATH_IDENTIFIER = "---";
    private static final String GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER = "+++";

    private static final String GIT_DIFF_CONTENT_CHANGESET_IDENTIFIER = "@@ ";

    private static final String GIT_DIFF_NO_NEWLINE_AT_END_OF_FILE_INDICATOR = "\\ No newline at end of file";

    private static final String GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER = "commit ";
    private static final String GIT_DIFF_NEWCOMMIT_AUTHOR_LINE_IDENTIFIER = "Author: ";
    private static final String GIT_DIFF_NEWCOMMIT_DATE_LINE_IDENTIFIER = "Date: ";

    // TODO NEXT: two newlines are a separator for revision-Data / revision information.
    private static final String GIT_DIFF_SECTION_SPLITTER = "\n\n";

    // TODO NEXT: a new line and a space on the next followed by newline is a newline in the file.

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
        List<ScmFullChangeSet> resultList = parseFullChangeSetList( new String( output.getProcessOutput(), StandardCharsets.UTF_8 ) );

        return resultList;
    }

    private List<ScmFullChangeSet> parseFullChangeSetList( String string ) {
        List<ScmFullChangeSet> resultList = new ArrayList<>();

        // For debug purposes 
        System.out.println( string );

        // prepare line lexxer
        GitScmLineBasedLexer lineLexer = new GitScmLineBasedLexer( string.split( "\\R" ) );

        // TODO NEXT: must split to new commit, we get the diff for each single commit in between. not what I expected.....
        // TODO NEXT: maybe a list of full change sets for each revision one entry in the list.

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
        // Parse the revision, then parse file until next revision - initialize another revision continue to parse the 
        // revision

        // TODO: parse the commit + id /Author/date/message andmaybe also provide a list of full changesets .... instead of one.
        ScmFullChangeSet scmFullChangeSet = new ScmFullChangeSet();
        Consumer<ScmFileChangeSet> fileChangeSetConsumer = scmFullChangeSet.fileChangeSet::add;

        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER )) {
            // TODO parse commit identifier
            lineLexer.consumeCurrentLine();
        }

        // TODO: Author
        // TODO: DATE
        // TODO: NEWLINE
        // TODO: commitmessage (starts with 4 spaces "    ")
        // TODO: NEWLINE

        while (lineLexer.hasNextLine() && !lineLexer.peekCurrentLine().startsWith( GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER )) {
            String currentLine = lineLexer.peekCurrentLine();

            // peek at current line
            if (currentLine.startsWith( GIT_DIFF_FILENAMEINFO_IDENTIFIER )) {
                // for each file file entry
                System.out.println( "[parseFullChangeSet][p]: '" + currentLine + "'" );
                fileChangeSetConsumer.accept( parseFileChangeSetEntry( lineLexer ) );

                // TODO: 
                // TODO: reinitialize for next revision.
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
        currentFileChangeSet.isBinaryFile = false;

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
            // TODO: parse and consume this info and add info to current file change set.
            lineLexer.consumeCurrentLine();
        }
        // Parse deleted file mode
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_DELETED_FILE_MODE )) {
            // TODO: parse and consume this info and add info to current file change set.
            lineLexer.consumeCurrentLine();
        }

        // ---------------------
        // parse renames
        // ---------------------

        // parse similarity of rename / move operation 
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_RENAME_SIMILARITY_INDEX )) {
            // TODO: parse and consume this info and add info to current file change set.
            currentFileChangeSet.similarity_info_line = lineLexer.consumeCurrentLine();
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
        }

        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_BINARY_FILES_IDENTIFIER )) {
            // TODO: parse and consume this info and add info to current file change set.            
            currentFileChangeSet.isBinaryFile = true;
            currentFileChangeSet.binary_file_info_line = lineLexer.consumeCurrentLine();
        }

        // ----------------------------
        // left and right file path
        // ----------------------------
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_LEFT_FILEPATH_IDENTIFIER )) {
            // TODO: parse and consume this info and add info to current file change set.
            lineLexer.consumeCurrentLine();
        }

        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER )) {
            // TODO: parse and consume this info and add info to current file change set.
            lineLexer.consumeCurrentLine();
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
            else if (currentLine.equals( "" )) {
                // actually we are done with this file.
                break;
            }
            else {
                // TODO: it either starts with a '+', '-', ' ' this should be handled and split into a different Array, 
                // where add, remove and unchanged info is stored for each index. 
                contentChangeset.line_diff_data.add( currentLine );
            }
        }

        contentChangeSetConsumer.accept( contentChangeset );
    }

}
