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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.mindscan.futuresqr.domain.model.changeset.FSqrFileChangeSet;
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
        FSqrRevisionFullChangeSet squashedDiff = calculateSquashedDiffv1( intermediateRevisions, selectedRevisions );

        return squashedDiff;
    }

    private FSqrRevisionFullChangeSet calculateSquashedDiffv1( List<FSqrRevisionFullChangeSet> intermediateRevisions, List<String> selectedRevisions ) {
        // intermediateRevisions contains all revisions in newest to oldest order
        boolean isFirstInitialized = false;

        FSqrRevisionFullChangeSet squashedDiff = new FSqrRevisionFullChangeSet();

        // MAYBE?: filterRevisions are those which are part of the review (usually codeReview.getRevisions)

        // we will create a list of paths, which are part of the touchedFiles in the review
        // we want to figure out, which files are touched in filtreRevisions, and selectedRevisions
        Collection<String> filesOfInterestInSelection = collectFilesForSelectedRevisions( intermediateRevisions, selectedRevisions );

        Map<String, FSqrFileChangeSet> pathToFileChangeSetMap = new HashMap<>();

        for (FSqrRevisionFullChangeSet fullChangeSet : intermediateRevisions) {
            // if the intermediate revision is part of the selected revisions, then we actually must process this change set
            if (selectedRevisions.contains( fullChangeSet.getRevisionId() )) {
                if (!isFirstInitialized) {
                    // initialize squashed diff with latest selected revision
                    squashedDiff.setRevisionId( fullChangeSet.getRevisionId() );
                    isFirstInitialized = true;
                }

                // TODO update squashedDiff with diffs of fullChangeSet, using some kind of include strategy...

                // because this is 
                for (FSqrFileChangeSet changeSet : fullChangeSet.getFileChangeSet()) {
                    String toPath = changeSet.getToPath();
                    if (!pathToFileChangeSetMap.containsKey( toPath )) {
                        // create a copy file changeset
                        FSqrFileChangeSet newFileChangeset = new FSqrFileChangeSet( changeSet );

                        // make it available in case we need to patch it later.
                        pathToFileChangeSetMap.put( toPath, newFileChangeset );

                        // provide this filechangeset in squashed diff.
                        squashedDiff.addFileChangeSet( newFileChangeset );
                    }
                    else {
                        FSqrFileChangeSet targetFileChangeSet = pathToFileChangeSetMap.get( toPath );
                        // TODO: squash changeSet into targetFileChangeSet

                        // check if there were in between versions, then we need to fix line numbers first before
                        // full squash.

                        // squash different aspects... renames, fileactions etc.
                    }
                }

                // we must add this revision to the squashedDiff
                // check if this is the most recent revision -> if so, we must initialize squashedDiff

                // now calculate for each file a new 
                // calculate the diffs which must be actively ignored
            }
            else {
                if (isFirstInitialized) {
                    // TODO update squashedDiff with diffs of fullchangeset, using some kind of ignore strategy
                    // maybe we want to update the line numbers, such that we can match other lines....?
                }
            }
        }

        // TODO: another strategy would be to now filter all fullchangesets according to the files and group by revision from newest to oldest.
        // not sure right now, this is a bit of a brainfuck...

        // maybe a n square algorithm, for each file separately, and then use a merger for algorithm...

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

    private FSqrRevisionFullChangeSet calculateSquashedDiffv2( List<FSqrRevisionFullChangeSet> intermediateRevisions, List<String> selectedRevisions ) {
        // TODO different idea,
        // for each touched file
        // figure out first touched revision and then add this
        // then figure out, if skipped revisions exist
        // then figure out if more revsions in selected revisions
        // combine ignored
        // combine selected

        return null;
    }

}
