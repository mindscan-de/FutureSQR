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
 * 
 */
public class ScmFileChangeSet {
    public String scmFromPath = "";
    public String scmToPath = "";

    public ScmFileAction fileAction = ScmFileAction.FILEACTION_UNKNOWN;

    public String fileMode = "";
    public String fileCurrentRevId;
    public String fileParentRevId;

    public String renamed_from = "";
    public String renamed_to = "";
    public int renameSimilarity;

    public boolean isBinaryFile = false;

    // -----------------------------
    // TODO also process this better
    // -----------------------------
    public String binary_file_info_line = "";

    // TODO: maybe we want to classify the charset of the originating file.
    // public String detectedCharset

    // ---------------------
    // TODO parsed FILE INFO
    // ---------------------

    public ArrayList<ScmFileContentChangeSet> fileContentChangeSet = new ArrayList<>();

}
