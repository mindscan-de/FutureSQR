    private static final String GIT_DIFF_RENAME_SIMILARITY_INDEX = "similarity index ";
    public static void parseRenameSimilarityToFileChangeSet( String similarityLine, ScmFileChangeSet currentFileChangeSet ) {
        String similarity = similarityLine.substring( GIT_DIFF_RENAME_SIMILARITY_INDEX.length(), similarityLine.length() - "%".length() );

        try {
            currentFileChangeSet.renameSimilarity = Integer.parseInt( similarity );
        }
        catch (Exception ex) {
            currentFileChangeSet.renameSimilarity = 100;
        }
    }
