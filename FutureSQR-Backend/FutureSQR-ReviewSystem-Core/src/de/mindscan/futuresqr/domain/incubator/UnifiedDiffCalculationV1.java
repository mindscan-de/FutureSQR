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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;

/**
 * 
 */
public class UnifiedDiffCalculationV1 {

    // TODO: we want to build a FullChangeSet, which can be displayed 
    public FSqrRevisionFullChangeSet squashDiffs( List<FSqrRevisionFullChangeSet> intermediateRevisions, Collection<String> filterRevisions,
                    List<String> selectedRevisions ) {

        // --------------------
        // 0 selected Revisions
        // --------------------
        if (selectedRevisions.size() == 0) {
            return new FSqrRevisionFullChangeSet();
        }

        // --------------------
        // 1 selected Revisions
        // --------------------
        if (selectedRevisions.size() == 1) {
            // actually only one revision is selected, so we can just return the correct intermediate revision from intermediate Revisions List
            String selectedRevisionId = selectedRevisions.get( 0 );
            if (selectedRevisionId == null) {
                return new FSqrRevisionFullChangeSet();
            }

            for (FSqrRevisionFullChangeSet fullChangeSet : intermediateRevisions) {
                if (selectedRevisionId.equals( fullChangeSet.getRevisionId() )) {
                    return fullChangeSet;
                }
            }

            // else - in case of unknown revision id
            return new FSqrRevisionFullChangeSet();
        }

        // -----------------------
        // many selected Revisions
        // -----------------------
        FSqrRevisionFullChangeSet squashedDiff = calculateSquashedDiff( intermediateRevisions, selectedRevisions );

        return squashedDiff;
    }

    private FSqrRevisionFullChangeSet calculateSquashedDiff( List<FSqrRevisionFullChangeSet> intermediateRevisions, List<String> selectedRevisions ) {
        // intermediateRevisions contains all revisions in newest to oldest order

        FSqrRevisionFullChangeSet squashedDiff = new FSqrRevisionFullChangeSet();
        // TODO initialize squashed diff with latest selected revision

        // filterRevisions are those which are part of the review (usually codeReview.getRevisions) (maybe we don't need this here...)

        // we will create a list of paths, which are part of the touchedFiles in the review
        // we want to figure out, which files are touched in filtreRevisions, and selectedRevisions
        Collection<String> filesOfInterestInSelection = collectFilesForSelectedRevisions( intermediateRevisions, selectedRevisions );

        for (FSqrRevisionFullChangeSet fSqrRevisionFullChangeSet : intermediateRevisions) {
            // if the intermediate revision is part of the selected revisions, then we actually must process this change set
            if (selectedRevisions.contains( fSqrRevisionFullChangeSet.getRevisionId() )) {
                // we must add this revision to the squashedDiff
                // check if this is the most recent revision -> if so, we must initialize squashedDiff

                // now calculate for each file a new 
                // calculate the diffs which must be actively ignored
            }
        }

        return intermediateRevisions.get( 0 );
    }

    private Collection<String> collectFilesForSelectedRevisions( List<FSqrRevisionFullChangeSet> intermediateRevisions, List<String> selectedRevisions ) {
        Set<String> files = new HashSet<>();

        for (FSqrRevisionFullChangeSet changeset : intermediateRevisions) {
            changeset.getFileChangeSet().forEach( fcs -> files.add( fcs.getToPath() ) );
            changeset.getFileChangeSet().forEach( fcs -> files.add( fcs.getFromPath() ) );
        }

        return files;
    }
}
