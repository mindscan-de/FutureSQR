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

/**
 * search key: ( projectId:string , revisionId:string ) -> CodeReviewId:string
 */
public class InMemoryCacheRevisionToCodeReviewIdTableImpl {

    private Map<String, Map<String, String>> projectIdRevisionIdToCodeReviewIdCache;

    /**
     * 
     */
    public InMemoryCacheRevisionToCodeReviewIdTableImpl() {
        this.projectIdRevisionIdToCodeReviewIdCache = new HashMap<>();
    }

    public boolean isCached( String projectId, String revisionId ) {
        if (isKnownProjectId( projectId )) {
            return projectIdRevisionIdToCodeReviewIdCache.get( projectId ).containsKey( revisionId );
        }
        return false;
    }

    private boolean isKnownProjectId( String projectId ) {
        return projectIdRevisionIdToCodeReviewIdCache.containsKey( projectId );
    }

    public String getCodeReviewId( String projectId, String revisionId ) {
        if (isCached( projectId, revisionId )) {
            return projectIdRevisionIdToCodeReviewIdCache.get( projectId ).get( revisionId );
        }
        return "";
    }

    public String getCodeReviewIdOrComputeIfAbsent( String projectId, String revisionId, BiFunction<String, String, String> codeReviewIdLoader ) {
        if (isCached( projectId, revisionId )) {
            return projectIdRevisionIdToCodeReviewIdCache.get( projectId ).get( revisionId );
        }

        if (codeReviewIdLoader == null) {
            return "";
        }

        String codeReviewId = codeReviewIdLoader.apply( projectId, revisionId );
        putCodeReviewId( projectId, revisionId, codeReviewId );

        return (codeReviewId == null) ? "" : codeReviewId;
    }

    public void putCodeReviewId( String projectId, String revisionId, String codeReviewId ) {
        if (codeReviewId == null) {
            return;
        }

        // TODO: should we keep/cache empty association?
        if (codeReviewId.isEmpty()) {
            return;
        }

        getOrCreateProjectToRevisionIdMap( projectId ).put( revisionId, codeReviewId );
    }

    public void removeCodeReviewId( String projectId, String revisionId ) {
        getOrCreateProjectToRevisionIdMap( projectId ).remove( revisionId );
    }

    private Map<String, String> getOrCreateProjectToRevisionIdMap( String projectId ) {
        return projectIdRevisionIdToCodeReviewIdCache.computeIfAbsent( projectId, pk -> new HashMap<String, String>() );
    }

}
