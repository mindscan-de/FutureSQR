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
package de.mindscan.futuresqr.scmaccess.types;

import java.util.ArrayList;

/**
 * TODO: add basic revision info here, which contains author, date, message etc
 *       instead of duplicating this information.
 */
public class ScmFullChangeSet {

    // TODO: actually this can be used as a query key to look up the basic revision info from the right table.
    public String revisionId;
    public String shortRevisionId;

    // TODO: authorName / AuthorId / AuthorEmail

    // TODO: author email

    public String date;
    // TODO: shortdate and reldate, which are not part of this view.... from git...

    // TODO: branch name?

    // commitMessage
    public String revisionCommitMessageFull;
    public String revisionCommitMessageHead;
    public String revisionCommitMessageDetails;

    public ArrayList<ScmFileChangeSet> fileChangeSet = new ArrayList<>();

}
