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

import com.google.gson.Gson;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.FSqrDiscussionThreadDatabaseTable;
import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThread;

/**
 * 
 */
public class FSqrDiscussionThreadDatabaseTableImpl implements FSqrDiscussionThreadDatabaseTable {

    private static final String DISCUSSION_TABLENAME = "DiscussionThread";

    private static final String DISCUSSION_UUID_COLUMN = "uuid";
    private static final String DISCUSSION_THREADDATA_COLUMN = "threadData";

    // 

    private static final String CREATE_TABLE_DISCUSSION_THREAD = //
                    "CREATE TABLE " + DISCUSSION_TABLENAME + " (" + DISCUSSION_UUID_COLUMN + ", " + DISCUSSION_THREADDATA_COLUMN + "); ";

    private static final String DROP_TABLE_IF_EXISTS = // 
                    "DROP TABLE IF EXISTS " + DISCUSSION_TABLENAME + ";";

    private static final String INSERT_DISCUSSION_THREAD = //
                    "INSERT INTO " + DISCUSSION_TABLENAME + " (" + DISCUSSION_UUID_COLUMN + ", " + DISCUSSION_THREADDATA_COLUMN + ") VALUES (?1, ?2); ";

    private static final String UPDATE_DISCUSSION_THREAD = //
                    "UPDATE " + DISCUSSION_TABLENAME + " SET " + DISCUSSION_THREADDATA_COLUMN + "=?2 WHERE " + DISCUSSION_UUID_COLUMN + "=?1;";

    private static final String SELECT_DISCUSSION_THREAD = //
                    "SELECT * FROM " + DISCUSSION_TABLENAME + " WHERE " + DISCUSSION_UUID_COLUMN + "=?1;";

    private FSqrDatabaseConnection connection;

    private Gson gson = new Gson();

    /**
     * 
     */
    public FSqrDiscussionThreadDatabaseTableImpl() {
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
    public void insertDiscussionThread( FSqrDiscussionThread newThread ) {
        try {
            String serializedThread = gson.toJson( newThread );

            PreparedStatement insertPS = this.connection.createPreparedStatement( INSERT_DISCUSSION_THREAD );

            insertPS.setString( 1, newThread.getDiscussionThreadUUID() );
            insertPS.setString( 2, serializedThread );

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
    public void updateThread( FSqrDiscussionThread thread ) {
        try {
            String serializedThread = gson.toJson( thread );

            PreparedStatement updatePS = this.connection.createPreparedStatement( UPDATE_DISCUSSION_THREAD );

            updatePS.setString( 1, thread.getDiscussionThreadUUID() );
            updatePS.setString( 2, serializedThread );

            updatePS.addBatch();
            updatePS.executeBatch();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrDiscussionThread selectDiscussionThread( String discussionThreadUUID ) {
        FSqrDiscussionThread result = null;
        try {
            PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_DISCUSSION_THREAD );

            selectPS.setString( 1, discussionThreadUUID );

            try (ResultSet resultSet = selectPS.executeQuery()) {
                if (resultSet.next()) {
                    result = createDicsussionThread( resultSet );
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private FSqrDiscussionThread createDicsussionThread( ResultSet resultSet ) throws Exception {
        String discussionDataString = resultSet.getString( DISCUSSION_THREADDATA_COLUMN );

        return gson.fromJson( discussionDataString, FSqrDiscussionThread.class );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void createTable() {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate( DROP_TABLE_IF_EXISTS );
            statement.executeUpdate( CREATE_TABLE_DISCUSSION_THREAD );
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
