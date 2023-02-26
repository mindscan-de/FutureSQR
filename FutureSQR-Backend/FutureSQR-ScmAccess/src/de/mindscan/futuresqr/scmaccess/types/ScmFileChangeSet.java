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
    // -------------------------------------
    // TODO remove temporary lazy file infos
    // -------------------------------------
    public String lazy_diff_line = "";

    // TODO: scmFromPath
    // TODO: scmToPath
    public String scmFromPath = "";
    public String scmToPath = "";

    // ------------------------------------------------------------
    // TODO remove temporary lazy index infos / file mode and more.
    // ------------------------------------------------------------
    public String lazy_index_line = "";

    // TODO also process this better
    public String similarity_info_line = "";
    public String renamed_from = "";
    public String renamed_to = "";

    public boolean isBinaryFile = false;
    public String binary_file_info_line = "";

    // ---------------------
    // TODO parsed FILE INFO
    // ---------------------

    public ArrayList<ScmFileContentChangeSet> fileContentChangeSet = new ArrayList<>();

}
