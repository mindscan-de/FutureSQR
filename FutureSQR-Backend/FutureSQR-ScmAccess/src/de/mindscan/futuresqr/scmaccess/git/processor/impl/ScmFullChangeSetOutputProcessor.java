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
        // 
        String[] lines = string.split( "\\R" );
        // actually we have to provide a  line scanner....

        for (String line : lines) {
            if (line.startsWith( "diff --git " )) {
                // for each file file entry
                parseFileChangeSetEntry( line, fileChangeSetConsumer );
            }
        }

        // firstmenge for file entry will trigger parseFileChangeSetEntry
    }

    private void parseFileChangeSetEntry( String currentLine, Consumer<ScmFileChangeSet> fileChangeSetConsumer ) {
        // TOOD: binary mode is false;

        // create a new file entry
        ScmFileChangeSet fileChangeSet = new ScmFileChangeSet();
        fileChangeSet.lazy_diff_line = currentLine;

        // while a first menge of a content changeset is found, we will trigger parseContentChangeSetEntry
        parseContentChangeSetEntry( fileChangeSet.fileContentChangeSet::add );

        // end of stream or
        // first menge of a file changeset will finish scanning this file entry 

        // result is a file entry in the ScmFullChangeSet
        fileChangeSetConsumer.accept( fileChangeSet );
    }

    private void parseContentChangeSetEntry( Consumer<ScmFileContentChangeSet> contentChangeSetConsumer ) {
        // this is the parsing of a single
        ScmFileContentChangeSet contentChangeset = new ScmFileContentChangeSet();

        contentChangeSetConsumer.accept( contentChangeset );
    }

}
