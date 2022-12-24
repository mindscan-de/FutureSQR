/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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
package de.mindscan.futuresqr.scmaccess.git;

/*
 * Choice for the special chars for the formatting of the git command.
 *  
 * %x1c - File separator
 * %x1d - Group Separator
 * %x1e - Record Separator
 * %x1f - Unit Separator
 */

/**
 * 
 */
public class GitCLICommonConstants {

    /** 
     * git "show" command
     */
    public static final String GIT_COMMAND_SHOW = "show";

    /** 
     * git "log" command
     */
    public static final String GIT_COMMAND_LOG = "log";

    /**
     * This list contains the meanings of the List in GitCLICommonConstants.GIT_FORMAT_PARAMS.
     */
    public static final String[] GIT_FORMAT_FIELDS = { "shortrev", "revisionid", "authorname", "authorid", "date", "shortdate", "reldate", "parents",
                    "parentsshort", "message" };
    /**
     * This list contains the identifiers for the git --pretty=format:xyz arguments. This Array is liked to GitCLICommonConstants.GIT_FORMAT_FIELDS}
     * This list is used for multiple instances of the --pretty formatter syntax 
     */
    public static final String[] GIT_FORMAT_PARAMS = { "%h", "%H", "%an", "%ae", "%ad", "%as", "%ar", "%P", "%p", "%s" };

    /**
     * Constant to avoid paging in git command line tool
     */
    public static final String NO_PAGER = "--no-pager";

    /**
     * Follow if renames of files should be included in the file history.
     */
    public static final String FOLLOW = "--follow";

    /**
     * Makes GIT to change the mode such that the following argument is interpreted as a file name.
     */
    public static final String MINUSMINUS = "--";

    /**
     * 
     */
    public static final String GIT_PRETTY_FORMAT_WITH_PARAMETERS = "--pretty=format:" + String.join( "%x1f", GIT_FORMAT_PARAMS ) + "%x1e";

}
