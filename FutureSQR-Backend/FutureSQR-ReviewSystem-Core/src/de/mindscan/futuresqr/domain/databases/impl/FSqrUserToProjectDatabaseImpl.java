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
package de.mindscan.futuresqr.domain.databases.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.mindscan.futuresqr.domain.databases.FSqrUserToProjectDatabase;

/**
 * 
 */
public class FSqrUserToProjectDatabaseImpl implements FSqrUserToProjectDatabase {

    private static final HashSet<String> EMPTY_HASH_SET = new HashSet<>();

    // search key: (useruuid:string) -> (projectid:Collection)
    private Map<String, Set<String>> tmpUserToProjectStarsTable;

    /**
     * 
     */
    public FSqrUserToProjectDatabaseImpl() {
        this.tmpUserToProjectStarsTable = new HashMap<>();

        // TODO: remove me, when we have a database and a database session object.
        initHardCodedData();
    }

    /**
     * 
     */
    protected void initHardCodedData() {
        String mindscanUserId = "8ce74ee9-48ff-3dde-b678-58a632887e31";
        insertStar( mindscanUserId, "furiousiron-frontend" );
        insertStar( mindscanUserId, "furiousiron-hfb" );
        insertStar( mindscanUserId, "futuresqr" );
        insertStar( mindscanUserId, "futuresqr-svn-trunk" );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void insertStar( String userId, String projectId ) {
        this.tmpUserToProjectStarsTable.computeIfAbsent( userId, id -> new HashSet<>() ).add( projectId );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void deleteStar( String userId, String projectId ) {
        this.tmpUserToProjectStarsTable.getOrDefault( userId, EMPTY_HASH_SET ).remove( userId );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Set<String> selectAllStarredProjectsByUserId( String userId ) {
        if (tmpUserToProjectStarsTable.containsKey( userId )) {
            return new HashSet<>( tmpUserToProjectStarsTable.get( userId ) );
        }

        return new HashSet<>();
    }

}
