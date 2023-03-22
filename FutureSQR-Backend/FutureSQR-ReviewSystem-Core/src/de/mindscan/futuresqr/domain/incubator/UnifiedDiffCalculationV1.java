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
import de.mindscan.futuresqr.domain.model.changeset.FSqrFileContentChangeSet;
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
        Map<String, FSqrFileChangeSet> pathToHiddenFileChangeSetJournalMap = new HashMap<>();

        for (FSqrRevisionFullChangeSet fullChangeSet : intermediateRevisions) {
            if (isSelectedRevision( fullChangeSet, selectedRevisions )) {
                // process the selected revision

                if (!isFirstInitialized) {
                    // initialize squashed diff with latest selected revision
                    squashedDiff.setRevisionId( fullChangeSet.getRevisionId() );
                    isFirstInitialized = true;
                }

                // process the file change set and integrate it into the squashed diff
                for (FSqrFileChangeSet changeSet : fullChangeSet.getFileChangeSet()) {
                    // TODO: use the changeSet.fileaction to determine the correct strategy.
                    // changeSet.getFileAction();
                    // A, M, R, D

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
                        // check if there is a hidden record for the targetFileChangeset, that one wins over pathToFileChangeSetMap
                        if (pathToHiddenFileChangeSetJournalMap.containsKey( toPath )) {
                            // that copy must be pulled from the Map, such that the entry gets invalidated, and replaces the pathoFileChangesetMap
                            FSqrFileChangeSet hiddenJournalFileChangeSet = pathToHiddenFileChangeSetJournalMap.remove( toPath );

                            //TODO: remove the previous filechangeset from squashed diff, and then replace it again by the updated file change set
                            //TODO: actually we must check what kind of change it was..., maybe we can't combine them....
                            FSqrFileChangeSet previous = pathToFileChangeSetMap.put( toPath, hiddenJournalFileChangeSet );

                            // remove the previous file change set, if there was a hidden file change set
                            squashedDiff.removeFileChangeSet( previous );
                        }

                        // we continue with the pathToFileChangeSetMap, after we got rid of probable hidden records.
                        FSqrFileChangeSet targetFileChangeSet = pathToFileChangeSetMap.get( toPath );

                        // squash changeSet into targetFileChangeSet
                        FSqrFileChangeSet updatedFileChangeset = squashSelectedFileChangeSet( targetFileChangeSet, changeSet );

                        squashedDiff.addFileChangeSet( updatedFileChangeset );
                    }
                }

                // check if this is the most recent revision -> if so, we must initialize squashedDiff
                // calculate the diffs which must be actively ignored
            }
            else {
                if (!isFirstInitialized) {
                    continue;
                }

                // TODO update squashedDiff with diffs of fullchangeset, using some kind of ignore strategy
                // maybe we want to update the line numbers, such that we can match other lines....?

                for (FSqrFileChangeSet changeSet : fullChangeSet.getFileChangeSet()) {
                    // TODO: use the changeSet.fileaction to determine the correct strategy.
                    // changeSet.getFileAction();
                    // A, M, R, D

                    String toPath = changeSet.getToPath();

                    // if this file is not in filesOfInterestInSelection, we simply ignore this file change set
                    if (!filesOfInterestInSelection.contains( toPath )) {
                        continue;
                    }

                    // if there is no entry in pathToFileChangeSetMap, we simply ignore this file change set
                    // because we have no previous record for this file
                    if (!pathToFileChangeSetMap.containsKey( toPath )) {
                        continue;
                    }

                    FSqrFileChangeSet hiddenFileChangeSetRecord;
                    if (pathToHiddenFileChangeSetJournalMap.containsKey( toPath )) {
                        // if there is a hidden record, we will continue the record.
                        hiddenFileChangeSetRecord = pathToHiddenFileChangeSetJournalMap.get( toPath );
                    }
                    else {
                        // else we create a new copy of the pathToFileChangeSetMap, what we may or may not use.
                        hiddenFileChangeSetRecord = new FSqrFileChangeSet( pathToFileChangeSetMap.get( toPath ) );
                    }

                    // TODO: use an ignore strategy.
                    // fix linenumbers and such.

                    // TODO: it should be checked , whether filechangesets are joinable.
                    hiddenFileChangeSetRecord = squashHiddenFileChangeSet( hiddenFileChangeSetRecord, changeSet );

                    // TODO: then update the updated hidden record.
                    // store/save this intermediate FileChangeset copy.
                    pathToHiddenFileChangeSetJournalMap.put( toPath, hiddenFileChangeSetRecord );
                }
            }
        }

        return squashedDiff;
    }

    private FSqrFileChangeSet squashSelectedFileChangeSet( FSqrFileChangeSet targetFileChangeSet, FSqrFileChangeSet currentChangeSet ) {
        List<FSqrFileContentChangeSet> fileContentChangeSet = currentChangeSet.getFileContentChangeSet();

        // depending on the two file changesettype we now want to select the correct merge/squash strategy.
        // and update the content change sets correctly.

        // squash different aspects... renames, fileactions etc.
        // TODO: update line numbers in content changeset
        // TODO: update line modes in content changeset
        // TODO: update file action
        // TODO: update file 

        for (FSqrFileContentChangeSet contentChangeSet : fileContentChangeSet) {
            // TODO: integrate the file changes content change sets
            // use some kind or merging mechanism, if lines do not overlap, bingo, just insert at correct position.
            // this is the more complicated stuff....

            // TODO: just add this stuff.... for now..., let's see whether the whole logic works alltogether,
            //       we can improve the squashing for each of the content change sets later 
            targetFileChangeSet.addContentChangeSet( contentChangeSet );
        }

        return targetFileChangeSet;
    }

    private FSqrFileChangeSet squashHiddenFileChangeSet( FSqrFileChangeSet targetFileChangeSet, FSqrFileChangeSet currentChangeSet ) {
        return targetFileChangeSet;
    }

    private boolean isSelectedRevision( FSqrRevisionFullChangeSet fullChangeSet, List<String> selectedRevisions ) {
        return selectedRevisions.contains( fullChangeSet.getRevisionId() );
    }

    private Collection<String> collectFilesForSelectedRevisions( List<FSqrRevisionFullChangeSet> intermediateRevisions, List<String> selectedRevisions ) {
        Set<String> files = new HashSet<>();

        for (FSqrRevisionFullChangeSet changeset : intermediateRevisions) {
            // TODO: actually only add if not empty...
            changeset.getFileChangeSet().forEach( fcs -> files.add( fcs.getToPath() ) );
            changeset.getFileChangeSet().forEach( fcs -> files.add( fcs.getFromPath() ) );
        }

        return files;
    }

    // These are the interesting transitions, but we also need to distinguish, whether, it is a hidden/ignored or 
    // selected revision. Some transitions might be "impossible", but we never know, how "impossible" these are. 
    // A - ADD, M - Modified, R - Renamed/Moved, D-Deleted

    // AA => ??
    // AR => 
    // AM
    // AD => empty for file

    // RA
    // RR
    // RM
    // RD

    // MA
    // MR
    // MM
    // MD

    // DA
    // DR
    // DM
    // DD
}
