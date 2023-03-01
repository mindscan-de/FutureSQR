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

    // TODO refactor this to fileMode, fileParentRevId, fileCurrentRevId
    private String lazyIndexLine = "";

    private String fileMode = "";
    private String fileParentRevId = "";
    private String fileCurrentRevId = "";

    // in case of a rename / move, we would also like to store from and to as well as the file similarity.
    private String renamedFrom = "";
    private String renamedTo = "";

    private String lazySimilarityInfo = "";

    /**
     * 
     */
    public FSqrFileChangeSet() {
        // intentionally left blank
    }

    /**
     * @param scmFileChangeSet
     */
    public FSqrFileChangeSet( ScmFileChangeSet scmFileChangeSet ) {

        this.lazyBinaryFileInfo = scmFileChangeSet.binary_file_info_line;
        this.isBinaryFile = scmFileChangeSet.isBinaryFile;

        this.fromPath = scmFileChangeSet.scmFromPath;
        this.toPath = scmFileChangeSet.scmToPath;
        // TODO: replace it...
        this.lazyIndexLine = scmFileChangeSet.lazy_index_line;
        // TODO: get this from scmFileChangeSet.
        this.fileMode = "";
        this.fileCurrentRevId = "";
        this.fileParentRevId = "";

        this.renamedFrom = scmFileChangeSet.renamed_from;
        this.renamedTo = scmFileChangeSet.renamed_to;
        this.lazySimilarityInfo = scmFileChangeSet.similarity_info_line;

        scmFileChangeSet.fileContentChangeSet.stream().forEach( x -> fileContentChangeSet.add( translate( x ) ) );
    }

    private FSqrFileContentChangeSet translate( ScmFileContentChangeSet x ) {
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

    public String getLazyIndexLine() {
        return lazyIndexLine;
    }

    public String getRenamedFrom() {
        return renamedFrom;
    }

    public String getRenamedTo() {
        return renamedTo;
    }

    public String getLazySimilarityInfo() {
        return lazySimilarityInfo;
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
}
