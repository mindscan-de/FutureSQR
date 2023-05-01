    private static final String GIT_DIFF_A_SLASH_PATH = GitOutputParsingConstants.GIT_DIFF_A_SLASH_PATH;
    private static final String GIT_DIFF_SAPCE_A_SLASH_PATH = GitOutputParsingConstants.GIT_DIFF_SAPCE_A_SLASH_PATH;
    private static final String GIT_DIFF_B_SLASH_PATH = GitOutputParsingConstants.GIT_DIFF_B_SLASH_PATH;
    private static final String GIT_DIFF_SPACE_B_SLASH_PATH = GitOutputParsingConstants.GIT_DIFF_SPACE_B_SLASH_PATH;
    private static final String GIT_DIFF_FILENAMEINFO_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_FILENAMEINFO_IDENTIFIER;
    private static final String GIT_DIFF_NEW_FILE_MODE = GitOutputParsingConstants.GIT_DIFF_NEW_FILE_MODE;
    private static final String GIT_DIFF_RENAME_SIMILARITY_INDEX = GitOutputParsingConstants.GIT_DIFF_RENAME_SIMILARITY_INDEX;

    private static final String GIT_DIFF_LEFT_FILEPATH_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_LEFT_FILEPATH_IDENTIFIER;
    private static final String GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER = GitOutputParsingConstants.GIT_DIFF_RIGHT_FILEPATH_IDENTIFIER;
        int firstIndexASlash = currentGitDiffLine.indexOf( GIT_DIFF_SAPCE_A_SLASH_PATH );
        int firstIndexBSlash = currentGitDiffLine.indexOf( GIT_DIFF_SPACE_B_SLASH_PATH );
        int lastIndexBSlash = currentGitDiffLine.lastIndexOf( GIT_DIFF_SPACE_B_SLASH_PATH );
        currentFileChangeSet.scmFromPath = currentGitDiffLine.substring( firstIndexASlash + GIT_DIFF_SAPCE_A_SLASH_PATH.length(), lastIndexBSlash );
        currentFileChangeSet.scmToPath = currentGitDiffLine.substring( firstIndexBSlash + GIT_DIFF_SPACE_B_SLASH_PATH.length() );
    public static void parseLeftFilePath( String leftFilePathLine, ScmFileChangeSet currentFileChangeSet ) {
        if (leftFilePathLine.startsWith( GIT_DIFF_LEFT_FILEPATH_IDENTIFIER )) {

            String leftFilePath = leftFilePathLine.substring( GIT_DIFF_LEFT_FILEPATH_IDENTIFIER.length() ).trim();

            if (leftFilePath.startsWith( GIT_DIFF_A_SLASH_PATH )) {
                leftFilePath = leftFilePath.substring( GIT_DIFF_A_SLASH_PATH.length() );
            }

            if ((leftFilePath.length() != 0) && !leftFilePath.equals( currentFileChangeSet.scmFromPath )) {
                // System.out.println( "XXX: Differs from scmFromPath: '" + leftFilePath + "' instead of: '" + currentFileChangeSet.scmFromPath + "'" );
                // correcting file path - because parsing the git --diff line may not be robust enough 
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

            if ((rightFilePath.length() != 0) && !rightFilePath.equals( currentFileChangeSet.scmToPath )) {
                // System.out.println( "XXX: Differs from scmToPath: '" + rightFilePath + "' instead of: '" + currentFileChangeSet.scmToPath + "'" );
                // correcting file path - because parsing the git --diff line may not be robust enough
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
        // TODO: parse and consume this info and add info to current file change set.
        currentFileChangeSet.renamed_to = renameToLine;
        System.out.println( "TODO PARSEME: rename_to_line: '" + renameToLine + "'" );
    }

    public static void parseRenameFrom( String renameFromLine, ScmFileChangeSet currentFileChangeSet ) {
        // TODO: parse and consume this info and add info to current file change set.
        currentFileChangeSet.renamed_from = renameFromLine;
        System.out.println( "TODO PARSEME: rename_from_line: '" + renameFromLine + "'" );
    }
