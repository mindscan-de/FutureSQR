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

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.FSqrProjectAssignedCodeReviewsTable;

/**
 * TODO: Refactor to more general database approach, such that these constants are in a 
 *       single database query provider, and get rid of of the constants  
 */
public class FSqrProjectAssignedCodeReviewsTableImpl implements FSqrProjectAssignedCodeReviewsTable {

    private static final String REVISION_TO_CODE_REVIEW_TABLENAME = "RevisionsToReviews";

    private static final String COLUMNNAME_PROJECT_ID = "projectId";
    private static final String COLUMNNAME_REVISION_ID = "revisionId";
    private static final String COLUMNNAME_REVIEW_ID = "reviewId";

    private static final String DROP_TABLE_IF_EXISTS = // 
                    "DROP TABLE IF EXISTS " + REVISION_TO_CODE_REVIEW_TABLENAME + ";";

    private static final String CREATE_TABLE_REVISION_TO_REVIEW = // 
                    "CREATE TABLE  " + REVISION_TO_CODE_REVIEW_TABLENAME + " (" + COLUMNNAME_PROJECT_ID + ", " + COLUMNNAME_REVISION_ID + ", "
                                    + COLUMNNAME_REVIEW_ID + ");";

    private static final String SELECT_CODEREVIEW_FOR_PROJECT_REVISION = //
                    "SELECT " + COLUMNNAME_REVIEW_ID + " FROM " + REVISION_TO_CODE_REVIEW_TABLENAME + " WHERE (" + COLUMNNAME_PROJECT_ID + "=?1  AND "
                                    + COLUMNNAME_REVISION_ID + "=?2 ); ";

    private static final String INSERT_CODEREVIEW_FOR_PROJECT_REVISION = //
                    "INSERT INTO " + REVISION_TO_CODE_REVIEW_TABLENAME + " (" + COLUMNNAME_PROJECT_ID + "," + COLUMNNAME_REVISION_ID + ","
                                    + COLUMNNAME_REVIEW_ID + ") VALUES (?1, ?2, ?3); ";

    private static final String REMOVE_CODEREVIEW_FOR_PROJECT_REVISION = //
                    "DELETE FROM " + REVISION_TO_CODE_REVIEW_TABLENAME + " WHERE ( " + COLUMNNAME_PROJECT_ID + "=?1 AND " + COLUMNNAME_REVISION_ID + "=?2 );";

    private static final String REMOVE_CODEREVIEW_FOR_PROJECT = //
                    "DELETE FROM " + REVISION_TO_CODE_REVIEW_TABLENAME + " WHERE ( " + COLUMNNAME_PROJECT_ID + "=?1 AND " + COLUMNNAME_REVIEW_ID + "=?2 );";

    private FSqrDatabaseConnection connection;

    /**
     * 
     */
    public FSqrProjectAssignedCodeReviewsTableImpl() {
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
    public String selectCodeReviewId( String projectId, String revisionId ) {
        String result = "";

        try (PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_CODEREVIEW_FOR_PROJECT_REVISION )) {
            selectPS.setString( 1, projectId );
            selectPS.setString( 2, revisionId );

            try (ResultSet resultSet = selectPS.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getString( COLUMNNAME_REVIEW_ID );
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
    public void insertCodeReviewId( String projectId, String revisionId, String reviewId ) {
        try (PreparedStatement insertPS = this.connection.createPreparedStatement( INSERT_CODEREVIEW_FOR_PROJECT_REVISION )) {
            insertPS.setString( 1, projectId );
            insertPS.setString( 2, revisionId );
            insertPS.setString( 3, reviewId );

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
    public void removeCodeReviewId( String projectId, String revisionId ) {
        try (PreparedStatement removePS = this.connection.createPreparedStatement( REMOVE_CODEREVIEW_FOR_PROJECT_REVISION )) {
            removePS.setString( 1, projectId );
            removePS.setString( 2, revisionId );

            removePS.addBatch();
            removePS.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void removeAllCodeRevisionsForReview( String projectId, String reviewId ) {
        try (PreparedStatement removePS = this.connection.createPreparedStatement( REMOVE_CODEREVIEW_FOR_PROJECT )) {
            removePS.setString( 1, projectId );
            removePS.setString( 2, reviewId );

            removePS.addBatch();
            removePS.executeBatch();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void createTable() {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate( DROP_TABLE_IF_EXISTS );
            statement.executeUpdate( CREATE_TABLE_REVISION_TO_REVIEW );
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
