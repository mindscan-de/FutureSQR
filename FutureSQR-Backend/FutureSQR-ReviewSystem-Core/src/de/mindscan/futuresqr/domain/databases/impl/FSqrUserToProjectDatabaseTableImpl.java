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

import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.FSqrUserToProjectDatabaseTable;

/**
 * This is a fake implementation of the User To Starred Projects Database table. This is half way to
 * through to implement an access for a real database table. 
 */
public class FSqrUserToProjectDatabaseTableImpl implements FSqrUserToProjectDatabaseTable {

    private Gson gson = new Gson();

    private static final String STARRED_PROJECT_TABLENAME = "StarredProjects";

    private static final String STARRED_PROJECT_FK_USERUUID_COLUM = "userUuid";
    private static final String STARRED_PROJECT_FK_PROJECTID_COLUUMN = "projectId";

    // 

    private static final String DROP_TABLE_IF_EXISTS = // 
                    "DROP TABLE IF EXISTS " + STARRED_PROJECT_TABLENAME + ";";

    private static final String CREATE_TABLE_STARRED_PROJECTS = //
                    "CREATE TABLE  " + STARRED_PROJECT_TABLENAME + //
                                    " (" + STARRED_PROJECT_FK_USERUUID_COLUM + ", " + STARRED_PROJECT_FK_PROJECTID_COLUUMN + ");";

    private static final String INSERT_STAR_PS = //
                    "INSERT INTO " + STARRED_PROJECT_TABLENAME + //
                                    " (" + STARRED_PROJECT_FK_USERUUID_COLUM + ", " + STARRED_PROJECT_FK_PROJECTID_COLUUMN + " ) VALUES (:?1, :?2);";

    private static final String DELETE_STAR_PS = //
                    "DELETE FROM " + STARRED_PROJECT_TABLENAME + // 
                                    " WHERE ( " + STARRED_PROJECT_FK_USERUUID_COLUM + "=:?1 AND " + STARRED_PROJECT_FK_PROJECTID_COLUUMN + "=:?2);";

    private static final String SELECT_STARRED_PROJECTS_BY_USER_PS = //
                    "SELECT * FROM " + STARRED_PROJECT_TABLENAME + //
                                    "WHERE (" + STARRED_PROJECT_FK_USERUUID_COLUM + "=:?1);";

    private static final String SELECT_STARRING_USERS_BY_PROJECT_PS = //
                    "SELECT * FROM " + STARRED_PROJECT_TABLENAME + //
                                    "WHERE (" + STARRED_PROJECT_FK_PROJECTID_COLUUMN + "=:?1);";

    private FSqrDatabaseConnection connection;

    private static final HashSet<String> EMPTY_HASH_SET = new HashSet<>();

    // search key: (useruuid:string) -> (projectid:Collection)
    private Map<String, Set<String>> tmpUserToProjectStarsTable;

    /**
     * 
     */
    public FSqrUserToProjectDatabaseTableImpl() {
        this.tmpUserToProjectStarsTable = new HashMap<>();

        // TODO: remove me, when we have a database and a database session object.
        initHardCodedData();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setDatbaseConnection( FSqrDatabaseConnection connection ) {
        this.connection = connection;
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
        insertStar( mindscanUserId, "stable-diffusion-webui" );
        insertStar( mindscanUserId, "stable-diffusion-webui-wiki" );
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

    /** 
     * {@inheritDoc}
     */
    @Override
    public void createTable() {
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate( DROP_TABLE_IF_EXISTS );
            statement.executeUpdate( CREATE_TABLE_STARRED_PROJECTS );
            initHardCodedData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

}
