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

/**
 * 
 */
public class FSqrSqliteDatabaseImpl {

    // ==========
    // TableNames
    // ==========
    public static final String SCM_USER_ALIASES_TABLENAME = "ScmUserAliases";
    // TODO Rename to EA Model truth.
    public static final String CODE_REVIEW_DISCUSSIONS_TABLENAME = "ReviewDiscussions";
    public static final String CODE_REVIEW_SCM_REVISIONS_TABLENAME = "RevisionsToReviews";
    public static final String CODE_REVIEWS_TABLENAME = "CodeReviews";
    public static final String DISCUSSION_THREAD_TABLENAME = "DiscussionThread";

    // ============
    // Column Names
    // ============

    // ScmUserAliases
    // --------------
    public static final String SCM_USER_ALIASES_FK_USERUUID_COLUMN = "userUuid";
    public static final String SCM_USER_ALIASES_ALIASNAME_COLUMN = "aliasName";

    // CodeReviewDiscussions
    // ---------------------
    // refactor to single FK(projectid, reviewId) 
    public static final String CODE_REVIEW_DISCUSSIONS_FK_PROJECTID_COLUMN = "projectId";
    // maybe use the UUID?, so we can drop the projectId...
    public static final String CODE_REVIEW_DISCUSSIONS_FK_REVIEWID_COLUMN = "reviewId";
    public static final String CODE_REVIEW_DISCUSSIONS_FK_THREADUUID_COLUM = "threadUuid";
    public static final String CODE_REVIEW_DISCISSIONS_CREATED_TS_COLUMNS = "created";
    // TODO maybe a last updated for each thread?...
    // TODO maybe we need a indicator, whether a full thread is resolved.
    // TODO maybe state whether thread was deleted...

    // CoreReviewScmRevisions
    public static final String CODE_REVIEW_SCM_REVISIONS_FK_PROJECTID_COLUMN = "projectId";
    public static final String CODE_REVIEW_SCM_REVISIONS_FK_REVIEWID_COLUMN = "reviewId";
    public static final String CODE_REVIEW_SCM_REVISIONS_SCMREVISIONID_COLUMN = "revisionId";

    // CodeReviews
    // -----------
    // TODO: reviewUuid
    public static final String CODE_REVIEWS_FK_PROJECTID_COLUMN = "projectId";
    // TODO: project Branch
    public static final String CODE_REVIEWS_STATE_COLUMN = "state";
    public static final String CODE_REVIEWS_REVIEWID_COLUMN = "reviewId";
    public static final String CODE_REVIEWS_REVIWEDATA_COLUMN = "reviewData";

    // DicussionThread
    // ---------------
    public static final String DISCUSSION_THREAD_PK_UUID_COLUMN = "uuid";
    public static final String DISCUSSION_THREAD_THREADDATA_COLUMN = "threadData";

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

    // =============
    // Create Tables
    // =============
    public static final String QUERY_SCM_USER_ALIASES_CREATE_TABLE = // 
                    "CREATE TABLE " + SCM_USER_ALIASES_TABLENAME + //
                                    " (" + SCM_USER_ALIASES_FK_USERUUID_COLUMN + //
                                    ", " + SCM_USER_ALIASES_ALIASNAME_COLUMN + ");";

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

}
