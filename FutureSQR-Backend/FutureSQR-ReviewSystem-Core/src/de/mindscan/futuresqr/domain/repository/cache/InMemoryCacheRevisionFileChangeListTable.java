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

import de.mindscan.futuresqr.domain.model.FSqrRevisionFileChangeList;

/**
 * 
 */
public class InMemoryCacheRevisionFileChangeListTable {

    private Map<String, Map<String, FSqrRevisionFileChangeList>> projectIdRevisionIdToFileChangeListCache;

    /**
     * 
     */
    public InMemoryCacheRevisionFileChangeListTable() {
        this.projectIdRevisionIdToFileChangeListCache = new HashMap<>();
    }

    public boolean isCached( String projectId, String revisionId ) {
        if (ifKnownProjectId( projectId )) {
            return projectIdRevisionIdToFileChangeListCache.get( projectId ).containsKey( revisionId );
        }

        return false;
    }

    private boolean ifKnownProjectId( String projectId ) {
        return projectIdRevisionIdToFileChangeListCache.containsKey( projectId );
    }

    public FSqrRevisionFileChangeList getFSqrRevisionFileChangeListOrComputIfAbsent( String projectId, String revisionId,
                    BiFunction<String, String, FSqrRevisionFileChangeList> fileListLoader ) {
        if (isCached( projectId, revisionId )) {
            return projectIdRevisionIdToFileChangeListCache.get( projectId ).get( revisionId );
        }

        if (fileListLoader == null) {
            return null;
        }

        FSqrRevisionFileChangeList fileChangelist = fileListLoader.apply( projectId, revisionId );

        if (fileChangelist != null) {
            putFileChangeList( projectId, revisionId, fileChangelist );
        }
        else {
            // TODO this (projectId x revisionId) doesn't seem to exist, we may buffer that negative answer for a while. 
        }

        return fileChangelist;
    }

    public void putFileChangeList( String projectId, String revisionId, FSqrRevisionFileChangeList fileChangeList ) {
        if (fileChangeList == null) {
            return;
        }

        // TODO: do we need some more checks?

        // insert the file changelist to cache
        getOrCreateProjectToRevisionIdMap( projectId ).put( revisionId, fileChangeList );
    }

    /**
     * @param projectId
     * @return
     */
    private Map<String, FSqrRevisionFileChangeList> getOrCreateProjectToRevisionIdMap( String projectId ) {
        return projectIdRevisionIdToFileChangeListCache.computeIfAbsent( projectId, pk -> new HashMap<String, FSqrRevisionFileChangeList>() );
    }
}
