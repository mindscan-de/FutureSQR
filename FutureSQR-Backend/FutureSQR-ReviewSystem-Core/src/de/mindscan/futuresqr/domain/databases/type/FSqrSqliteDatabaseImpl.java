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
package de.mindscan.futuresqr.domain.databases.type;

import java.util.ArrayList;
import java.util.Collection;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;

/**
 * 
 */
public class FSqrSqliteDatabaseImpl /* implements DatabaseConnectionSetter */ {

    // ==========
    // TableNames
    // ==========
    private static final String SCM_USER_ALIASES_TABLENAME = "ScmUserAliases";
    private static final SqliteDatabaseTable SCM_USER_ALIASES_TABLE = new SqliteDatabaseTable( SCM_USER_ALIASES_TABLENAME );

    // TODO Rename to EA Model truth.
    private static final String CODE_REVIEW_DISCUSSIONS_TABLENAME = "ReviewDiscussions";
    private static final SqliteDatabaseTable CODE_REVIEW_DISCUSSIONS_TABLE = new SqliteDatabaseTable( CODE_REVIEW_DISCUSSIONS_TABLENAME );

    private static final String CODE_REVIEW_SCM_REVISIONS_TABLENAME = "RevisionsToReviews";
    private static final SqliteDatabaseTable CODE_REVIEW_SCM_REVISIONS_TABLE = new SqliteDatabaseTable( CODE_REVIEW_SCM_REVISIONS_TABLENAME );

    private static final String CODE_REVIEWS_TABLENAME = "CodeReviews";
    private static final SqliteDatabaseTable CODE_REVIEWS_TABLE = new SqliteDatabaseTable( CODE_REVIEWS_TABLENAME );

    private static final String DISCUSSION_THREAD_TABLENAME = "DiscussionThread";
    private static final SqliteDatabaseTable DISCUSSION_THREAD_TABLE = new SqliteDatabaseTable( DISCUSSION_THREAD_TABLENAME );

    private static final String SCM_CONFIGURATION_TABLENAME = "ScmConfigurations";
    private static final SqliteDatabaseTable SCM_CONFIGURATION_TABLE = new SqliteDatabaseTable( SCM_CONFIGURATION_TABLENAME );

    private static final String STARRED_PROJECTS_TABLENAME = "StarredProjects";
    private static final SqliteDatabaseTable STARRED_PROJECTS_TABLE = new SqliteDatabaseTable( STARRED_PROJECTS_TABLENAME );

    private static final String SYSTEM_USERS_TABLENAME = "SystemUsers";
    private static final SqliteDatabaseTable SYSTEM_USERS_TABLE = new SqliteDatabaseTable( SYSTEM_USERS_TABLENAME );

    private static final String SCM_REVISIONS = "ScmRevisions";
    private static final SqliteDatabaseTable SCM_REVISIONS_TABLE = new SqliteDatabaseTable( SCM_REVISIONS );

    // ============
    // Column Names
    // ============

    // ScmUserAliases
    // --------------
    public static final SqliteDatabaseTableColumn SCM_USER_ALIASES_FK_USERUUID_COLUMN = new SqliteDatabaseTableColumn( SCM_USER_ALIASES_TABLE, "userUuid",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn SCM_USER_ALIASES_ALIASNAME_COLUMN = new SqliteDatabaseTableColumn( SCM_USER_ALIASES_TABLE, "aliasName",
                    SqliteDatabaseTableColumnType.TEXT );

    // CodeReviewDiscussions
    // ---------------------
    // refactor to single FK(projectid, reviewId) 
    public static final SqliteDatabaseTableColumn CODE_REVIEW_DISCUSSIONS_FK_PROJECTID_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEW_DISCUSSIONS_TABLE,
                    "projectId", SqliteDatabaseTableColumnType.TEXT );
    // maybe use the UUID?, so we can drop the projectId...
    public static final SqliteDatabaseTableColumn CODE_REVIEW_DISCUSSIONS_FK_REVIEWID_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEW_DISCUSSIONS_TABLE,
                    "reviewId", SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn CODE_REVIEW_DISCUSSIONS_FK_THREADUUID_COLUM = new SqliteDatabaseTableColumn( CODE_REVIEW_DISCUSSIONS_TABLE,
                    "threadUuid", SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn CODE_REVIEW_DISCISSIONS_CREATED_TS_COLUMNS = new SqliteDatabaseTableColumn( CODE_REVIEW_DISCUSSIONS_TABLE,
                    // TODO FIXME LATER
                    "created", SqliteDatabaseTableColumnType.NYI );
    // TODO maybe a last updated for each thread?...
    // TODO maybe we need a indicator, whether a full thread is resolved.
    // TODO maybe state whether thread was deleted...

    // CoreReviewScmRevisions
    public static final SqliteDatabaseTableColumn CODE_REVIEW_SCM_REVISIONS_FK_PROJECTID_COLUMN = new SqliteDatabaseTableColumn(
                    CODE_REVIEW_SCM_REVISIONS_TABLE, "projectId", SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn CODE_REVIEW_SCM_REVISIONS_FK_REVIEWID_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEW_SCM_REVISIONS_TABLE,
                    "reviewId", SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn CODE_REVIEW_SCM_REVISIONS_SCMREVISIONID_COLUMN = new SqliteDatabaseTableColumn(
                    CODE_REVIEW_SCM_REVISIONS_TABLE, "revisionId", SqliteDatabaseTableColumnType.TEXT );

    // CodeReviews
    // -----------
    // TODO: reviewUuid
    public static final SqliteDatabaseTableColumn CODE_REVIEWS_FK_PROJECTID_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEWS_TABLE, "projectId",
                    SqliteDatabaseTableColumnType.TEXT );
    // TODO: project Branch
    public static final SqliteDatabaseTableColumn CODE_REVIEWS_STATE_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEWS_TABLE, "state",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn CODE_REVIEWS_REVIEWID_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEWS_TABLE, "reviewId",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn CODE_REVIEWS_REVIWEDATA_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEWS_TABLE, "reviewData",
                    SqliteDatabaseTableColumnType.TEXT );

