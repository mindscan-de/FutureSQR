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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 */
public class InMemoryCacheUserStarredProjectTableImpl {

    // search key: (useruuid:string) -> (projectid:Collection)
    private Map<String, Set<String>> starredProjectsByUser;

    /**
     * 
     */
    public InMemoryCacheUserStarredProjectTableImpl() {
        this.starredProjectsByUser = new HashMap<>();
    }

    // return if there is data for this user cached.
    public boolean isCached( String userId ) {
        return starredProjectsByUser.containsKey( userId );
    }

    public void addStarredProject( String userId, String projectId ) {
        this.starredProjectsByUser.computeIfAbsent( userId, id -> new HashSet<>() ).add( projectId );
    }

    public void removeStarredProject( String userId, String projectId ) {
        if (isCached( userId )) {
            this.starredProjectsByUser.get( userId ).remove( projectId );
        }
    }

    public boolean isStarred( String userId, String projectId ) {
        if (!isCached( userId )) {
            return false;
        }

        return this.starredProjectsByUser.get( userId ).contains( projectId );
    }

    public Set<String> getStarredProjects( String userId ) {
        if (isCached( userId )) {
            return this.starredProjectsByUser.get( userId );
        }
        return new HashSet<>();
    }

}
