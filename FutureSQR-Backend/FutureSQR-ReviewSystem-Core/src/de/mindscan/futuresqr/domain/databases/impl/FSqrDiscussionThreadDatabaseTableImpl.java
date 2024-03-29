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

import com.google.gson.Gson;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.FSqrDiscussionThreadDatabaseTable;
import de.mindscan.futuresqr.domain.databases.type.FSqrSqliteDatabaseImpl;
import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThread;

/**
 * TODO: Refactor to more general database approach, such that these constants are in a 
 *       single database query provider, and get rid of of the constants  
 */
public class FSqrDiscussionThreadDatabaseTableImpl implements FSqrDiscussionThreadDatabaseTable {

    private static final String INSERT_DISCUSSION_THREAD = //
                    "INSERT INTO " + FSqrSqliteDatabaseImpl.getDiscussionThreadTable().tableName() + //
                                    " (" + FSqrSqliteDatabaseImpl.DISCUSSION_THREAD_PK_UUID_COLUMN.getColumnName() + //
                                    ", " + FSqrSqliteDatabaseImpl.DISCUSSION_THREAD_THREADDATA_COLUMN.getColumnName() + ") VALUES (?1, ?2); ";

    private static final String UPDATE_DISCUSSION_THREAD = //
                    "UPDATE " + FSqrSqliteDatabaseImpl.getDiscussionThreadTable().tableName() + //
                                    " SET " + FSqrSqliteDatabaseImpl.DISCUSSION_THREAD_THREADDATA_COLUMN.getColumnName() + "=?2 WHERE "
                                    + FSqrSqliteDatabaseImpl.DISCUSSION_THREAD_PK_UUID_COLUMN.getColumnName() + "=?1;";

    private static final String SELECT_DISCUSSION_THREAD = //
                    "SELECT * FROM " + FSqrSqliteDatabaseImpl.getDiscussionThreadTable().tableName() + //
                                    " WHERE " + FSqrSqliteDatabaseImpl.DISCUSSION_THREAD_PK_UUID_COLUMN.getColumnName() + "=?1;";

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
        try (PreparedStatement insertPS = this.connection.createPreparedStatement( INSERT_DISCUSSION_THREAD )) {
            String serializedThread = gson.toJson( newThread );

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
        try (PreparedStatement updatePS = this.connection.createPreparedStatement( UPDATE_DISCUSSION_THREAD )) {
            String serializedThread = gson.toJson( thread );

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
        try (PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_DISCUSSION_THREAD )) {
            selectPS.setString( 1, discussionThreadUUID );

            try (ResultSet resultSet = selectPS.executeQuery()) {
                if (resultSet.next()) {
                    result = createDiscussionThread( resultSet );
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private FSqrDiscussionThread createDiscussionThread( ResultSet resultSet ) throws Exception {
        String discussionDataString = resultSet.getString( FSqrSqliteDatabaseImpl.DISCUSSION_THREAD_THREADDATA_COLUMN.getColumnName() );

        return gson.fromJson( discussionDataString, FSqrDiscussionThread.class );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void createTable() {
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

}
