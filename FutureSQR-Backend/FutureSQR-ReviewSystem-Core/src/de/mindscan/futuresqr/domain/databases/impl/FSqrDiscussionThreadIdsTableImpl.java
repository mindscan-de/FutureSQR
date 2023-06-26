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
import java.util.ArrayList;
import java.util.List;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.FSqrDiscussionThreadIdsTable;
import de.mindscan.futuresqr.domain.databases.type.FSqrSqliteDatabaseImpl;

/**
 * TODO: Refactor to more general database approach, such that these constants are in a 
 *       single database query provider, and get rid of of the constants  
 */
public class FSqrDiscussionThreadIdsTableImpl implements FSqrDiscussionThreadIdsTable {

    private static final String INSERT_TABLE_REVIEW_DISCUSSON = //
                    "INSERT INTO " + FSqrSqliteDatabaseImpl.getCodeReviewDiscussionsTable().tableName() + //
                                    " (" + FSqrSqliteDatabaseImpl.CODE_REVIEW_DISCUSSIONS_FK_PROJECTID_COLUMN + //
                                    ", " + FSqrSqliteDatabaseImpl.CODE_REVIEW_DISCUSSIONS_FK_REVIEWID_COLUMN + //
                                    ", " + FSqrSqliteDatabaseImpl.CODE_REVIEW_DISCUSSIONS_FK_THREADUUID_COLUM + //;
                                    ", " + FSqrSqliteDatabaseImpl.CODE_REVIEW_DISCISSIONS_CREATED_TS_COLUMNS + //
                                    " ) VALUES (?1, ?2, ?3, CURRENT_TIMESTAMP); ";

    private static final String SELECT_DISCUSSIONS_FOR_REVIEW_PS = //
                    "SELECT * FROM " + FSqrSqliteDatabaseImpl.getCodeReviewDiscussionsTable().tableName() + //
                                    " WHERE ( " + FSqrSqliteDatabaseImpl.CODE_REVIEW_DISCUSSIONS_FK_PROJECTID_COLUMN + "=?1  AND "
                                    + FSqrSqliteDatabaseImpl.CODE_REVIEW_DISCUSSIONS_FK_REVIEWID_COLUMN + "=?2) " + //
                                    " ORDER BY " + FSqrSqliteDatabaseImpl.CODE_REVIEW_DISCISSIONS_CREATED_TS_COLUMNS + ";";

    private FSqrDatabaseConnection connection;

    /**
     * 
     */
    public FSqrDiscussionThreadIdsTableImpl() {
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
     * {@inheritDoc}
     */
    @Override
    public void addDiscussionThread( String projectId, String reviewId, String discussionThreadUUID ) {
        try (PreparedStatement insertPS = this.connection.createPreparedStatement( INSERT_TABLE_REVIEW_DISCUSSON )) {
            insertPS.setString( 1, projectId );
            insertPS.setString( 2, reviewId );
            insertPS.setString( 3, discussionThreadUUID );

            insertPS.addBatch();
            insertPS.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public List<String> selectDiscussionThreads( String projectId, String reviewId ) {
        List<String> result = new ArrayList<>();

        // TODO: maybe return a  different type, with some extra infos about the thread.
        try (PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_DISCUSSIONS_FOR_REVIEW_PS )) {
            selectPS.setString( 1, projectId );
            selectPS.setString( 2, reviewId );

            try (ResultSet resultSet = selectPS.executeQuery()) {
                while (resultSet.next()) {
                    result.add( resultSet.getString( FSqrSqliteDatabaseImpl.CODE_REVIEW_DISCUSSIONS_FK_THREADUUID_COLUM ) );
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
            statement.executeUpdate( FSqrSqliteDatabaseImpl.QUERY_CODE_REVIEW_DISCUSSIONS_DROP_TABLE );
            statement.executeUpdate( FSqrSqliteDatabaseImpl.QUERY_CODE_REVIEW_DISCUSSIONS_CREATE_TABLE );
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
