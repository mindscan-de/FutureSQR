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

import com.google.gson.Gson;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.FSqrProjectAssignedCodeReviewsTable;

/**
 * 
 */
public class FSqrProjectAssignedCodeReviewsTableImpl implements FSqrProjectAssignedCodeReviewsTable {

    private static final String REVISION_TO_CODE_REVIEW_TABLENAME = "RevisionsToReviews";

    private static final String DROP_TABLE_IF_EXISTS = // 
                    "DROP TABLE IF EXISTS " + REVISION_TO_CODE_REVIEW_TABLENAME + ";";

    private static final String CREATE_TABLE_REVISION_TO_REVIEW = // 
                    "CREATE TABLE  " + REVISION_TO_CODE_REVIEW_TABLENAME + " (projectId, revisionId, reviewId);";

    private Gson gson = new Gson();
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
        try {

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void createTable() {
        try {
            Statement statement = this.connection.createStatement();
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
