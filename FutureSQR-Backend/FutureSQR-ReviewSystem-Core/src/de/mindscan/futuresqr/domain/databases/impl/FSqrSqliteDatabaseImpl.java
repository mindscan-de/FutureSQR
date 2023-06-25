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

/**
 * 
 */
public class FSqrSqliteDatabaseImpl {

    // ----------
    // TableNames
    // ----------
    static final String SCM_USER_ALIASES_TABLENAME = "ScmUserAliases";
    static final String CODE_REVIEWS_TABLENAME = "CodeReviews";

    // ------------
    // Column Names
    // ------------

    // ScmUserAliases
    static final String SCM_USER_ALIASES_FK_USERUUID_COLUMN = "userUuid";
    static final String SCM_USER_ALIASES_ALIASNAME_COLUMN = "aliasName";

    // CodeReviews
    // TODO: reviewUuid
    static final String CODE_REVIEWS_FK_PROJECTID_COLUMN = "projectId";
    // TODO: project Branch
    static final String CODE_REVIEWS_STATE_COLUMN = "state";
    static final String CODE_REVIEWS_REVIEWID_COLUMN = "reviewId";
    static final String CODE_REVIEWS_REVIWEDATA_COLUMN = "reviewData";

    // --------------
    // Drop If Exists
    // --------------
    static final String QUERY_SCM_USER_ALIASES_DROP_TABLE = //
                    "DROP TABLE IF EXISTS " + SCM_USER_ALIASES_TABLENAME + ";";

    static final String QUERY_CODE_REVIEWS_DROP_TABLE = // 
                    "DROP TABLE IF EXISTS " + CODE_REVIEWS_TABLENAME + ";";

    // -------------
    // Create Tables
    // -------------
    static final String QUERY_SCM_USER_ALIASES_CREATE_TABLE = // 
                    "CREATE TABLE " + SCM_USER_ALIASES_TABLENAME + //
                                    " (" + SCM_USER_ALIASES_FK_USERUUID_COLUMN + //
                                    ", " + SCM_USER_ALIASES_ALIASNAME_COLUMN + "); ";

    static final String QUERY_CODE_REVIEWS_CREATE_TABLE = // 
                    "CREATE TABLE  " + CODE_REVIEWS_TABLENAME + //
                                    " (" + CODE_REVIEWS_FK_PROJECTID_COLUMN + //
                                    ", " + CODE_REVIEWS_REVIEWID_COLUMN + //
                                    ", " + CODE_REVIEWS_REVIWEDATA_COLUMN + //
                                    ", " + CODE_REVIEWS_STATE_COLUMN + ");";

}
