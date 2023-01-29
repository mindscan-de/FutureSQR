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
    private static final String GIT_DIFF_FILENAMEINFO_IDENTIFIER_TRIMMED = GIT_DIFF_FILENAMEINFO_IDENTIFIER.trim();

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

        // TODO: analyze the standard changesets, UTF_8 ...
        // TODO: Actually each file can have it's own encoding, such that we must provide a scanner with different charset modes
        // this needs to be fixed longer term.
        parseFullChangeSet( new String( output.getProcessOutput(), StandardCharsets.UTF_8 ), scmFullChangeSet.fileChangeSet::add );

        return scmFullChangeSet;
    }

    private void parseFullChangeSet( String string, Consumer<ScmFileChangeSet> fileChangeSetConsumer ) {
        // TODO: Actually the split tokens should not be consumed from the string
        //       can to this later.
        GitScmLineBasedLexer lineLexer = new GitScmLineBasedLexer( string.split( "\\R" ) );

        while (lineLexer.hasNextLine()) {
            String currentLine = lineLexer.peekCurrentLine();

            // peek at current line
            if (currentLine.startsWith( GIT_DIFF_FILENAMEINFO_IDENTIFIER )) {
                // for each file file entry
                parseFileChangeSetEntry( lineLexer, fileChangeSetConsumer );
            }
            else {
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
        // TODO: parse this file name info identifier, such that this info is in the currentFileChangeSet
        currentFileChangeSet.lazy_diff_line = lineLexer.consumeCurrentLine();

        // while a first menge of a content changeset is found, we will trigger parseContentChangeSetEntry
        parseContentChangeSetEntry( currentFileChangeSet.fileContentChangeSet::add );

        // end of stream or
        // first menge of a file changeset will finish scanning this file entry 

        // result is a file entry in the ScmFullChangeSet
        fileChangeSetConsumer.accept( currentFileChangeSet );
    }

    private void parseContentChangeSetEntry( Consumer<ScmFileContentChangeSet> contentChangeSetConsumer ) {
        // this is the parsing of a single
        ScmFileContentChangeSet contentChangeset = new ScmFileContentChangeSet();

        contentChangeSetConsumer.accept( contentChangeset );
    }

}
