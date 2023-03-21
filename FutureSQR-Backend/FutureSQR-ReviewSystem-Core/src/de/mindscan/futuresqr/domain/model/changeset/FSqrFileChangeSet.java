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
import de.mindscan.futuresqr.scmaccess.types.ScmFileContentChangeSet;

/**
 * 
 */
public class FSqrFileChangeSet {

    private List<FSqrFileContentChangeSet> fileContentChangeSet = new ArrayList<>();
    private boolean isBinaryFile = false;

    // TODO refactor this.
    private String lazyBinaryFileInfo = "";

    // new
    private String fromPath = "";
    private String toPath = "";

    private String fileAction = "";
    private String fileMode = "";
    private String fileParentRevId = "";
    private String fileCurrentRevId = "";

    // in case of a rename / move, we would also like to store from and to as well as the file similarity.
    private String renamedFrom = "";
    private String renamedTo = "";
    private int renameSimilarity = 100;

    /**
     * 
     */
    public FSqrFileChangeSet() {
        // intentionally left blank
    }

    public FSqrFileChangeSet( ScmFileChangeSet scmFileChangeSet ) {
        this.lazyBinaryFileInfo = scmFileChangeSet.binary_file_info_line;
        this.isBinaryFile = scmFileChangeSet.isBinaryFile;

        this.fromPath = scmFileChangeSet.scmFromPath;
        this.toPath = scmFileChangeSet.scmToPath;

        this.fileMode = scmFileChangeSet.fileMode;
        this.fileCurrentRevId = scmFileChangeSet.fileCurrentRevId;
        this.fileParentRevId = scmFileChangeSet.fileParentRevId;
        this.fileAction = scmFileChangeSet.fileAction;

        this.renamedFrom = scmFileChangeSet.renamed_from;
        this.renamedTo = scmFileChangeSet.renamed_to;
        this.renameSimilarity = scmFileChangeSet.renameSimilarity;

        scmFileChangeSet.fileContentChangeSet.stream().forEach( x -> fileContentChangeSet.add( translate( x ) ) );
    }

    private FSqrFileContentChangeSet translate( ScmFileContentChangeSet x ) {
        return new FSqrFileContentChangeSet( x );
    }

    public FSqrFileChangeSet( FSqrFileChangeSet otherFileChangeSet ) {
        this.lazyBinaryFileInfo = otherFileChangeSet.lazyBinaryFileInfo;
        this.isBinaryFile = otherFileChangeSet.isBinaryFile;

        this.fromPath = otherFileChangeSet.fromPath;
        this.toPath = otherFileChangeSet.toPath;

        this.fileMode = otherFileChangeSet.fileMode;
        this.fileCurrentRevId = otherFileChangeSet.fileCurrentRevId;
        this.fileParentRevId = otherFileChangeSet.fileParentRevId;
        this.fileAction = otherFileChangeSet.fileAction;

        this.renamedFrom = otherFileChangeSet.renamedFrom;
        this.renamedTo = otherFileChangeSet.renamedTo;
        this.renameSimilarity = otherFileChangeSet.renameSimilarity;

        otherFileChangeSet.fileContentChangeSet.stream().forEach( x -> this.fileContentChangeSet.add( copy( x ) ) );
    }

    private FSqrFileContentChangeSet copy( FSqrFileContentChangeSet x ) {
        return new FSqrFileContentChangeSet( x );
    }

    public List<FSqrFileContentChangeSet> getFileContentChangeSet() {
        return fileContentChangeSet;
    }

    public boolean isBinaryFile() {
        return isBinaryFile;
    }

    public String getLazyBinaryFileInfo() {
        return lazyBinaryFileInfo;
    }

    public String getRenamedFrom() {
        return renamedFrom;
    }

    public String getRenamedTo() {
        return renamedTo;
    }

    public String getFromPath() {
        return fromPath;
    }

    public String getToPath() {
        return toPath;
    }

    public String getFileMode() {
        return fileMode;
    }

    public String getFileParentRevId() {
        return fileParentRevId;
    }

    public String getFileCurrentRevId() {
        return fileCurrentRevId;
    }

    public String getFileAction() {
        return fileAction;
    }

    public int getRenameSimilarity() {
        return renameSimilarity;
    }
}
