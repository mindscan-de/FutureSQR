import de.mindscan.futuresqr.scmaccess.types.ScmDiffLine;
import de.mindscan.futuresqr.scmaccess.types.ScmDiffLineType;
    private static final String GIT_DIFF_FOUR_SAPCE_INDENT = "    ";
    private static final String GIT_DIFF_EMPTY_LINE = "";
        // read and parse Author
        if (lineLexer.peekCurrentLine().startsWith( GIT_DIFF_NEWCOMMIT_AUTHOR_LINE_IDENTIFIER )) {
            String commitAuthorLine = lineLexer.consumeCurrentLine();
            FullChangeSetParsers.parseCommitAuthorToFullChangeSet( commitAuthorLine, scmFullChangeSet );
        }

        // read and parse DATE
        if ((lineLexer.peekCurrentLine().startsWith( GIT_DIFF_NEWCOMMIT_DATE_LINE_IDENTIFIER ))) {
            String commitDateLine = lineLexer.consumeCurrentLine();
            FullChangeSetParsers.parseCommitDateToFullChangeSet( commitDateLine, scmFullChangeSet );
        }

        // read commit message.
        if (GIT_DIFF_EMPTY_LINE.equals( lineLexer.peekCurrentLine() )) {
            List<String> commitLines = new ArrayList<>();

            // consume empty line
            lineLexer.consumeCurrentLine();

            while (!GIT_DIFF_EMPTY_LINE.equals( lineLexer.peekCurrentLine() )) {
                String commentline = lineLexer.consumeCurrentLine();

                if (commentline.startsWith( GIT_DIFF_FOUR_SAPCE_INDENT )) {
                    // remove 4 space indent
                    commitLines.add( commentline.substring( GIT_DIFF_FOUR_SAPCE_INDENT.length() ) );
                }
            }

            // consume empty line
            lineLexer.consumeCurrentLine();

            String commitMessage = String.join( "\n", commitLines );
            FullChangeSetParsers.parseCommitMessageToFullChangeSet( commitMessage, scmFullChangeSet );
        }
            else if (GIT_DIFF_EMPTY_LINE.equals( currentLine )) {
                contentChangeset.unifiedDiffLines.add( toScmDiffLine( currentLine ) );
    private ScmDiffLine toScmDiffLine( String currentLine ) {
        String lineContent = currentLine.length() > 0 ? currentLine.substring( 1 ) : currentLine;

        return new ScmDiffLine( ScmDiffLineType.toType( currentLine ), lineContent );
    }
