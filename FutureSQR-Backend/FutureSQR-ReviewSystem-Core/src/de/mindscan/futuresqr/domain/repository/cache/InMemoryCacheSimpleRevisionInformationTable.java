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

import de.mindscan.futuresqr.domain.model.FSqrRevision;

/**
 * 
 */
public class InMemoryCacheSimpleRevisionInformationTable {

    private Map<String, Map<String, FSqrRevision>> projectIdRevisionIdToRevisionCaache;

    /**
     * 
     */
    public InMemoryCacheSimpleRevisionInformationTable() {
        this.projectIdRevisionIdToRevisionCaache = new HashMap<>();
    }

    public boolean isCached( String projectId, String revisionId ) {
        if (isKnownProjectId( projectId )) {
            return projectIdRevisionIdToRevisionCaache.get( projectId ).containsKey( revisionId );
        }

        return false;
    }

    private boolean isKnownProjectId( String projectId ) {
        return projectIdRevisionIdToRevisionCaache.containsKey( projectId );
    }

    public FSqrRevision getFSqrRevisionOrComputeIfAbsent( String projectId, String revisionId, BiFunction<String, String, FSqrRevision> revisionInfoLoader ) {
        if (isCached( projectId, revisionId )) {
            return projectIdRevisionIdToRevisionCaache.get( projectId ).get( revisionId );
        }

        if (revisionInfoLoader == null) {
            return null;
        }

        FSqrRevision revision = revisionInfoLoader.apply( projectId, revisionId );

        if (revision != null) {
            putFSqrRevision( projectId, revisionId, revision );
        }
        else {
            // TODO: this {project x revision} doesn't seem to exist.
        }

        return revision;
    }

    public void putFSqrRevision( String projectId, String revisionId, FSqrRevision revision ) {
        if (revision == null) {
            return;
        }

        // TODO: do we need more checks?

        // insert the revision.
        getOrCreateProjectToRevisionIdMap( projectId ).put( revisionId, revision );
    }

    private Map<String, FSqrRevision> getOrCreateProjectToRevisionIdMap( String projectId ) {
        return projectIdRevisionIdToRevisionCaache.computeIfAbsent( projectId, pk -> new HashMap<String, FSqrRevision>() );
    }
}
