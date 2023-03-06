import de.mindscan.futuresqr.scmaccess.git.processor.impl.parsers.FullChangeSetParsers;
    private static final String GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER = "commit ";
    private static final String GIT_DIFF_NEWCOMMIT_AUTHOR_LINE_IDENTIFIER = "Author: ";
    private static final String GIT_DIFF_NEWCOMMIT_DATE_LINE_IDENTIFIER = "Date: ";

        ScmFullChangeSet result = parseFullChangeSet( new String( output.getProcessOutput(), StandardCharsets.UTF_8 ) );
        return result;
    private ScmFullChangeSet parseFullChangeSet( String string ) {

        ScmFullChangeSet scmFullChangeSet = new ScmFullChangeSet();
        Consumer<ScmFileChangeSet> fileChangeSetConsumer = scmFullChangeSet.fileChangeSet::add;

        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER )) {
            String commitRevisionIdLine = lineLexer.consumeCurrentLine();
            FullChangeSetParsers.parseCommitRevisionLineToFullChangeSet( commitRevisionIdLine, scmFullChangeSet );
        }

        // TODO: Author
        // TODO: DATE
        // TODO: NEWLINE / empty line
        // TODO: commitmessage (starts with 4 spaces "    ")
        // TODO: NEWLINE / empty line

                fileChangeSetConsumer.accept( parseFileChangeSetEntry( lineLexer ) );
        return scmFullChangeSet;
    private ScmFileChangeSet parseFileChangeSetEntry( GitScmLineBasedLexer lineLexer ) {
            String deleteFileModeLine = lineLexer.consumeCurrentLine();
            String similarityLine = lineLexer.consumeCurrentLine();
            FileChangeSetParsers.parseRenameSimilarityToFileChangeSet( similarityLine, currentFileChangeSet );
            return currentFileChangeSet;
            return currentFileChangeSet;
        return currentFileChangeSet;