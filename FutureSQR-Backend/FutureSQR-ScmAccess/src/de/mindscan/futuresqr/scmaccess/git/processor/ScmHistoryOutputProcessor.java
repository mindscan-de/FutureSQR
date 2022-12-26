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
package de.mindscan.futuresqr.scmaccess.git.processor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutput;
import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutputProcessor;
import de.mindscan.futuresqr.scmaccess.git.GitCommand;
import de.mindscan.futuresqr.scmaccess.git.command.GitCommandWithLimit;
import de.mindscan.futuresqr.scmaccess.types.ScmBasicRevisionInformation;
import de.mindscan.futuresqr.scmaccess.types.ScmHistory;

/**
 * 
 */
public class ScmHistoryOutputProcessor implements GitCLICommandOutputProcessor<ScmHistory> {

    /**
     * 
     */
    public ScmHistoryOutputProcessor() {
        // intentionally left blank.
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmHistory transform( GitCLICommandOutput output ) {
        ScmHistory scmHistory = new ScmHistory();
        List<ScmBasicRevisionInformation> revisions = new ArrayList<>();

        parseRecordAndUnitSeparator( new String( output.getProcessOutput(), StandardCharsets.UTF_8 ), revisions::add );

        scmHistory.scmRepository = output.getRepository();
        scmHistory.revisions = revisions;

        // TODO: read

        GitCommand gitCommand = output.getCommand();

        if (gitCommand instanceof GitCommandWithLimit) {
            scmHistory.hasLimit = true;
            scmHistory.limit = ((GitCommandWithLimit) gitCommand).getLimit();
        }

        return scmHistory;
    }

    private void parseRecordAndUnitSeparator( String inputstring, Consumer<ScmBasicRevisionInformation> consumer ) {
        String[] records = inputstring.split( "\\x1e" );

        for (int i = 0; i < records.length; i++) {
            // actually we want a ltrim only...
            String[] elements = records[i].trim().split( "\\x1f" );
            consumer.accept( buildBasicRevisionInfo( elements ) );
        }
    }

    private ScmBasicRevisionInformation buildBasicRevisionInfo( String[] record ) {
        ScmBasicRevisionInformation result = new ScmBasicRevisionInformation();

        result.shortRevisionId = record[0];
        result.revisionId = record[1];
        result.authorName = record[2];
        result.authorId = record[3];
        result.date = record[4];
        result.shortDate = record[5];
        result.relDate = record[6];
        result.parentIds = new ArrayList<>();
        // TODO needs to be split
        result.parentIds.add( record[7] );
        result.shortParentIds = new ArrayList<>();
        // TODO needs to be split
        result.shortParentIds.add( record[8] );
        result.message = record[9];

        return result;
    }

}
