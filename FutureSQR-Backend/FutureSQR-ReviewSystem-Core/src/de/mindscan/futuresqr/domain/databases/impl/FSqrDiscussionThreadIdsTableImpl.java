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
import java.util.Collection;
import java.util.LinkedHashSet;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.FSqrDiscussionThreadIdsTable;

/**
 * 
 */
public class FSqrDiscussionThreadIdsTableImpl implements FSqrDiscussionThreadIdsTable {

    // table name

    private static final String REVIEW_DISCUSSIONS_TABLENAME = "ReviewDiscussions";

    // column names

    private static final String REVIEW_DISCUSSIONS_FK_PROJECTID_COLUMN = "projectId";
    // maybe use the UUID?, so we can drop the projectId...
    private static final String REVIEW_DISCUSSIONS_FK_REVIEWID_COLUMN = "reviewId";
    private static final String REVIEW_DISCUSSIONS_FK_THREADUUID_COLUM = "threadUuid";
    private static final String REVIEW_DISCISSIONS_CREATED_TS_COLUMNS = "created";
    // TODO maybe a last updated for each thread?...
    // TODO maybe we need a indicator, whether a full thread is resolved.
    // TODO maybe state whether thread was deleted...

    // sql statements

    private static final String DROP_TABLE_IF_EXISTS = // 
                    "DROP TABLE IF EXISTS " + REVIEW_DISCUSSIONS_TABLENAME + ";";

    private static final String CREATE_TABLE_REVIEW_DISCUSSIONS = //
                    "CREATE TABLE  " + REVIEW_DISCUSSIONS_TABLENAME + //
                                    " (" + REVIEW_DISCUSSIONS_FK_PROJECTID_COLUMN + //
                                    ", " + REVIEW_DISCUSSIONS_FK_REVIEWID_COLUMN + //
                                    ", " + REVIEW_DISCUSSIONS_FK_THREADUUID_COLUM + //
                                    ", " + REVIEW_DISCISSIONS_CREATED_TS_COLUMNS + ");";

    private static final String INSERT_TABLE_REVIEW_DISCUSSON = //
                    "INSERT INTO " + REVIEW_DISCUSSIONS_TABLENAME + //
                                    " (" + REVIEW_DISCUSSIONS_FK_PROJECTID_COLUMN + //
                                    ", " + REVIEW_DISCUSSIONS_FK_REVIEWID_COLUMN + //
                                    ", " + REVIEW_DISCUSSIONS_FK_THREADUUID_COLUM + //;
                                    ", " + REVIEW_DISCISSIONS_CREATED_TS_COLUMNS + //
                                    " ) VALUES (?1, ?2, ?3, CURRENT_TIMESTAMP); ";

    private static final String SELECT_DISCUSSIONS_FOR_REVIEW_PS = //
                    "SELECT * FROM " + REVIEW_DISCUSSIONS_TABLENAME + //
                                    " WHERE ( " + REVIEW_DISCUSSIONS_FK_PROJECTID_COLUMN + "=?1  AND " + REVIEW_DISCUSSIONS_FK_REVIEWID_COLUMN + "=?2) " + //
                                    " ORDER BY " + REVIEW_DISCISSIONS_CREATED_TS_COLUMNS + ";";

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
    public Collection<String> selectDiscussionThreads( String projectId, String reviewId ) {
        LinkedHashSet<String> result = new LinkedHashSet<>();

        try (PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_DISCUSSIONS_FOR_REVIEW_PS )) {
            selectPS.setString( 1, projectId );
            selectPS.setString( 2, reviewId );

            try (ResultSet resultSet = selectPS.executeQuery()) {
                while (resultSet.next()) {
                    result.add( resultSet.getString( REVIEW_DISCUSSIONS_FK_THREADUUID_COLUM ) );
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
            statement.executeUpdate( DROP_TABLE_IF_EXISTS );
            statement.executeUpdate( CREATE_TABLE_REVIEW_DISCUSSIONS );
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