    // DiscussionThread
    // ---------------
    public static final SqliteDatabaseTableColumn DISCUSSION_THREAD_PK_UUID_COLUMN = new SqliteDatabaseTableColumn( DISCUSSION_THREAD_TABLE, "uuid",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn DISCUSSION_THREAD_THREADDATA_COLUMN = new SqliteDatabaseTableColumn( DISCUSSION_THREAD_TABLE, "threadData",
                    SqliteDatabaseTableColumnType.TEXT );

    // ScmConfiguration
    // ----------------
    public static final SqliteDatabaseTableColumn SCM_CONFIGURATION_PK_PROJECTID_COLUMN = new SqliteDatabaseTableColumn( SCM_CONFIGURATION_TABLE, "projectId",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn SCM_CONFIGURATION_SCMCONFIGDATA_COLUMN = new SqliteDatabaseTableColumn( SCM_CONFIGURATION_TABLE,
                    "scmConfigData", SqliteDatabaseTableColumnType.TEXT );

    // StarredProjects
    // ---------------
    public static final SqliteDatabaseTableColumn STARRED_PROJECTS_FK_USERUUID_COLUM = new SqliteDatabaseTableColumn( STARRED_PROJECTS_TABLE, "userUuid",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn STARRED_PROJECTS_FK_PROJECTID_COLUUMN = new SqliteDatabaseTableColumn( STARRED_PROJECTS_TABLE, "projectId",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn STARRED_PROJECTS_STARRED_TS_COLUMN = new SqliteDatabaseTableColumn( STARRED_PROJECTS_TABLE, "whenStarred",
                    SqliteDatabaseTableColumnType.TEXT );

    // SystemUsers
    // -----------
    public static final SqliteDatabaseTableColumn SYSTEM_USERS_PK_UUID_COLUMN = new SqliteDatabaseTableColumn( SYSTEM_USERS_TABLE, "uuid",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn SYSTEM_USERS_LOGINNAME_COLUMN = new SqliteDatabaseTableColumn( SYSTEM_USERS_TABLE, "userLoginName",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn SYSTEM_USERS_DISPLAYNAME_COLUMN = new SqliteDatabaseTableColumn( SYSTEM_USERS_TABLE, "userDisplayName",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn SYSTEM_USERS_EMAIL_COLUMN = new SqliteDatabaseTableColumn( SYSTEM_USERS_TABLE, "userEmail",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn SYSTEM_USERS_AVATARLOCATION_COLUMN = new SqliteDatabaseTableColumn( SYSTEM_USERS_TABLE, "avatarLocation",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn SYSTEM_USERS_ISBANNED_COLUMN = new SqliteDatabaseTableColumn( SYSTEM_USERS_TABLE, "isBanned",
                    SqliteDatabaseTableColumnType.BOOL );
    // TODO: CREATED DATE_COLUMN
    // TODO: MODIFIED DATE COLUMN
    // TODO: BANNED DATE COLUMN

    // ScmRevisions
    // ------------
    public static final SqliteDatabaseTableColumn SCM_REVISIONS_PK_UUID_COLUMN = new SqliteDatabaseTableColumn( SCM_REVISIONS_TABLE, "scmRevisionsUuid",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn SCM_REVISIONS_FK_PROJECTID_COLUMN = new SqliteDatabaseTableColumn( SCM_REVISIONS_TABLE, "projectId",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn SCM_REVISIONS_BRANCH_COLUMN = new SqliteDatabaseTableColumn( SCM_REVISIONS_TABLE, "scmBranch",
                    SqliteDatabaseTableColumnType.TEXT );
    public static final SqliteDatabaseTableColumn SCM_REVISIONS_SCM_REVISIONID_COLUMN = new SqliteDatabaseTableColumn( SCM_REVISIONS_TABLE, "scmRevisionId",
                    SqliteDatabaseTableColumnType.TEXT );
    // TODO: Revision DATA....

    /**
     * @return the codeReviewDiscussionsTable
     */
    public static SqliteDatabaseTable getCodeReviewDiscussionsTable() {
        return CODE_REVIEW_DISCUSSIONS_TABLE;
    }

    /**
     * @return the codeReviewScmRevisionsTable
     */
    public static SqliteDatabaseTable getCodeReviewScmRevisionsTable() {
        return CODE_REVIEW_SCM_REVISIONS_TABLE;
    }

    /**
     * @return the codeReviewsTable
     */
    public static SqliteDatabaseTable getCodeReviewsTable() {
        return CODE_REVIEWS_TABLE;
    }

    /**
     * @return the discussionThreadTable
     */
    public static SqliteDatabaseTable getDiscussionThreadTable() {
        return DISCUSSION_THREAD_TABLE;
    }

    /**
     * @return the scmConfigurationTable
     */
    public static SqliteDatabaseTable getScmConfigurationTable() {
        return SCM_CONFIGURATION_TABLE;
    }

    /**
     * @return the systemUsersTable
     */
    public static SqliteDatabaseTable getSystemUsersTable() {
        return SYSTEM_USERS_TABLE;
    }

    /**
     * @return the starredProjectsTable
     */
    public static SqliteDatabaseTable getStarredProjectsTable() {
        return STARRED_PROJECTS_TABLE;
    }

    /**
     * @return the scmUserAliasesTable
     */
    public static SqliteDatabaseTable getScmUserAliasesTable() {
        return SCM_USER_ALIASES_TABLE;
    }

    /**
     * @return
     */
    public static SqliteDatabaseTable getScmRevisionsTable() {
        return SCM_REVISIONS_TABLE;
    }

    private Collection<SqliteDatabaseTable> databaseTables;

    // ----------------------------------
    // instance part ....
    // ----------------------------------

    /**
     * 
     */
    public FSqrSqliteDatabaseImpl() {
        // TODO: initialize the database tables... (empty) obviously.
        // database name?
        this.databaseTables = new ArrayList<>();

        // TODO: rework this initialization later.
        // this should be part of the new command see SqliteDatabaseTableColumn for better understanding.
        // because this current particular initialization is kind of problematic. This can be reworked
        this.addTableToDatabase( SCM_USER_ALIASES_TABLE );
        this.addTableToDatabase( CODE_REVIEW_DISCUSSIONS_TABLE );
        this.addTableToDatabase( CODE_REVIEW_SCM_REVISIONS_TABLE );
        this.addTableToDatabase( CODE_REVIEWS_TABLE );
        this.addTableToDatabase( DISCUSSION_THREAD_TABLE );
        this.addTableToDatabase( SCM_CONFIGURATION_TABLE );
        this.addTableToDatabase( STARRED_PROJECTS_TABLE );
        this.addTableToDatabase( SYSTEM_USERS_TABLE );
        this.addTableToDatabase( SCM_REVISIONS_TABLE );

    }

//  /** 
//  * {@inheritDoc}
//  */
// @Override
// public void setDatbaseConnection( FSqrDatabaseConnection connection ) {
//     // TODO Auto-generated method stub
//
// }

    public void createDatabase() {
        // intentionally left blank 
        // SQLite doesn't support this particular command.
    }

    public void dropDatbase() {
        // intentionally left blank 
        // SQLite doesn't support this particular command.
    }

    private void addTableToDatabase( SqliteDatabaseTable table ) {
        this.databaseTables.add( table );
    }

    private Collection<SqliteDatabaseTable> getDatabaseTables() {
        return new ArrayList<>( this.databaseTables );
    }

    public void createTables( FSqrDatabaseConnection dbConnection ) {
        Collection<SqliteDatabaseTable> allTables = getDatabaseTables();

        System.out.println( "creating tables" );
        for (SqliteDatabaseTable table : allTables) {
            table.createTable( dbConnection );
        }
    }

    public void dropTables( FSqrDatabaseConnection dbConnection ) {
        Collection<SqliteDatabaseTable> allTables = getDatabaseTables();

        System.out.println( "dropping tables" );
        for (SqliteDatabaseTable table : allTables) {
            table.dropTable( dbConnection );
        }
    }

}
