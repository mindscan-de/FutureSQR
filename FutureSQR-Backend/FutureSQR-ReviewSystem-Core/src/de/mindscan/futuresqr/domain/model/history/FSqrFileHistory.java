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
package de.mindscan.futuresqr.domain.model.history;

import java.util.ArrayList;
import java.util.List;

import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.scmaccess.types.ScmFileHistory;

/**
 * This is basically the same as FSqrScmHistory, but the filePath is a certain file, whereas FSqrScmHistory has an
 * implicit filepath of "/" (root) - actually master or main or trunk
 */
public class FSqrFileHistory {
    private String filePath = "";
    private List<FSqrRevision> revisions = new ArrayList<>();

    private boolean hasLimit = false;
    private int limitValue = -1;

    public FSqrFileHistory() {
        // intentionally left blank
    }

    public FSqrFileHistory( ScmFileHistory history ) {
        this.filePath = history.filePath;
        history.revisions.stream().forEach( x -> this.revisions.add( new FSqrRevision( x ) ) );

        // TODO: we need to update the file revisions list with the info, whether they have a review already or not. 

        // TODO füllen haslimit und limit value.
    }

    public int getLimitValue() {
        return limitValue;
    }

    public boolean hasLimit() {
        return hasLimit;
    }

    public String getFilePath() {
        return filePath;
    }

    public List<FSqrRevision> getRevisions() {
        return revisions;
    }
}
