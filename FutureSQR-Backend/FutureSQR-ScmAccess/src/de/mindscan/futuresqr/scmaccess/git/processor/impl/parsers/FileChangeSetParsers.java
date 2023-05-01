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

    private static final String GIT_DIFF_A_SLASH_PATH = GitOutputParsingConstants.GIT_DIFF_A_SLASH_PATH;
    private static final String GIT_DIFF_SAPCE_A_SLASH_PATH = GitOutputParsingConstants.GIT_DIFF_SAPCE_A_SLASH_PATH;
    private static final String GIT_DIFF_B_SLASH_PATH = GitOutputParsingConstants.GIT_DIFF_B_SLASH_PATH;
    private static final String GIT_DIFF_SPACE_B_SLASH_PATH = GitOutputParsingConstants.GIT_DIFF_SPACE_B_SLASH_PATH;
    private static final String GIT_DIFF_FILENAMEINFO_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_FILENAMEINFO_IDENTIFIER;
    private static final String GIT_DIFF_NEW_FILE_MODE = GitOutputParsingConstants.GIT_DIFF_NEW_FILE_MODE;
    private static final String GIT_DIFF_RENAME_SIMILARITY_INDEX = GitOutputParsingConstants.GIT_DIFF_RENAME_SIMILARITY_INDEX;

    private static final String GIT_DIFF_LEFT_FILEPATH_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_LEFT_FILEPATH_IDENTIFIER;
    private static final String GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER;

    private static final String GIT_DIFF_RENAME_TO = GitOutputParsingConstants.GIT_DIFF_RENAME_TO;
    private static final String GIT_DIFF_RENAME_FROM = GitOutputParsingConstants.GIT_DIFF_RENAME_FROM;

    private static final String GIT_DEV_NULL = "/dev/null";

    public static void parseGitDiffLineToFileChangeSet( String currentGitDiffLine, ScmFileChangeSet currentFileChangeSet ) {

        if (!currentGitDiffLine.startsWith( GIT_DIFF_FILENAMEINFO_IDENTIFIER )) {
            currentFileChangeSet.scmFromPath = "";
            currentFileChangeSet.scmToPath = "";
            return;
        }

        int firstIndexASlash = currentGitDiffLine.indexOf( GIT_DIFF_SAPCE_A_SLASH_PATH );
        // int lastIndexASlash = currentLazyDiffLine.lastIndexOf( " a/" );

        int firstIndexBSlash = currentGitDiffLine.indexOf( GIT_DIFF_SPACE_B_SLASH_PATH );
        int lastIndexBSlash = currentGitDiffLine.lastIndexOf( GIT_DIFF_SPACE_B_SLASH_PATH );

        currentFileChangeSet.scmFromPath = currentGitDiffLine.substring( firstIndexASlash + GIT_DIFF_SAPCE_A_SLASH_PATH.length(), lastIndexBSlash );
        currentFileChangeSet.scmToPath = currentGitDiffLine.substring( firstIndexBSlash + GIT_DIFF_SPACE_B_SLASH_PATH.length() );
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

    public static void parseNewFileModeLineToFileChangeSet( String newFileModeLine, ScmFileChangeSet currentFileChangeSet ) {
        String fileMode = newFileModeLine.substring( GIT_DIFF_NEW_FILE_MODE.length() );
        currentFileChangeSet.fileMode = fileMode.trim();
    }

    public static void parseRenameSimilarityToFileChangeSet( String similarityLine, ScmFileChangeSet currentFileChangeSet ) {
        String similarity = similarityLine.substring( GIT_DIFF_RENAME_SIMILARITY_INDEX.length(), similarityLine.length() - "%".length() );

        try {
            currentFileChangeSet.renameSimilarity = Integer.parseInt( similarity );
        }
        catch (Exception ex) {
            currentFileChangeSet.renameSimilarity = 100;
        }
    }

    public static void parseLeftFilePath( String leftFilePathLine, ScmFileChangeSet currentFileChangeSet ) {
        if (leftFilePathLine.startsWith( GIT_DIFF_LEFT_FILEPATH_IDENTIFIER )) {

            String leftFilePath = leftFilePathLine.substring( GIT_DIFF_LEFT_FILEPATH_IDENTIFIER.length() ).trim();

            if (leftFilePath.startsWith( GIT_DIFF_A_SLASH_PATH )) {
                leftFilePath = leftFilePath.substring( GIT_DIFF_A_SLASH_PATH.length() );
            }

            if ((leftFilePath.length() != 0) && !GIT_DEV_NULL.equals( leftFilePath ) && !leftFilePath.equals( currentFileChangeSet.scmFromPath )) {
                // System.out.println( "XXX: Differs from scmFromPath: '" + leftFilePath + "' instead of: '" + currentFileChangeSet.scmFromPath + "'" );
                // correcting file path - because parsing the diff --git line may not be robust enough 
                currentFileChangeSet.scmFromPath = leftFilePath;
            }
        }
    }

    public static void parseRightFilePath( String rightFilePathLine, ScmFileChangeSet currentFileChangeSet ) {
        if (rightFilePathLine.startsWith( GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER )) {

            String rightFilePath = rightFilePathLine.substring( GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER.length() ).trim();

            if (rightFilePath.startsWith( GIT_DIFF_B_SLASH_PATH )) {
                rightFilePath = rightFilePath.substring( GIT_DIFF_B_SLASH_PATH.length() );
            }

            if ((rightFilePath.length() != 0) && !GIT_DEV_NULL.equals( rightFilePath ) && !rightFilePath.equals( currentFileChangeSet.scmToPath )) {
                // System.out.println( "XXX: Differs from scmToPath: '" + rightFilePath + "' instead of: '" + currentFileChangeSet.scmToPath + "'" );
                // correcting file path - because parsing the diff --git line may not be robust enough
                currentFileChangeSet.scmToPath = rightFilePath;
            }
        }
    }

    public static void parseBinaryFileInfoLine( String binaryFileInfoLine, ScmFileChangeSet currentFileChangeSet ) {
        // TODO: parse and consume this info and add info to current file change set.        
        currentFileChangeSet.isBinaryFile = true;
        currentFileChangeSet.binary_file_info_line = binaryFileInfoLine;
        System.out.println( "XXX: binaryFileInfoLine: " + binaryFileInfoLine );
    }

    public static void parseRenameTo( String renameToLine, ScmFileChangeSet currentFileChangeSet ) {
        if (renameToLine.startsWith( GIT_DIFF_RENAME_TO )) {
            String renameToFilename = renameToLine.substring( GIT_DIFF_RENAME_TO.length() ).trim();
            currentFileChangeSet.renamed_to = renameToFilename;
        }
    }

    public static void parseRenameFrom( String renameFromLine, ScmFileChangeSet currentFileChangeSet ) {
        if (renameFromLine.startsWith( GIT_DIFF_RENAME_FROM )) {
            String renameFromFilename = renameFromLine.substring( GIT_DIFF_RENAME_FROM.length() ).trim();
            currentFileChangeSet.renamed_from = renameFromFilename;
        }
    }

}
