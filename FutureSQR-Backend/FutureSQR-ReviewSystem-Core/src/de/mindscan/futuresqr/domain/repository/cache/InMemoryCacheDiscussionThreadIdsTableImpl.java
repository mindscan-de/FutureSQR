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

/**
 * search key: ( projectId:string , reviewId:string ) -> ( threadIds:ArrayList<String> )
 */
public class InMemoryCacheDiscussionThreadIdsTableImpl {

    private Map<String, Map<String, ArrayList<String>>> projectIdReviewIdToThreadIds;

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

    public boolean hasDiscussionThreadUUID( String projectId, String reviewId, String discussionThreadUUID ) {
        if (!isCached( projectId, reviewId )) {
            return false;
        }

        return getDiscussionThreadUUIDs( projectId, reviewId ).contains( discussionThreadUUID );
    }

    private boolean isKnownProjectId( String projectId ) {
        return projectIdReviewIdToThreadIds.containsKey( projectId );
    }

    public List<String> getDiscussionThreadUUIDs( String projectId, String reviewId ) {
        if (isCached( projectId, reviewId )) {
            return projectIdReviewIdToThreadIds.get( projectId ).get( reviewId );
        }
        return new ArrayList<>();
    }

    public void addDiscussionThread( String projectId, String reviewId, String threadUUID ) {
        this.projectIdReviewIdToThreadIds // 
                        .computeIfAbsent( projectId, id -> new HashMap<>() )//
                        .computeIfAbsent( reviewId, id -> new ArrayList<String>() ) //
                        .add( threadUUID );
    }
}
