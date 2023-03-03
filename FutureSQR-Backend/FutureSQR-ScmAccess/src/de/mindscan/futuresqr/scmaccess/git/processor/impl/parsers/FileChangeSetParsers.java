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

import de.mindscan.futuresqr.scmaccess.types.ScmFileChangeSet;

/**
 * 
 */
public class FileChangeSetParsers {

    private static final String GIT_DIFF_A_SLASH_PATH = " a/";
    private static final String GIT_DIFF_B_SLASH_PATH = " b/";
    private static final String GIT_DIFF_FILENAMEINFO_IDENTIFIER = "diff --git ";

    public static void parseGitDiffLineToFileChangeSet( String currentGitDiffLine, ScmFileChangeSet currentFileChangeSet ) {

        if (!currentGitDiffLine.startsWith( GIT_DIFF_FILENAMEINFO_IDENTIFIER )) {
            currentFileChangeSet.scmFromPath = "";
            currentFileChangeSet.scmToPath = "";
            return;
        }

        int firstIndexASlash = currentGitDiffLine.indexOf( GIT_DIFF_A_SLASH_PATH );
        // int lastIndexASlash = currentLazyDiffLine.lastIndexOf( " a/" );

        int firstIndexBSlash = currentGitDiffLine.indexOf( GIT_DIFF_B_SLASH_PATH );
        int lastIndexBSlash = currentGitDiffLine.lastIndexOf( GIT_DIFF_B_SLASH_PATH );

        currentFileChangeSet.scmFromPath = currentGitDiffLine.substring( firstIndexASlash + GIT_DIFF_A_SLASH_PATH.length(), lastIndexBSlash );
        currentFileChangeSet.scmToPath = currentGitDiffLine.substring( firstIndexBSlash + GIT_DIFF_B_SLASH_PATH.length() );
    }

    public static void parseIndexLineToFileChangeSet( String currentIndexLine, ScmFileChangeSet currentFileChangeSet ) {
        String[] fileinfo = currentIndexLine.trim().split( " ", 3 );
        String revisions = fileinfo[1];
        String[] revisioninfo = revisions.split( "\\.\\.", 2 );
        currentFileChangeSet.fileParentRevId = revisioninfo[0];
        currentFileChangeSet.fileCurrentRevId = revisioninfo[1];

        if (fileinfo.length > 2) {
            String filemode = fileinfo[2];
            currentFileChangeSet.fileMode = filemode;
        }
    }

}
