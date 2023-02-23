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
import java.util.function.Consumer;

import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutput;
import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutputProcessor;
import de.mindscan.futuresqr.scmaccess.types.ScmFileChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContentChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;

/**
 * 
 */
public class ScmFullChangeSetOutputProcessor implements GitCLICommandOutputProcessor<ScmFullChangeSet> {

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

    // TODO NEXT: two newlines are a separator for revision-Data / revision information.
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
        ScmFullChangeSet scmFullChangeSet = new ScmFullChangeSet();

        // TODO: parse the commit + id /Author/date/message andmaybe also provide a list of full changesets .... instead of one.

        // TODO: analyze the standard changesets, UTF_8 ...
        // TODO: Actually each file can have it's own encoding, such that we must provide a scanner with different charset modes
        // this needs to be fixed longer term.
        parseFullChangeSet( new String( output.getProcessOutput(), StandardCharsets.UTF_8 ), scmFullChangeSet.fileChangeSet::add );

        return scmFullChangeSet;
    }

    private void parseFullChangeSet( String string, Consumer<ScmFileChangeSet> fileChangeSetConsumer ) {
        // System.out.println( string );
        // TODO NEXT: must split to new commit, we get the diff for each single commit in between. not what I expected.....
        // TODO NEXT: maybe a list of full change sets for each revision one entry in the list.

        // TODO: Actually the split tokens should not be consumed from the string
        //       can to this later.
        GitScmLineBasedLexer lineLexer = new GitScmLineBasedLexer( string.split( "\\R" ) );

        while (lineLexer.hasNextLine()) {
            String currentLine = lineLexer.peekCurrentLine();

            // peek at current line
            if (currentLine.startsWith( GIT_DIFF_FILENAMEINFO_IDENTIFIER )) {
                // for each file file entry
                System.out.println( "[parseFullChangeSet][p]: '" + currentLine + "'" );
                parseFileChangeSetEntry( lineLexer, fileChangeSetConsumer );

                // TODO: reinitialize for next revision.?
                // TODO: maybe adding the ScmFileChangeset should be part of this method? not of the parseFileChangeSetEntry.
            }
            else {
                // This will either skip some header or unparsed stuff from the inner parsers.
                System.out.println( "[parseFullChangeSet][u]: '" + currentLine + "'" );
                lineLexer.advanceToNextLine();
            }
        }

        // first menge for file entry will trigger parseFileChangeSetEntry
    }

    private void parseFileChangeSetEntry( GitScmLineBasedLexer lineLexer, Consumer<ScmFileChangeSet> fileChangeSetConsumer ) {
        // TOOD: binary mode is false;

        // create a new file entry
        ScmFileChangeSet currentFileChangeSet = new ScmFileChangeSet();

        // Parse the file info
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_FILENAMEINFO_IDENTIFIER )) {
            // TODO: parse and consume this file name info identifier, such that this info is in the currentFileChangeSet
            currentFileChangeSet.lazy_diff_line = lineLexer.consumeCurrentLine();
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
            // TODO: parse and consume this info and add info to current file change set.            
            currentFileChangeSet.lazy_index_line = lineLexer.consumeCurrentLine();
        }
        else {
            currentFileChangeSet.lazy_index_line = "(empty)";
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
            fileChangeSetConsumer.accept( currentFileChangeSet );
            return;
        }

        if (currentFileChangeSet.isBinaryFile) {
            fileChangeSetConsumer.accept( currentFileChangeSet );
            return;
        }

        // content changeset identifier is @@ at start
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_CONTENT_CHANGESET_IDENTIFIER )) {

            // while a first menge of a content changeset is found, we will trigger parseContentChangeSetEntry
            while (lineLexer.hasNextLine() && lineLexer.peekCurrentLine().startsWith( GIT_DIFF_CONTENT_CHANGESET_IDENTIFIER )) {
                parseContentChangeSetEntry( lineLexer, currentFileChangeSet.fileContentChangeSet::add );
            }
        }

        // result is a file entry in the ScmFullChangeSet
        // MAYBE we want
        fileChangeSetConsumer.accept( currentFileChangeSet );
    }

    private void parseContentChangeSetEntry( GitScmLineBasedLexer lineLexer, Consumer<ScmFileContentChangeSet> contentChangeSetConsumer ) {
        // this is the parsing of a single
        ScmFileContentChangeSet contentChangeset = new ScmFileContentChangeSet();

        // double check in case we change the outer parser.
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_CONTENT_CHANGESET_IDENTIFIER )) {
            // TODO: parse and consume this info and add info to current file change set.            
            contentChangeset.line_info = lineLexer.consumeCurrentLine();
        }

        // we must stop at end of input, start at new file info 
        while (lineLexer.hasNextLine() && !lineLexer.peekCurrentLine().startsWith( GIT_DIFF_FILENAMEINFO_IDENTIFIER )
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
