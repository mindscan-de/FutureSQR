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
package de.mindscan.futuresqr.domain.model.changeset;

import java.util.ArrayList;
import java.util.List;

import de.mindscan.futuresqr.scmaccess.types.ScmFileChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;

/**
 * 
 */
public class FSqrRevisionFullChangeSet {

    private String revisionId = "";
    private String shortRevisionId = "";
    private List<FSqrFileChangeSet> fileChangeSet = new ArrayList<>();

    private String commitMessageDetails;
    private String commitMessageFull;
    private String commitMessageHead;

    /**
     * 
     */
    public FSqrRevisionFullChangeSet() {
        // intentionally left blank
    }

    public FSqrRevisionFullChangeSet( ScmFullChangeSet scmFullChangeSet ) {
        this.revisionId = scmFullChangeSet.revisionId;
        this.shortRevisionId = scmFullChangeSet.shortRevisionId;
        this.commitMessageDetails = scmFullChangeSet.revisionCommitMessageDetails;
        this.commitMessageFull = scmFullChangeSet.revisionCommitMessageFull;
        this.commitMessageHead = scmFullChangeSet.revisionCommitMessageHead;

        scmFullChangeSet.fileChangeSet.stream().forEach( x -> this.fileChangeSet.add( transform( x ) ) );
    }

    private FSqrFileChangeSet transform( ScmFileChangeSet scmFileChangeSet ) {
        return new FSqrFileChangeSet( scmFileChangeSet );
    }

    public List<FSqrFileChangeSet> getFileChangeSet() {
        return fileChangeSet;
    }

    public String getRevisionId() {
        return revisionId;
    }

    public String getShortRevisionId() {
        return shortRevisionId;
    }

    public String getCommitMessageDetails() {
        return commitMessageDetails;
    }

    public String getCommitMessageFull() {
        return commitMessageFull;
    }

    public String getCommitMessageHead() {
        return commitMessageHead;
    }

    public void addFileChangeSet( FSqrFileChangeSet fileChangSet ) {
        this.fileChangeSet.add( fileChangSet );
    }

    public void removeFileChangeSet( FSqrFileChangeSet previous ) {
        this.fileChangeSet.remove( previous );
    }

    public void setRevisionId( String revisionId ) {
        this.revisionId = revisionId;
    }

    public void setShortRevisionId( String shortRevisionId ) {
        this.shortRevisionId = shortRevisionId;
    }
}
