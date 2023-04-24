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

import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;

/**
 * 
 */
public class FullChangeSetParsers {

    private static final String GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER = "commit ";
    private static final String GIT_DIFF_NEWCOMMIT_AUTHOR_LINE_IDENTIFIER = "Author: ";
    private static final String GIT_DIFF_NEWCOMMIT_DATE_LINE_IDENTIFIER = "Date: ";
    private static final int GIT_SHORT_REVISION_LENGTH = 7;

    public static void parseCommitRevisionLineToFullChangeSet( String commitRevisionIdLine, ScmFullChangeSet scmFullChangeSet ) {
        if (commitRevisionIdLine.startsWith( GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER )) {
            scmFullChangeSet.revisionId = commitRevisionIdLine.substring( GIT_DIFF_NEWCOMMIT_COMMIT_IDENTIFIER.length() );
            scmFullChangeSet.shortRevisionId = scmFullChangeSet.revisionId.substring( 0, GIT_SHORT_REVISION_LENGTH );
        }
    }

    public static void parseCommitAuthorToFullChangeSet( String commitAuthorLine, ScmFullChangeSet scmFullChangeSet ) {
        System.out.println( "Authorline: '" + commitAuthorLine + "'" );
        if (commitAuthorLine.startsWith( GIT_DIFF_NEWCOMMIT_AUTHOR_LINE_IDENTIFIER )) {

        }
    }

    public static void parseCommitDateToFullChangeSet( String commitDateLine, ScmFullChangeSet scmFullChangeSet ) {
        System.out.println( "Dateline: '" + commitDateLine + "'" );
        if (commitDateLine.startsWith( GIT_DIFF_NEWCOMMIT_DATE_LINE_IDENTIFIER )) {

        }
    }

    public static void parseCommitMessageToFullChangeSet( String commitMessage, ScmFullChangeSet scmFullChangeSet ) {
        scmFullChangeSet.revisionCommitMessageFull = commitMessage;

        String[] splitCommitMessage = commitMessage.split( "\\R", 2 );

        if (splitCommitMessage.length > 0) {
            scmFullChangeSet.revisionCommitMessageHead = splitCommitMessage[0];
        }

        if (splitCommitMessage.length > 1) {
            scmFullChangeSet.revisionCommitMessageDetails = splitCommitMessage[1];
        }
    }

}
