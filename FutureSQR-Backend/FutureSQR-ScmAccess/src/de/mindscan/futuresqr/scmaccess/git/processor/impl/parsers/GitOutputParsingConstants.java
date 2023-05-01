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
package de.mindscan.futuresqr.scmaccess.git.processor.impl.parsers;

/**
 * 
 */
public class GitOutputParsingConstants {

    public static final String GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER = "commit ";
    public static final String GIT_DIFF_NEWCOMMIT_AUTHOR_LINE_IDENTIFIER = "Author: ";
    public static final String GIT_DIFF_NEWCOMMIT_DATE_LINE_IDENTIFIER = "Date: ";
    public static final String GIT_DIFF_NEWCOMMIT_FOUR_SAPCE_INDENT = "    ";

    public static final String GIT_DIFF_A_SLASH_PATH = "a/";
    public static final String GIT_DIFF_SAPCE_A_SLASH_PATH = " a/";
    public static final String GIT_DIFF_B_SLASH_PATH = "b/";
    public static final String GIT_DIFF_SPACE_B_SLASH_PATH = " b/";

    public static final String GIT_DIFF_FILENAMEINFO_IDENTIFIER = "diff --git ";

    public static final String GIT_DIFF_NEW_FILE_MODE = "new file mode ";
    public static final String GIT_DIFF_DELETED_FILE_MODE = "deleted file mode";

    public static final String GIT_DIFF_RENAME_SIMILARITY_INDEX = "similarity index ";
    public static final String GIT_DIFF_RENAME_FROM = "rename from";
    public static final String GIT_DIFF_RENAME_TO = "rename to";

    public static final String GIT_DIFF_LEFT_FILEPATH_IDENTIFIER = "---";
    public static final String GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER = "+++";

    public static final String GIT_DIFF_BINARY_FILES_IDENTIFIER = "Binary files";
    public static final String GIT_DIFF_NO_NEWLINE_AT_END_OF_FILE_INDICATOR = "\\ No newline at end of file";

}
