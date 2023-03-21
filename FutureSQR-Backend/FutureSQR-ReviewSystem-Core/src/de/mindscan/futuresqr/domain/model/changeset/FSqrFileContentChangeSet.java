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
package de.mindscan.futuresqr.domain.model.changeset;

import java.util.ArrayList;
import java.util.List;

import de.mindscan.futuresqr.scmaccess.types.ScmFileContentChangeSet;

/**
 * 
 */
public class FSqrFileContentChangeSet {

    private List<FSqrDiffLine> unifiedDiffData;

    private int diffLeftLineCountStart = 1;
    private int diffLeftLineCountDelta = 0;
    private int diffRightLineCountStart = 1;
    private int diffRightLineCountDelta = 0;

    public FSqrFileContentChangeSet() {
        // intentionally left blank
    }

    public FSqrFileContentChangeSet( ScmFileContentChangeSet contentChangeSet ) {
        this.unifiedDiffData = new ArrayList<>();

        contentChangeSet.unifiedDiffLines.forEach( line -> this.unifiedDiffData.add( new FSqrDiffLine( line ) ) );

        this.diffLeftLineCountDelta = contentChangeSet.diffLeftLineCountDelta;
        this.diffLeftLineCountStart = contentChangeSet.diffLeftLineCountStart;
        this.diffRightLineCountDelta = contentChangeSet.diffRightLineCountDelta;
        this.diffRightLineCountStart = contentChangeSet.diffRightLineCountStart;

    }

    public FSqrFileContentChangeSet( FSqrFileContentChangeSet other ) {
        this.unifiedDiffData = new ArrayList<>();

        other.unifiedDiffData.forEach( line -> this.unifiedDiffData.add( new FSqrDiffLine( line ) ) );

        this.diffLeftLineCountDelta = other.diffLeftLineCountDelta;
        this.diffLeftLineCountStart = other.diffLeftLineCountStart;
        this.diffRightLineCountDelta = other.diffRightLineCountDelta;
        this.diffRightLineCountStart = other.diffRightLineCountStart;
    }

    public List<FSqrDiffLine> getUnifiedDiffData() {
        return unifiedDiffData;
    }

    public int getDiffLeftLineCountDelta() {
        return diffLeftLineCountDelta;
    }

    public int getDiffLeftLineCountStart() {
        return diffLeftLineCountStart;
    }

    public int getDiffRightLineCountDelta() {
        return diffRightLineCountDelta;
    }

    public int getDiffRightLineCountStart() {
        return diffRightLineCountStart;
    }

}
