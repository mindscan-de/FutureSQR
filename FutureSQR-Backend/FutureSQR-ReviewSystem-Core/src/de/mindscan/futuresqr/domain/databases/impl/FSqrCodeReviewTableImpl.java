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
import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.FSqrCodeReviewTable;
import de.mindscan.futuresqr.domain.model.FSqrCodeReview;
import de.mindscan.futuresqr.domain.model.FSqrCodeReviewLifecycleState;

/**
 * This is an implementation of the database tables related to code reviews. 
 * 
 * This an implementation relying on SQLite - This implementation is for exploratory reasons use only and
 * will be improved and replaced by something else. But as for June 2023 speaking, This will serve a good
 * cause until we decided for a database structure and implementation.
 * 
 * Now the thing is to provide some long awaited persistence and to test some update and write back 
 * mechanics.
 * 
 * - One problem is that the scm configuration contains the counter... so reviews are duplicated, the scm 
 *   config needs to be updated to contain the next review.
 * - Another problem is that we should have both the reviewid and the review number within the project
 * - it turns out, that relational databases seem not to be a good idea, overly complated to serialize and 
 *   unersialize into a database and into objects, a NOSQL Database might be of more use.
 */
public class FSqrCodeReviewTableImpl implements FSqrCodeReviewTable {

    private Gson gson = new Gson();

    private static final String CODE_REVIEW_TABLENAME = "CodeReviews";

    private static final String CODE_REVIEW_REVIWEDATA_COLUMN = "reviewData";
    private static final String CODE_REVIEW_STATE_COLUMN = "state";
    private static final String CODE_REVIEW_FK_PROJECTID_COLUMN = "projectId";
    private static final String CODE_REVIEW_REVIEWID_COLUMN = "reviewId";

    // reviewData is the gson serialized FSqrCodeReview object - just get it done.... for now. we will invest 
    // some more thoughts into it some time later.  
    private static final String CREATE_TABLE_CODE_REVIEWS = // 
                    "CREATE TABLE  " + CODE_REVIEW_TABLENAME + " (" + CODE_REVIEW_FK_PROJECTID_COLUMN + ", " + CODE_REVIEW_REVIEWID_COLUMN + ", "
                                    + CODE_REVIEW_REVIWEDATA_COLUMN + ", " + CODE_REVIEW_STATE_COLUMN + ");";

    private static final String DROP_TABLE_IF_EXISTS = // 
                    "DROP TABLE IF EXISTS " + CODE_REVIEW_TABLENAME + ";";

    private static final String SELECT_FROM_CODE_REVIEWS_PS = //
                    "SELECT * FROM " + CODE_REVIEW_TABLENAME + " WHERE (" + CODE_REVIEW_FK_PROJECTID_COLUMN + "=?1 AND " + CODE_REVIEW_REVIEWID_COLUMN
                                    + "=?2); ";

    private static final String SELECT_FROM_CODE_REVIEWS_WHERE_STATE_PS = //
                    "SELECT * FROM " + CODE_REVIEW_TABLENAME + " WHERE (" + CODE_REVIEW_FK_PROJECTID_COLUMN + "=?1 AND " + CODE_REVIEW_STATE_COLUMN + "=?2); ";

    private static final String INSERT_CODE_REVIEW_PS = //
                    "INSERT INTO " + CODE_REVIEW_TABLENAME + " (" + CODE_REVIEW_FK_PROJECTID_COLUMN + ", " + CODE_REVIEW_REVIEWID_COLUMN + ", "
                                    + CODE_REVIEW_REVIWEDATA_COLUMN + ", " + CODE_REVIEW_STATE_COLUMN + ") VALUES (?1,?2,?3,?4);";

    private static final String UPDATE_CODE_REVIEW_PS = //
                    "UPDATE " + CODE_REVIEW_TABLENAME + " SET " + CODE_REVIEW_REVIWEDATA_COLUMN + "=?3, " + CODE_REVIEW_STATE_COLUMN + "=?4 WHERE ( "
                                    + CODE_REVIEW_FK_PROJECTID_COLUMN + "=?1 AND " + CODE_REVIEW_REVIEWID_COLUMN + "=?2);";

    private FSqrDatabaseConnection connection;

