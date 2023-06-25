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
import de.mindscan.futuresqr.domain.databases.type.FSqrSqliteDatabaseImpl;

/**
 * TODO: Refactor to more general database approach, such that these constants are in a 
 *       single database query provider, and get rid of of the constants  
 */
public class FSqrUserToProjectDatabaseTableImpl implements FSqrUserToProjectDatabaseTable {

    private static final String STARRED_PROJECTS_COUNT = "COUNT";

    private static final String INSERT_STAR_PS = //
                    "INSERT INTO " + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_TABLENAME + //
                                    " (" + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_FK_USERUUID_COLUM + //
                                    ", " + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_FK_PROJECTID_COLUUMN + //
                                    ", " + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_STARRED_TS_COLUMN + " ) VALUES (?1, ?2, CURRENT_TIMESTAMP);";

    private static final String DELETE_STAR_PS = //
                    "DELETE FROM " + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_TABLENAME + // 
                                    " WHERE ( " + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_FK_USERUUID_COLUM + "=?1 AND "
                                    + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_FK_PROJECTID_COLUUMN + "=?2);";

    private static final String SELECT_STARRED_PROJECTS_BY_USER_PS = //
                    "SELECT * FROM " + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_TABLENAME + //
                                    " WHERE (" + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_FK_USERUUID_COLUM + "=?1) " + //
                                    " ORDER BY " + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_STARRED_TS_COLUMN + ";";

    private static final String SELECT_STARRING_USERS_BY_PROJECT_PS = //
                    "SELECT * FROM " + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_TABLENAME + //
                                    " WHERE (" + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_FK_PROJECTID_COLUUMN + "=?1) " + //
                                    " ORDER BY " + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_STARRED_TS_COLUMN + ";";

    private static final String SELECT_STARRING_USERCOUNT_BY_PROJECT_PS = //
                    "SELECT COUNT(*) AS " + STARRED_PROJECTS_COUNT + " FROM " + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_TABLENAME + //
                                    " WHERE (" + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_FK_PROJECTID_COLUUMN + "=?1) " + //
                                    " ORDER BY " + FSqrSqliteDatabaseImpl.STARRED_PROJECTS_STARRED_TS_COLUMN + ";";

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
        try (PreparedStatement starPS = this.connection.createPreparedStatement( INSERT_STAR_PS )) {
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
        try (PreparedStatement unstarPS = this.connection.createPreparedStatement( DELETE_STAR_PS )) {
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
        try (PreparedStatement selectProjectsForUserPS = this.connection.createPreparedStatement( SELECT_STARRED_PROJECTS_BY_USER_PS )) {
            selectProjectsForUserPS.setString( 1, userId );

            Set<String> result = new LinkedHashSet<>();
            try (ResultSet resultSet = selectProjectsForUserPS.executeQuery()) {
                while (resultSet.next()) {
                    // actually we might want to add the time stamp
                    result.add( resultSet.getString( FSqrSqliteDatabaseImpl.STARRED_PROJECTS_FK_PROJECTID_COLUUMN ) );
                }
            }
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
        try (PreparedStatement selectStarringUsersPS = this.connection.createPreparedStatement( SELECT_STARRING_USERS_BY_PROJECT_PS );) {
            selectStarringUsersPS.setString( 1, projectId );

            Set<String> result = new LinkedHashSet<>();
            try (ResultSet resultSet = selectStarringUsersPS.executeQuery()) {
                while (resultSet.next()) {
                    // actually we might want to add the time stamp
                    result.add( resultSet.getString( FSqrSqliteDatabaseImpl.STARRED_PROJECTS_FK_USERUUID_COLUM ) );
                }
            }
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
        int result = 0;

        try (PreparedStatement countStarringUsersPS = this.connection.createPreparedStatement( SELECT_STARRING_USERCOUNT_BY_PROJECT_PS )) {
            countStarringUsersPS.setString( 1, projectId );

            try (ResultSet resultSet = countStarringUsersPS.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getInt( STARRED_PROJECTS_COUNT );
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void createTable() {

        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate( FSqrSqliteDatabaseImpl.QUERY_STARRED_PROJECTS_DROP_TABLE );
            statement.executeUpdate( FSqrSqliteDatabaseImpl.QUERY_STARRED_PROJECTS_CREATE_TABLE );
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
