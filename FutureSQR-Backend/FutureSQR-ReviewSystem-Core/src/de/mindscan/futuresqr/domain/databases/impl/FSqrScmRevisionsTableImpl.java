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
import java.util.List;

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
                                    // TODO: branchName...
                                    // TODO: filter these fields down from ScmBasicInformation?

                                    ", " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_DATA_COLUMN.getColumnName() + //
                                    " ) VALUES (?1, ?2, ?3, ?4, ?5);";

    private static final String UPDATE_SCM_REVISION_BY_UUID = //
                    "UPDATE " + FSqrSqliteDatabaseImpl.getDiscussionThreadTable().tableName() + //;
                                    " SET " + // 
                                    // TODO: projectid
                                    // TODO: branch
                                    // TODO: revisionid
                                    // TODO: serialized data
                                    " WHERE " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_PK_UUID_COLUMN.getColumnName() + "=?1;";

    private static final String SELECT_SCM_REVISION = //
                    "SELECT * FROM " + FSqrSqliteDatabaseImpl.getScmRevisionsTable().tableName() + //
                                    " WHERE (" + FSqrSqliteDatabaseImpl.SCM_REVISIONS_FK_PROJECTID_COLUMN.getColumnName() + "=?1" + //
                                    " AND " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_SCM_REVISIONID_COLUMN.getColumnName() + "=?2 );";

    // TODO: select * from project + branch order by date descending -> list of revisions
//    private static final String SELECT_SCMREVISIONS_WHERE_PROJECT_BRANCH = //
//                    "SELECT * FROM " + FSqrSqliteDatabaseImpl.getScmRevisionsTable().tableName() + //
//                                    " WHERE ( " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_FK_PROJECTID_COLUMN.getColumnName() + "=?1" + //
//                                    " AND " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_BRANCH_COLUMN.getColumnName() + "=?2 ) " + //
//                                    " ORDER BY " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_COMMITDATE.getColumnName() + " DESC LIMIT 75;";

    // TODO: select * from project order by date limit 75 -> list of revisions
//  private static final String SELECT_SCMREVISIONS_WHERE_PROJECT = //
//  "SELECT * FROM " + FSqrSqliteDatabaseImpl.getScmRevisionsTable().tableName() + //
//                  " WHERE ( " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_FK_PROJECTID_COLUMN.getColumnName() + "=?1 )" + //
//                  " ORDER BY " + FSqrSqliteDatabaseImpl.SCM_REVISIONS_COMMITDATE.getColumnName() + " DESC LIMIT 75;";

    // TODO: select newest commit timestamp for project
    // TODO: select newest commit timestamp for project + branch.
    // TODO: select newest commits for project, distinct(branch)

    // TODO: calculate this newest commit for each project (distinct project)

    // TODO: calculate count of commits in the last 90 month, rank projects
    // a.k.a. most active projects

    // TODO: order projects by newest commit (distinct project)
    // a.k.a. most recent changed projects.

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

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrRevision selectScmHeadRevision( String projectId ) {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void updateScmRevision( String projectId, FSqrRevision revision ) {
        // TODO: Auto-generated method stub
        // TODO: is there a real reason to to update this object?
        // * maybe the author uuid? - but this can be calculated/mapped on the fly.
        //   using the scm aliases table...
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public List<FSqrRevision> selectScmRevisionsByProject( String projectId, int limit ) {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public List<FSqrRevision> selectScmRevisionsByProject( String projectId, String branch, int limit ) {
        // TODO Auto-generated method stub
        return null;
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
