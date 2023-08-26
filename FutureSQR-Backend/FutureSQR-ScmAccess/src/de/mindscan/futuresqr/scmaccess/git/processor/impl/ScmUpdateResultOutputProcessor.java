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
package de.mindscan.futuresqr.scmaccess.git.processor.impl;

import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutput;
import de.mindscan.futuresqr.scmaccess.git.GitCLICommandOutputProcessor;
import de.mindscan.futuresqr.scmaccess.types.ScmUpdateResult;

/**
 * 
 */
public class ScmUpdateResultOutputProcessor implements GitCLICommandOutputProcessor<ScmUpdateResult> {

    /** 
     * {@inheritDoc}
     */
    @Override
    public ScmUpdateResult transform( GitCLICommandOutput output ) {
        String commandOutput = new String( output.getProcessOutput() );

        System.out.println( commandOutput );

        /**
         * 
        parse....  updating
        
        Updating a8e24aef..539d7b9e
        Fast-forward
        .../tasks/DetectNewScmProjectBranchesTask.java     |  2 +-
        .../tasks/ScanIndexScmRevisionsBackwardsTask.java  |  2 +-
        .../tasks/ScanIndexScmRevisionsForwardTask.java    |  2 +-
        .../crawlers/tasks/UpdateProjectCacheTask.java     | 48 +++++++++++++++++++---
        .../repository/FSqrScmRepositoryServices.java      |  6 ++-
        .../futuresqr/tasks/FSqrBackgroundTask.java        |  6 ++-
        .../futuresqr/tasks/FSqrBackgroundTaskBase.java    | 33 ++++++++++++---
        7 files changed, 82 insertions(+), 17 deletions(-)
        
        
        parse.... up-to-date
        
        
         */

        // TODO Auto-generated method stub

        // updated - from - to
        // update result updated / uptodate / failed
        return null;
    }

}
