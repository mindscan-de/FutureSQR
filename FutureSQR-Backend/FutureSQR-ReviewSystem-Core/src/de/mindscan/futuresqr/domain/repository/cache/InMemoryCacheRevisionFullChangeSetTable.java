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
package de.mindscan.futuresqr.domain.repository.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;

/**
 * 
 */
public class InMemoryCacheRevisionFullChangeSetTable {

    private Map<String, Map<String, FSqrRevisionFullChangeSet>> projectIdRevisionIdToRevisionFullChangeSetCache;

    /**
     * 
     */
    public InMemoryCacheRevisionFullChangeSetTable() {
        this.projectIdRevisionIdToRevisionFullChangeSetCache = new HashMap<>();
    }

    public boolean isCached( String projectId, String revisionId ) {
        if (isKnownProjectId( projectId )) {
            return projectIdRevisionIdToRevisionFullChangeSetCache.get( projectId ).containsKey( revisionId );
        }

        return false;
    }

    private boolean isKnownProjectId( String projectId ) {
        return projectIdRevisionIdToRevisionFullChangeSetCache.containsKey( projectId );
    }

    public FSqrRevisionFullChangeSet getFSqrRevisionFullChangeSetOrComputeIfAbsent( String projectId, String revisionId,
                    BiFunction<String, String, FSqrRevisionFullChangeSet> changesetLoader ) {

        if (isCached( projectId, revisionId )) {
            return projectIdRevisionIdToRevisionFullChangeSetCache.get( projectId ).get( revisionId );
        }

        if (changesetLoader == null) {
            return null;
        }

        FSqrRevisionFullChangeSet fullchangeset = changesetLoader.apply( projectId, revisionId );

        if (fullchangeset != null) {
            putFSqrRevisionFullChangeSet( projectId, revisionId, fullchangeset );
        }
        else {
            // TODO: this (project x revision) doesnt seem to exist (despite loading)
        }

        return fullchangeset;
    }

    public void putFSqrRevisionFullChangeSet( String projectId, String revisionId, FSqrRevisionFullChangeSet fullchangeset ) {
        if (fullchangeset == null) {
            return;
        }

        // TODO: do we need moew checks.

        // insert the revision
        getOrCreateProjectToRevisionIdMap( projectId ).put( revisionId, fullchangeset );
    }

    private Map<String, FSqrRevisionFullChangeSet> getOrCreateProjectToRevisionIdMap( String projectId ) {
        return projectIdRevisionIdToRevisionFullChangeSetCache.computeIfAbsent( projectId, pk -> new HashMap<String, FSqrRevisionFullChangeSet>() );
    }

}
