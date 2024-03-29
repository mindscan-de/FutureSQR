/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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
package de.mindscan.futuresqr.scmaccess.git.processor.impl;

import java.nio.charset.StandardCharsets;

import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutput;
import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutputProcessor;
import de.mindscan.futuresqr.scmaccess.git.GitCommand;
import de.mindscan.futuresqr.scmaccess.git.command.GitCommandWithFilePath;
import de.mindscan.futuresqr.scmaccess.git.command.GitCommandWithRevisionId;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContent;

/**
 * 
 */
public class ScmFileContentOutputProcessor implements GitCLICommandOutputProcessor<ScmFileContent> {

    /**
     * 
     */
    public ScmFileContentOutputProcessor() {
        // intentionally left blank.
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmFileContent transform( GitCLICommandOutput output ) {
        ScmFileContent scmFileContent = new ScmFileContent();

        scmFileContent.scmRepository = output.getRepository();
        // FIXME?
        // actually the charset should be determined, like cp-1252 and such, so it can be properly converted to UTF-8
        // using some useful tables, BUt in case of downloads, we actually don't want to alter the truth. but the UTF-8
        // decoder may change the truth, actually we should probably use bytes, instead of a string.
        scmFileContent.fileContent = new String( output.getProcessOutput(), StandardCharsets.UTF_8 );

        GitCommand gitCommand = output.getCommand();

        if (gitCommand instanceof GitCommandWithFilePath) {
            scmFileContent.filePath = ((GitCommandWithFilePath) gitCommand).getFilePath();
        }

        if (gitCommand instanceof GitCommandWithRevisionId) {
            scmFileContent.fileRevisionId = ((GitCommandWithRevisionId) gitCommand).getRevisionId();
        }

        return scmFileContent;
    }

}
