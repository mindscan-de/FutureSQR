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
package de.mindscan.futuresqr.devbackend.httpresponse;

import java.util.ArrayList;
import java.util.List;

import de.mindscan.futuresqr.domain.model.changeset.FSqrFileContentChangeSet;

/**
 * see BackendModelSingleCommitFileContentChangeSet
 */
public class OutputSingleCommitFileContentChangeSet {

    public int diffLeftLineCountDelta = -1;
    public int diffLeftLineCountStart = -1;
    public int diffRightLineCountDelta = -1;
    public int diffRightLineCountStart = -1;

    public List<String> line_diff_data = new ArrayList<>();

    public OutputSingleCommitFileContentChangeSet() {
        // intentionally left blank
    }

    public OutputSingleCommitFileContentChangeSet( FSqrFileContentChangeSet contentChangeSet ) {
        this.line_diff_data = new ArrayList<>();

        contentChangeSet.getUnifiedDiffData().forEach( uline -> line_diff_data.add( uline.asUnifiedLineContent() ) );

        this.diffLeftLineCountDelta = contentChangeSet.getDiffLeftLineCountDelta();
        this.diffLeftLineCountStart = contentChangeSet.getDiffLeftLineCountStart();
        this.diffRightLineCountDelta = contentChangeSet.getDiffRightLineCountDelta();
        this.diffRightLineCountStart = contentChangeSet.getDiffRightLineCountStart();
    }

}
