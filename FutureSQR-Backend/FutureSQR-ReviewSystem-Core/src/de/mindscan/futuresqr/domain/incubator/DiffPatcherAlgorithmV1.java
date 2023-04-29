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
package de.mindscan.futuresqr.domain.incubator;

import java.util.ArrayList;
import java.util.List;

import de.mindscan.futuresqr.domain.model.changeset.FSqrFileContentChangeSet;

/**
 * 
 */
public class DiffPatcherAlgorithmV1 {

    // TODO: patch... sort... merge adjacent in case they touch or they are slightly overlayed into one single truth.

    // TDD this?
    public List<FSqrFileContentChangeSet> patch( FSqrFileContentChangeSet left, FSqrFileContentChangeSet right ) {
        ArrayList<FSqrFileContentChangeSet> result = new ArrayList<>();

        if (!isPatchCollision( left, right )) {
            // add both to results, according to the order of both ordered by left element by right and right element by left. 
            // figure out which start values for the line numbers are the truth, and whether we have to patch it.

            return result;
        }

        return result;
    }

    /**
     * This method calculates, whether the left and right block collide, in terms of the join left 
     * --&gt; ? &lt;-- right.
     * 
     * @param leftCCS
     * @param rightCCS
     * @return
     */
    public boolean isPatchCollision( FSqrFileContentChangeSet leftCCS, FSqrFileContentChangeSet rightCCS ) {
        // if left block is completely before right block, they don't collide
        if ((leftCCS.getDiffRightLineCountStart() - 1 + leftCCS.getDiffRightLineCountDelta()) < rightCCS.getDiffLeftLineCountStart()) {
            return false;
        }

        if ((rightCCS.getDiffLeftLineCountStart() - 1 + rightCCS.getDiffLeftLineCountDelta()) < leftCCS.getDiffRightLineCountStart()) {
            return false;
        }

        return true;
    }
}
