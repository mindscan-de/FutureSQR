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
package de.mindscan.futuresqr.scmaccess.git.processor;

import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutputProcessor;
import de.mindscan.futuresqr.scmaccess.git.processor.impl.ScmFileContentOutputProcessor;
import de.mindscan.futuresqr.scmaccess.git.processor.impl.ScmFileHistoryOutputProcessor;
import de.mindscan.futuresqr.scmaccess.git.processor.impl.ScmFullChangeSetOutputProcessor;
import de.mindscan.futuresqr.scmaccess.git.processor.impl.ScmHistoryOutputProcessor;
import de.mindscan.futuresqr.scmaccess.git.processor.impl.ScmSingleRevisionFileChangeListOutputProcessor;
import de.mindscan.futuresqr.scmaccess.git.processor.impl.ScmVoidOutputProcessor;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContent;
import de.mindscan.futuresqr.scmaccess.types.ScmFileHistory;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmHistory;
import de.mindscan.futuresqr.scmaccess.types.ScmSingleRevisionFileChangeList;

/**
 * 
 */
public class GitOutputProcessors {

    public static GitCLICommandOutputProcessor<ScmFileContent> toScmFileContent() {
        return new ScmFileContentOutputProcessor();
    }

    public static GitCLICommandOutputProcessor<ScmFileHistory> toScmFileHistory() {
        return new ScmFileHistoryOutputProcessor();
    }

    public static GitCLICommandOutputProcessor<ScmHistory> toScmHistory() {
        return new ScmHistoryOutputProcessor();
    }

    public static GitCLICommandOutputProcessor<ScmSingleRevisionFileChangeList> toScmSingleRevisionFileChangeList() {
        return new ScmSingleRevisionFileChangeListOutputProcessor();
    }

    public static GitCLICommandOutputProcessor<ScmFullChangeSet> toScmFullChangeSet() {
        return new ScmFullChangeSetOutputProcessor();
    }

    public static GitCLICommandOutputProcessor<Void> toScmVoid() {
        return new ScmVoidOutputProcessor();
    }
}
