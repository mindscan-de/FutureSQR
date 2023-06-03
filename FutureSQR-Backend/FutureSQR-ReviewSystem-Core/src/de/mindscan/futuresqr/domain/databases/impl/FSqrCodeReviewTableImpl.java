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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.FSqrCodeReviewTable;
import de.mindscan.futuresqr.domain.model.FSqrCodeReview;

/**
 * This is a fake implementation for the Database CodeReview table, to work
 * on the logic and handling of a database persistence before adopting a 
 * certain database.
 */
public class FSqrCodeReviewTableImpl implements FSqrCodeReviewTable {

    private static final String CODE_REVIEW_TABLENAME = "CodeReviews";

    // TODO: add the columnNames.

    private static final String CREATE_TABLE_CODE_REVIEWS = "CREATE TABLE  " + CODE_REVIEW_TABLENAME + " ();";
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS " + CODE_REVIEW_TABLENAME + ";";
    private static final String SELECT_FROM_CODE_REVIEWS = "SELECT * from " + CODE_REVIEW_TABLENAME + " WHERE projectId=? AND reviewId=?";

    private Map<String, Map<String, FSqrCodeReview>> projectIdReviewIdToCodeReviewTable;

    private FSqrDatabaseConnection connection;

    /**
     * 
     */
    public FSqrCodeReviewTableImpl() {
        this.projectIdReviewIdToCodeReviewTable = new HashMap<>();
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

        // Make sure the list doesn't grow on read operation.
        if (this.projectIdReviewIdToCodeReviewTable.containsKey( projectId )) {
            return getProjectMapOrCompute( projectId ).getOrDefault( reviewId, null );
        }

        try {
            PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_FROM_CODE_REVIEWS );

            selectPS.setString( 1, projectId );
            selectPS.setString( 2, reviewId );

            ResultSet resultSet = selectPS.executeQuery();
            while (resultSet.next()) {
                // TODO convert a resultset-item () into a CodeReview.

            }
            resultSet.close();

        }
        catch (Exception e) {
            return null;
        }

        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public List<FSqrCodeReview> selectOpenCodeReviews( String projectId ) {
        ArrayList<FSqrCodeReview> resultList = new ArrayList<>();

        if (this.projectIdReviewIdToCodeReviewTable.containsKey( projectId )) {
            this.projectIdReviewIdToCodeReviewTable.get( projectId ).values().stream().filter( FSqrCodeReview::isOpenCodeReview )
                            .forEach( r -> resultList.add( r ) );
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

        if (this.projectIdReviewIdToCodeReviewTable.containsKey( projectId )) {
            this.projectIdReviewIdToCodeReviewTable.get( projectId ).values().stream().filter( FSqrCodeReview::isClosedCodeReview )
                            .forEach( r -> resultList.add( r ) );
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
        this.getProjectMapOrCompute( codeReview.getProjectId() ).put( codeReview.getReviewId(), codeReview );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void updateCodeReview( FSqrCodeReview codeReview ) {
        this.getProjectMapOrCompute( codeReview.getProjectId() ).put( codeReview.getReviewId(), codeReview );
    }

    private Map<String, FSqrCodeReview> getProjectMapOrCompute( String projectId ) {
        return this.projectIdReviewIdToCodeReviewTable.computeIfAbsent( projectId, k -> new HashMap<>() );
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
            // TODO: handle exception
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
