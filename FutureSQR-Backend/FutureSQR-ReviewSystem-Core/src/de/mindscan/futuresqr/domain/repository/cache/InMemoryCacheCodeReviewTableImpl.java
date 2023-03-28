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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import de.mindscan.futuresqr.domain.model.FSqrCodeReview;

/**
 * search key: ( projectId:string , reviewId:string ) -> codereview:FSqrCodeReview
 */
public class InMemoryCacheCodeReviewTableImpl {

    // TODO: also LRU cache implementation, such that the number of reviews in memory
    // are limited, and those which aren't used are rotated out from cache.

    private Map<String, Map<String, FSqrCodeReview>> projectIdReviewIdToCodeReviewCache;

    /**
     * 
     */
    public InMemoryCacheCodeReviewTableImpl() {
        this.projectIdReviewIdToCodeReviewCache = new HashMap<>();
    }

    public boolean isCached( String projectId, String reviewId ) {
        if (isKnownProjectId( projectId )) {
            return projectIdReviewIdToCodeReviewCache.get( projectId ).containsKey( reviewId );
        }
        return false;
    }

    private boolean isKnownProjectId( String projectId ) {
        return projectIdReviewIdToCodeReviewCache.containsKey( projectId );
    }

    public FSqrCodeReview getCodeReview( String projectId, String reviewId ) {
        if (isCached( projectId, reviewId )) {
            return projectIdReviewIdToCodeReviewCache.get( projectId ).get( reviewId );
        }
        return null;
    }

    public FSqrCodeReview getCodeReviewOrComputeIfAbsent( String projectId, String reviewId, BiFunction<String, String, FSqrCodeReview> computeCodeReview ) {
        if (isCached( projectId, reviewId )) {
            return getCodeReview( projectId, reviewId );
        }

        FSqrCodeReview codeReview = computeCodeReview.apply( projectId, reviewId );
        putCodeReview( projectId, reviewId, codeReview );
        return codeReview;
    }

    public List<FSqrCodeReview> filterCodeReviewsByProject( String projectId, Predicate<FSqrCodeReview> predicate ) {
        ArrayList<FSqrCodeReview> resultList = new ArrayList<>();

        if (isKnownProjectId( projectId )) {
            getOrCreateProjectToCodeReviewMap( projectId ).values().stream().filter( predicate ).forEach( r -> resultList.add( r ) );
        }

        return resultList;
    }

    public void putCodeReview( String projectId, String reviewId, FSqrCodeReview codeReview ) {
        getOrCreateProjectToCodeReviewMap( projectId ).put( reviewId, codeReview );
    }

    private Map<String, FSqrCodeReview> getOrCreateProjectToCodeReviewMap( String projectId ) {
        return projectIdReviewIdToCodeReviewCache.computeIfAbsent( projectId, pk -> new HashMap<String, FSqrCodeReview>() );
    }

}