    /**
     * 
     */
    public FSqrCodeReviewTableImpl() {
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
    public FSqrCodeReview selectCodeReview( String projectId, String reviewId ) {
        FSqrCodeReview result = null;

        try {

            PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_FROM_CODE_REVIEWS_PS );

            selectPS.setString( 1, projectId );
            selectPS.setString( 2, reviewId );

            ResultSet resultSet = selectPS.executeQuery();
            while (resultSet.next()) {
                result = createFSqrCodeReview( resultSet );

                // we only process the first result here.
                break;
            }
            resultSet.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    private FSqrCodeReview createFSqrCodeReview( ResultSet resultSet ) throws Exception {
        String reviewDataString = resultSet.getString( CODE_REVIEW_REVIWEDATA_COLUMN );

        return gson.fromJson( reviewDataString, FSqrCodeReview.class );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public List<FSqrCodeReview> selectOpenCodeReviews( String projectId ) {
        ArrayList<FSqrCodeReview> resultList = new ArrayList<>();

        try {
            PreparedStatement openReviews = this.connection.createPreparedStatement( SELECT_FROM_CODE_REVIEWS_WHERE_STATE_PS );

            openReviews.setString( 1, projectId );
            openReviews.setInt( 2, FSqrCodeReviewLifecycleState.toStateIndex( FSqrCodeReviewLifecycleState.Open ) );

            ResultSet resultSet = openReviews.executeQuery();

            while (resultSet.next()) {
                resultList.add( createFSqrCodeReview( resultSet ) );
            }

            resultSet.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Comparator<FSqrCodeReview> comparing = Comparator.comparing( FSqrCodeReview::getReviewId );
        resultList.sort( comparing );

        return resultList;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public List<FSqrCodeReview> selectRecentlyClosedReviews( String projectId ) {
        ArrayList<FSqrCodeReview> resultList = new ArrayList<>();

        // TODO: *Recently* closed - requires close date....

        try {
            PreparedStatement openReviews = this.connection.createPreparedStatement( SELECT_FROM_CODE_REVIEWS_WHERE_STATE_PS );

            openReviews.setString( 1, projectId );
            openReviews.setInt( 2, FSqrCodeReviewLifecycleState.toStateIndex( FSqrCodeReviewLifecycleState.Closed ) );

            ResultSet resultSet = openReviews.executeQuery();

            while (resultSet.next()) {
                resultList.add( createFSqrCodeReview( resultSet ) );
            }

            resultSet.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Comparator<FSqrCodeReview> comparing = Comparator.comparing( FSqrCodeReview::getReviewId );
        resultList.sort( comparing );

        return resultList;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void insertNewCodeReview( FSqrCodeReview codeReview ) {
        try {
            String serializedCodeReview = gson.toJson( codeReview );

            PreparedStatement insert = this.connection.createPreparedStatement( INSERT_CODE_REVIEW_PS );

            insert.setString( 1, codeReview.getProjectId() );
            insert.setString( 2, codeReview.getReviewId() );
            insert.setString( 3, serializedCodeReview );
            insert.setInt( 4, FSqrCodeReviewLifecycleState.toStateIndex( codeReview.getCurrentReviewState() ) );

            insert.addBatch();
            insert.executeBatch();

            this.connection.finishTransaction();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void updateCodeReview( FSqrCodeReview codeReview ) {

        try {
            String serializedCodeReview = gson.toJson( codeReview );

            PreparedStatement update = this.connection.createPreparedStatement( UPDATE_CODE_REVIEW_PS );

            // WHERE
            update.setString( 1, codeReview.getProjectId() );
            update.setString( 2, codeReview.getReviewId() );
            // SET
            update.setString( 3, serializedCodeReview );
            update.setInt( 4, FSqrCodeReviewLifecycleState.toStateIndex( codeReview.getCurrentReviewState() ) );

            update.addBatch();
            update.executeBatch();
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
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate( DROP_TABLE_IF_EXISTS );
            statement.executeUpdate( CREATE_TABLE_CODE_REVIEWS );
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
        // TODO:
        // -----
        // save into db files the codereviewtable to disk, maybe use gson for this, for some time until we have a
        // database up and running. But Basically the table is the schema.
    }

}
