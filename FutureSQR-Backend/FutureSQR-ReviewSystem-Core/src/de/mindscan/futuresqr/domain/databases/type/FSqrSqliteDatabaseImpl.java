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

    // ============
    // Column Names
    // ============

    // ScmUserAliases
    // --------------
    public static final SqliteDatabaseTableColumn SCM_USER_ALIASES_FK_USERUUID_COLUMN = new SqliteDatabaseTableColumn( SCM_USER_ALIASES_TABLE, "userUuid" );
    public static final SqliteDatabaseTableColumn SCM_USER_ALIASES_ALIASNAME_COLUMN = new SqliteDatabaseTableColumn( SCM_USER_ALIASES_TABLE, "aliasName" );

    // CodeReviewDiscussions
    // ---------------------
    // refactor to single FK(projectid, reviewId) 
    public static final SqliteDatabaseTableColumn CODE_REVIEW_DISCUSSIONS_FK_PROJECTID_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEW_DISCUSSIONS_TABLE,
                    "projectId" );
    // maybe use the UUID?, so we can drop the projectId...
    public static final SqliteDatabaseTableColumn CODE_REVIEW_DISCUSSIONS_FK_REVIEWID_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEW_DISCUSSIONS_TABLE,
                    "reviewId" );
    public static final SqliteDatabaseTableColumn CODE_REVIEW_DISCUSSIONS_FK_THREADUUID_COLUM = new SqliteDatabaseTableColumn( CODE_REVIEW_DISCUSSIONS_TABLE,
                    "threadUuid" );
    public static final SqliteDatabaseTableColumn CODE_REVIEW_DISCISSIONS_CREATED_TS_COLUMNS = new SqliteDatabaseTableColumn( CODE_REVIEW_DISCUSSIONS_TABLE,
                    "created" );
    // TODO maybe a last updated for each thread?...
    // TODO maybe we need a indicator, whether a full thread is resolved.
    // TODO maybe state whether thread was deleted...

    // CoreReviewScmRevisions
    public static final SqliteDatabaseTableColumn CODE_REVIEW_SCM_REVISIONS_FK_PROJECTID_COLUMN = new SqliteDatabaseTableColumn(
                    CODE_REVIEW_SCM_REVISIONS_TABLE, "projectId" );
    public static final SqliteDatabaseTableColumn CODE_REVIEW_SCM_REVISIONS_FK_REVIEWID_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEW_SCM_REVISIONS_TABLE,
                    "reviewId" );
    public static final SqliteDatabaseTableColumn CODE_REVIEW_SCM_REVISIONS_SCMREVISIONID_COLUMN = new SqliteDatabaseTableColumn(
                    CODE_REVIEW_SCM_REVISIONS_TABLE, "revisionId" );

    // CodeReviews
    // -----------
    // TODO: reviewUuid
    public static final SqliteDatabaseTableColumn CODE_REVIEWS_FK_PROJECTID_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEWS_TABLE, "projectId" );
    // TODO: project Branch
    public static final SqliteDatabaseTableColumn CODE_REVIEWS_STATE_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEWS_TABLE, "state" );
    public static final SqliteDatabaseTableColumn CODE_REVIEWS_REVIEWID_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEWS_TABLE, "reviewId" );
    public static final SqliteDatabaseTableColumn CODE_REVIEWS_REVIWEDATA_COLUMN = new SqliteDatabaseTableColumn( CODE_REVIEWS_TABLE, "reviewData" );

    // DiscussionThread
    // ---------------
    public static final SqliteDatabaseTableColumn DISCUSSION_THREAD_PK_UUID_COLUMN = new SqliteDatabaseTableColumn( DISCUSSION_THREAD_TABLE, "uuid" );
    public static final SqliteDatabaseTableColumn DISCUSSION_THREAD_THREADDATA_COLUMN = new SqliteDatabaseTableColumn( DISCUSSION_THREAD_TABLE, "threadData" );

    // ScmConfiguration
    // ----------------
    public static final SqliteDatabaseTableColumn SCM_CONFIGURATION_PK_PROJECTID_COLUMN = new SqliteDatabaseTableColumn( SCM_CONFIGURATION_TABLE, "projectId" );
    public static final SqliteDatabaseTableColumn SCM_CONFIGURATION_SCMCONFIGDATA_COLUMN = new SqliteDatabaseTableColumn( SCM_CONFIGURATION_TABLE,
                    "scmConfigData" );

    // StarredProjects
    public static final String STARRED_PROJECTS_FK_USERUUID_COLUM = "userUuid";
    public static final String STARRED_PROJECTS_FK_PROJECTID_COLUUMN = "projectId";
    public static final String STARRED_PROJECTS_STARRED_TS_COLUMN = "whenStarred";

    // SystemUsers
    public static final String SYSTEM_USERS_PK_UUID_COLUMN = "uuid";
    public static final String SYSTEM_USERS_LOGINNAME_COLUMN = "userLoginName";
    public static final String SYSTEM_USERS_DISPLAYNAME_COLUMN = "userDisplayName";
    public static final String SYSTEM_USERS_EMAIL_COLUMN = "userEmail";
    public static final String SYSTEM_USERS_AVATARLOCATION_COLUMN = "avatarLocation";
    public static final String SYSTEM_USERS_ISBANNED_COLUMN = "isBanned";
    // TODO: CREATED DATE_COLUMN
    // TODO: MODIFIED DATE COLUMN
    // TODO: BANNED DATE COLUMN

    // ==============
    // Drop If Exists
    // ==============
    public static final String QUERY_SCM_USER_ALIASES_DROP_TABLE = //
                    "DROP TABLE IF EXISTS " + SCM_USER_ALIASES_TABLENAME + ";";

    public static final String QUERY_CODE_REVIEWS_DROP_TABLE = // 
                    "DROP TABLE IF EXISTS " + CODE_REVIEWS_TABLENAME + ";";

    public static final String QUERY_DISCUSSION_THREAD_DROP_TABLE = // 
                    "DROP TABLE IF EXISTS " + DISCUSSION_THREAD_TABLENAME + ";";

    public static final String QUERY_CODE_REVIEW_DISCUSSIONS_DROP_TABLE = //
                    "DROP TABLE IF EXISTS " + CODE_REVIEW_DISCUSSIONS_TABLENAME + ";";

    public static final String QUERY_CODE_REVIEW_SCM_REVISIONS_DROP_TABLE = //
                    "DROP TABLE IF EXISTS " + CODE_REVIEW_SCM_REVISIONS_TABLENAME + ";";

    public static final String QUERY_SCM_CONFIGURATION_DROP_TABLE = //
                    "DROP TABLE IF EXISTS " + SCM_CONFIGURATION_TABLENAME + ";";

    public static final String QUERY_STARRED_PROJECTS_DROP_TABLE = // 
                    "DROP TABLE IF EXISTS " + STARRED_PROJECTS_TABLENAME + ";";

    public static final String QUERY_SYSTEM_USERS_DROP_TABLE = // 
                    "DROP TABLE IF EXISTS " + SYSTEM_USERS_TABLENAME + ";";

    // =============
    // Create Tables
    // =============
    public static final String QUERY_SCM_USER_ALIASES_CREATE_TABLE = // 
                    "CREATE TABLE " + SCM_USER_ALIASES_TABLENAME + //
                                    " (" + SCM_USER_ALIASES_FK_USERUUID_COLUMN.getColumnName() + //
                                    ", " + SCM_USER_ALIASES_ALIASNAME_COLUMN.getColumnName() + ");";

    public static final String QUERY_CODE_REVIEWS_CREATE_TABLE = // 
                    "CREATE TABLE  " + CODE_REVIEWS_TABLENAME + //
                                    " (" + CODE_REVIEWS_FK_PROJECTID_COLUMN + //
                                    ", " + CODE_REVIEWS_REVIEWID_COLUMN + //
                                    ", " + CODE_REVIEWS_REVIWEDATA_COLUMN + //
                                    ", " + CODE_REVIEWS_STATE_COLUMN + ");";

    public static final String QUERY_DISCUSSION_THREAD_CREATE_TABLE = //
                    "CREATE TABLE " + DISCUSSION_THREAD_TABLENAME + //
                                    " (" + DISCUSSION_THREAD_PK_UUID_COLUMN + //
                                    ", " + DISCUSSION_THREAD_THREADDATA_COLUMN + ");";

    public static final String QUERY_CODE_REVIEW_DISCUSSIONS_CREATE_TABLE = //
                    "CREATE TABLE  " + CODE_REVIEW_DISCUSSIONS_TABLENAME + //
                                    " (" + CODE_REVIEW_DISCUSSIONS_FK_PROJECTID_COLUMN + //
                                    ", " + CODE_REVIEW_DISCUSSIONS_FK_REVIEWID_COLUMN + //
                                    ", " + CODE_REVIEW_DISCUSSIONS_FK_THREADUUID_COLUM + //
                                    ", " + CODE_REVIEW_DISCISSIONS_CREATED_TS_COLUMNS + ");";

    public static final String QUERY_CODE_REVIEW_SCM_REVISIONS_CREATE_TABLE = // 
                    "CREATE TABLE  " + CODE_REVIEW_SCM_REVISIONS_TABLENAME + //
                                    " (" + CODE_REVIEW_SCM_REVISIONS_FK_PROJECTID_COLUMN + //
                                    ", " + CODE_REVIEW_SCM_REVISIONS_SCMREVISIONID_COLUMN + // 
                                    ", " + CODE_REVIEW_SCM_REVISIONS_FK_REVIEWID_COLUMN + ");";

    public static final String QUERY_SCM_CONFIGURATION_CREATE_TABLE = //
                    "CREATE TABLE " + SCM_CONFIGURATION_TABLENAME + //
                                    " (" + SCM_CONFIGURATION_PK_PROJECTID_COLUMN + //
                                    ", " + SCM_CONFIGURATION_SCMCONFIGDATA_COLUMN + "); ";

    public static final String QUERY_STARRED_PROJECTS_CREATE_TABLE = //
                    "CREATE TABLE  " + STARRED_PROJECTS_TABLENAME + //
                                    " (" + STARRED_PROJECTS_FK_USERUUID_COLUM + //
                                    ", " + STARRED_PROJECTS_FK_PROJECTID_COLUUMN + //
                                    ", " + STARRED_PROJECTS_STARRED_TS_COLUMN + ");";

    public static final String QUERY_SYSTEM_USERS_CREATE_TABLE = //
                    "CREATE TABLE " + SYSTEM_USERS_TABLENAME + // 
                                    " ( " + SYSTEM_USERS_PK_UUID_COLUMN + //
                                    ", " + SYSTEM_USERS_LOGINNAME_COLUMN + //
                                    ", " + SYSTEM_USERS_DISPLAYNAME_COLUMN + //
                                    ", " + SYSTEM_USERS_EMAIL_COLUMN + //
                                    ", " + SYSTEM_USERS_AVATARLOCATION_COLUMN + //
                                    ", " + SYSTEM_USERS_ISBANNED_COLUMN + ");";

//    /** 
//     * {@inheritDoc}
//     */
//    @Override
//    public void setDatbaseConnection( FSqrDatabaseConnection connection ) {
//        // TODO Auto-generated method stub
//
//    }

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

    // TODO: createDatabase
    // TODO: createTables()
    public void createTables() {
        // TODO: get registered tables ...
        Collection<SqliteDatabaseTable> allTables = new ArrayList<>();

        // TODO: foreach call
        for (SqliteDatabaseTable table : allTables) {
            table.createTable( /* TODO: DatabaseConnection? */ );
        }
    }

    // TODO: dropDatabase

    // TODO: dropTables();
    public void dropTables() {
        // TODO: get registered tables ... 
        Collection<SqliteDatabaseTable> allTables = new ArrayList<>();

        // TODO: foreach call
        for (SqliteDatabaseTable table : allTables) {
            table.dropTable( /* TODO: DatabaseConnection? */ );
        }
    }
}
