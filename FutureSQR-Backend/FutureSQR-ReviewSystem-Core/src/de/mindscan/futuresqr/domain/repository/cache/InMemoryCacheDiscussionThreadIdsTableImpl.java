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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * search key: ( projectId:string , reviewId:string ) -> ( threadIds:ArrayList<String> )
 */
public class InMemoryCacheDiscussionThreadIdsTableImpl {

    private Map<String, Map<String, Set<String>>> projectIdReviewIdToThreadIds;

    /**
     * 
     */
    public InMemoryCacheDiscussionThreadIdsTableImpl() {
        this.projectIdReviewIdToThreadIds = new HashMap<>();
    }

    public boolean isCached( String projectId, String reviewId ) {
        if (isKnownProjectId( projectId )) {
            return projectIdReviewIdToThreadIds.get( projectId ).containsKey( reviewId );
        }
        return false;
    }

    boolean hasDiscussionThreadUUID( String projectId, String reviewId, String discussionThreadUUID ) {
        if (!isCached( projectId, reviewId )) {
            return false;
        }

        return getDiscussionThreadUUIDs( projectId, reviewId ).contains( discussionThreadUUID );
    }

    public boolean hasDiscussionThreadUUID( String projectId, String reviewId, String discussionThreadUUID,
                    BiFunction<String, String, Collection<String>> loader ) {
        return getDiscussionThreadUUIDs( projectId, reviewId, loader ).contains( discussionThreadUUID );
    }

    private boolean isKnownProjectId( String projectId ) {
        return projectIdReviewIdToThreadIds.containsKey( projectId );
    }

    Collection<String> getDiscussionThreadUUIDs( String projectId, String reviewId ) {
        if (isCached( projectId, reviewId )) {
            return projectIdReviewIdToThreadIds.get( projectId ).get( reviewId );
        }
        return new ArrayList<>();
    }

    public Collection<String> getDiscussionThreadUUIDs( String projectId, String reviewId, BiFunction<String, String, Collection<String>> loader ) {
        if (isCached( projectId, reviewId )) {
            return projectIdReviewIdToThreadIds.get( projectId ).get( reviewId );
        }

        if (loader != null) {
            Collection<String> threadIds = loader.apply( projectId, reviewId );
            if (threadIds != null) {
                this.addAllDiscussionThreads( projectId, reviewId, threadIds );
                return threadIds;
            }
        }
        return new ArrayList<>();
    }

    public void addDiscussionThread( String projectId, String reviewId, String threadUUID ) {
        this.projectIdReviewIdToThreadIds // 
                        .computeIfAbsent( projectId, id -> new HashMap<>() )//
                        .computeIfAbsent( reviewId, id -> new LinkedHashSet<String>() ) //
                        .add( threadUUID );
    }

    public void addAllDiscussionThreads( String projectId, String reviewId, Collection<String> threadUUIDs ) {
        this.projectIdReviewIdToThreadIds // 
                        .computeIfAbsent( projectId, id -> new HashMap<>() )//
                        .computeIfAbsent( reviewId, id -> new LinkedHashSet<String>() ) //
                        .addAll( threadUUIDs );
    }

}
