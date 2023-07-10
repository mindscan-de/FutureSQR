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
import de.mindscan.futuresqr.domain.databases.FSqrScmRevisionsTable;
import de.mindscan.futuresqr.domain.databases.type.FSqrSqliteDatabaseImpl;
import de.mindscan.futuresqr.domain.model.FSqrRevision;

/**
 * 
 */
public class FSqrScmRevisionsTableImpl implements FSqrScmRevisionsTable {

    private static final String INSERT_SCM_REVISION = //
                    "INSERT INTO " + FSqrSqliteDatabaseImpl.getScmRevisionsTable().tableName() + //
                                    " (" + FSqrSqliteDatabaseImpl.SCM_REVISIONS_PK_UUID_COLUMN.getColumnName() + // 
                                    ", " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_FK_PROJECTID_COLUMN.getColumnName() + //
                                    ", " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_BRANCH_COLUMN.getColumnName() + //
                                    // 
                                    ", " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_SCM_REVISIONID_COLUMN.getColumnName() + //

                                    // TODO: revisionId
                                    // TODO: ShortRevisionId
                                    // TODO: authorname
                                    // TODO: authorid
                                    // TODO: authoruuid,
                                    // TODO: revisionTimestamp
                                    // TODO: revisionDate
                                    // TODO: revisionShortDate
                                    // TODO: revisionRelativeDate (dynamic calculation)
                                    // TODO: commitMessageFull
                                    // TODO: parentIds : list<string>
                                    // TODO: shortParentIds : list<string>
                                    // TODO: hasAttachedReviews (dynamic calculation)
                                    // TODO: reviewId (calculation from other table)
                                    // TODO: reviewClosed ()
                                    // TODO: branchname...
                                    // TODO: filter these fields down from ScmBasicInformation?

                                    ", " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_DATA_COLUMN.getColumnName() + //

                                    ") VALUES (?1, ?2, ?3, ?4, ?5);";

    private static final String SELECT_SCM_REVISION = //
                    "SELECT * FROM " + FSqrSqliteDatabaseImpl.getScmRevisionsTable().tableName() + //
                                    " WHERE )" + FSqrSqliteDatabaseImpl.SCM_REVISIONS_FK_PROJECTID_COLUMN.getColumnName() + "=?1" + //
                                    " AND " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_SCM_REVISIONID_COLUMN.getColumnName() + "=?2 );";;

    private FSqrDatabaseConnection connection;

    private Gson gson = new Gson();

    /**
     * 
     */
    public FSqrScmRevisionsTableImpl() {
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
    public void insertScmRevision( String projectId, FSqrRevision revision ) {
        try (PreparedStatement insertPS = this.connection.createPreparedStatement( INSERT_SCM_REVISION )) {
            String serializedRevision = gson.toJson( revision );

            insertPS.setString( 1, revision.getOrComputeRevisionUUID() );
            insertPS.setString( 2, projectId );
            insertPS.setString( 3, revision.getBranchName() );
            insertPS.setString( 4, revision.getRevisionId() );
            insertPS.setString( 5, serializedRevision );

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
    public FSqrRevision selectScmRevision( String projectId, String revisionId ) {
        FSqrRevision result = null;

        try (PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_SCM_REVISION )) {
            selectPS.setString( 1, projectId );
            selectPS.setString( 2, revisionId );

            try (ResultSet resultSet = selectPS.executeQuery()) {
                if (resultSet.next()) {
                    result = createRevision( resultSet );
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private FSqrRevision createRevision( ResultSet resultSet ) throws Exception {
        String serializedRevision = resultSet.getString( FSqrSqliteDatabaseImpl.SCM_REVISIONS_DATA_COLUMN.getColumnName() );

        return gson.fromJson( serializedRevision, FSqrRevision.class );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void createTable() {
        // intentionally left blank
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        // intentionally left blank
    }

}
