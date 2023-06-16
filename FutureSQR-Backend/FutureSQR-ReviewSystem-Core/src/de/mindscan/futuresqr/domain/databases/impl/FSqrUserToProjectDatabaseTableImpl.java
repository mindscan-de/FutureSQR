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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.FSqrUserToProjectDatabaseTable;

/**
 * This is a fake implementation of the User To Starred Projects Database table. This is half way to
 * through to implement an access for a real database table. 
 */
public class FSqrUserToProjectDatabaseTableImpl implements FSqrUserToProjectDatabaseTable {

    private static final String STARRED_PROJECT_TABLENAME = "StarredProjects";

    private static final String STARRED_PROJECT_FK_USERUUID_COLUM = "userUuid";
    private static final String STARRED_PROJECT_FK_PROJECTID_COLUUMN = "projectId";
    private static final String STARRED_PROJECT_STARRED_TS = "whenStarred";

    // 

    private static final String DROP_TABLE_IF_EXISTS = // 
                    "DROP TABLE IF EXISTS " + STARRED_PROJECT_TABLENAME + ";";

    private static final String CREATE_TABLE_STARRED_PROJECTS = //
                    "CREATE TABLE  " + STARRED_PROJECT_TABLENAME + //
                                    " (" + STARRED_PROJECT_FK_USERUUID_COLUM + //
                                    ", " + STARRED_PROJECT_FK_PROJECTID_COLUUMN + //
                                    ", " + STARRED_PROJECT_STARRED_TS + ");";

    // TODO: current date and time

    private static final String INSERT_STAR_PS = //
                    "INSERT INTO " + STARRED_PROJECT_TABLENAME + //
                                    " (" + STARRED_PROJECT_FK_USERUUID_COLUM + //
                                    ", " + STARRED_PROJECT_FK_PROJECTID_COLUUMN + //
                                    ", " + STARRED_PROJECT_STARRED_TS + " ) VALUES (?1, ?2, CURRENT_TIMESTAMP);";

    private static final String DELETE_STAR_PS = //
                    "DELETE FROM " + STARRED_PROJECT_TABLENAME + // 
                                    " WHERE ( " + STARRED_PROJECT_FK_USERUUID_COLUM + "=?1 AND " + STARRED_PROJECT_FK_PROJECTID_COLUUMN + "=?2);";

    private static final String SELECT_STARRED_PROJECTS_BY_USER_PS = //
                    "SELECT * FROM " + STARRED_PROJECT_TABLENAME + //
                                    " WHERE (" + STARRED_PROJECT_FK_USERUUID_COLUM + "=?1) " + //
                                    " ORDER BY " + STARRED_PROJECT_STARRED_TS + ";";

    private static final String SELECT_STARRING_USERS_BY_PROJECT_PS = //
                    "SELECT * FROM " + STARRED_PROJECT_TABLENAME + //
                                    " WHERE (" + STARRED_PROJECT_FK_PROJECTID_COLUUMN + "=?1) " + //
                                    " ORDER BY " + STARRED_PROJECT_STARRED_TS + ";";

    private FSqrDatabaseConnection connection;

    /**
     * 
     */
    public FSqrUserToProjectDatabaseTableImpl() {
        // intentionally left blank
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
        try {
            PreparedStatement starPS = this.connection.createPreparedStatement( INSERT_STAR_PS );
            starPS.setString( 1, userId );
            starPS.setString( 2, projectId );

            starPS.addBatch();
            starPS.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void deleteStar( String userId, String projectId ) {
        try {
            PreparedStatement unstarPS = this.connection.createPreparedStatement( DELETE_STAR_PS );
            unstarPS.setString( 1, userId );
            unstarPS.setString( 2, projectId );

            unstarPS.addBatch();
            unstarPS.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Set<String> selectAllStarredProjectsByUserId( String userId ) {
        try {
            Set<String> result = new LinkedHashSet<>();

            PreparedStatement selectProjectsForUserPS = this.connection.createPreparedStatement( SELECT_STARRED_PROJECTS_BY_USER_PS );

            selectProjectsForUserPS.setString( 1, userId );

            ResultSet resultSet = selectProjectsForUserPS.executeQuery();
            while (resultSet.next()) {
                // actually we might want to add the time stamp
                result.add( resultSet.getString( STARRED_PROJECT_FK_PROJECTID_COLUUMN ) );
            }
            resultSet.close();
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new HashSet<>();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Set<String> selectAllStarringUsersForProject( String projectId ) {
        try {
            Set<String> result = new LinkedHashSet<>();

            PreparedStatement selectStarringUsersPS = this.connection.createPreparedStatement( SELECT_STARRING_USERS_BY_PROJECT_PS );

            selectStarringUsersPS.setString( 1, projectId );

            ResultSet resultSet = selectStarringUsersPS.executeQuery();
            while (resultSet.next()) {
                // actually we might want to add the time stamp
                result.add( resultSet.getString( STARRED_PROJECT_FK_USERUUID_COLUM ) );

            }
            resultSet.close();
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new HashSet<>();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfStarsForProject( String projectId ) {
        // TODO implement the counting and the additional caching of the star count.
        return 0;
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
        // intentionally left blank
    }

}
