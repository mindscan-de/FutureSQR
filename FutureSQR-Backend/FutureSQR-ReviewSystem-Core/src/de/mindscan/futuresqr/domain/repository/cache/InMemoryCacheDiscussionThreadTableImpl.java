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
import java.util.Map;
import java.util.function.Function;

import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThread;

/**
 * search key: ( threaduuid:string ) -> thread:FSqrDiscussionThread
 */
public class InMemoryCacheDiscussionThreadTableImpl {

    // search key: ( threaduuid:string ) -> thread:FSqrDiscussionThread
    private Map<String, FSqrDiscussionThread> threadCacheTable;

    /**
     * 
     */
    public InMemoryCacheDiscussionThreadTableImpl() {
        this.threadCacheTable = new HashMap<>();
    }

    boolean isCached( String threadUuid ) {
        return this.threadCacheTable.containsKey( threadUuid );
    }

    public void putDiscussionThread( String threadUuid, FSqrDiscussionThread newThread ) {
        this.threadCacheTable.put( threadUuid, newThread );
    }

    FSqrDiscussionThread getDiscussionThread( String threadUuuid ) {
        return this.threadCacheTable.get( threadUuuid );
    }

    public FSqrDiscussionThread getDiscussionThread( String threadUuuid, Function<String, FSqrDiscussionThread> loader ) {
        if (isCached( threadUuuid )) {
            return getDiscussionThread( threadUuuid );
        }

        if (loader != null) {
            FSqrDiscussionThread thread = loader.apply( threadUuuid );
            if (thread != null) {
                putDiscussionThread( threadUuuid, thread );
                return thread;
            }
        }

        return null;
    }

    Collection<FSqrDiscussionThread> lookupThreads( Collection<String> input ) {
        ArrayList<FSqrDiscussionThread> result = new ArrayList<>();

        for (String threadId : input) {
            FSqrDiscussionThread discussionThread = this.getDiscussionThread( threadId );
            if (discussionThread != null) {
                result.add( discussionThread );
            }
        }

        return result;
    }

    public Collection<FSqrDiscussionThread> lookupThreads( Collection<String> input, Function<String, FSqrDiscussionThread> loader ) {
        ArrayList<FSqrDiscussionThread> result = new ArrayList<>();

        for (String threadId : input) {
            FSqrDiscussionThread discussionThread = this.getDiscussionThread( threadId, loader );
            if (discussionThread != null) {
                result.add( discussionThread );
            }
        }

        return result;
    }

}
