/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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
import java.util.function.Consumer;

import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutput;
import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutputProcessor;
import de.mindscan.futuresqr.scmaccess.git.GitCommand;
import de.mindscan.futuresqr.scmaccess.git.command.GitCommandWithRevisionId;
import de.mindscan.futuresqr.scmaccess.types.ScmSingleRevisionFileChangeList;

/**
 * 
 */
public class ScmSingleRevisionFileChangeListOutputProcessor implements GitCLICommandOutputProcessor<ScmSingleRevisionFileChangeList> {

    private final static String ASCII_RECORD_SEPARATOR_REGEX = "\\x1e";
    private final static String ASCII_UNIT_SEPARATOR_REGEX = "\\x1f";

    /**
     * 
     */
    public ScmSingleRevisionFileChangeListOutputProcessor() {
        // intentionally left blank
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmSingleRevisionFileChangeList transform( GitCLICommandOutput output ) {
        ScmSingleRevisionFileChangeList fileChangeList = new ScmSingleRevisionFileChangeList();

        // TODO: remove me after implementation - collect file change info which is new line separated, then tab separated. 
        System.out.println( new String( output.getProcessOutput() ) );

        fileChangeList.scmRepository = output.getRepository();
        fileChangeList.fileChangeInformation = new ArrayList<>();

        // first line contains some more commit details... do we need them?
        // revisionId
        // committer
        // relDate

        // use the recordseparator \\x1e to split first line from fileinfo
        String processOutput = new String( output.getProcessOutput(), StandardCharsets.UTF_8 );
        String[] processOutputSplitted = processOutput.split( ASCII_RECORD_SEPARATOR_REGEX );

        if (processOutputSplitted.length != 2) {
            throw new RuntimeException( "Unexpected Format found." );
        }

        // commit details.
        parseCommitDetails( processOutputSplitted[0], fileChangeList );

        // fileChangeList.fileChangeInformation
        parseFileListDetails( processOutputSplitted[1], fileChangeList.fileChangeInformation::add );

        GitCommand gitCommand = output.getCommand();

        if (gitCommand instanceof GitCommandWithRevisionId) {
            fileChangeList.revisionId = ((GitCommandWithRevisionId) gitCommand).getRevisionId();
        }

        return fileChangeList;
    }

    private void parseCommitDetails( String string, ScmSingleRevisionFileChangeList target ) {
        // e.g.
        // 8485a4abffce885376bebb7d1d788a0b10aa9ff6<US>mindscan-de<US>5 weeks ago<RS>
        // split by unit separator and assign to correct fields.
        String[] splitted = string.trim().split( ASCII_UNIT_SEPARATOR_REGEX );

        target.revisionId = splitted[0];
        // authorname = splitted[1]
        // reldate    = splitted[2]
    }

    private void parseFileListDetails( String string, Consumer<String[]> collector ) {
        String[] fileRecords = string.trim().split( "\\R" );

        // for each line separate by tab, or more sophisticated by first element in line and parse accordingly. 
        for (String fileRecordLine : fileRecords) {
            String[] record = fileRecordLine.trim().split( "\t" );
            collector.accept( record );
        }
    }
}
