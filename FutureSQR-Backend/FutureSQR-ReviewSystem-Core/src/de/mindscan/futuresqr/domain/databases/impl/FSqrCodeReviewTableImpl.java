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
    public void create() {
        // TODO Auto-generated method stub

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
