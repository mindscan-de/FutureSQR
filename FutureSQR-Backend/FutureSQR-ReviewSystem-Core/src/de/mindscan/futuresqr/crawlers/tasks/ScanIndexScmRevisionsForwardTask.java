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
package de.mindscan.futuresqr.crawlers.tasks;

import de.mindscan.futuresqr.tasks.FSqrBackgroundTask;

/**
 * We actually have two scan and index strategies for scm revisions. 
 * 
 * When we commit and update the revisions we start at the oldest revision known
 * to the database for a certain project with a certain revision.  
 */
public class ScanIndexScmRevisionsForwardTask implements FSqrBackgroundTask {

    private String projectIdentifier;
    private String projectBranch;
    private String startRevision;

    /**
     * 
     */
    public ScanIndexScmRevisionsForwardTask( String projectIdentifier, String projectBranch, String startRevision ) {
        this.projectIdentifier = projectIdentifier;
        this.projectBranch = projectBranch;
        this.startRevision = startRevision;
    }

    // TODO: set execution context
    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        // TODO: execution - callback to report success / failure / etc.... / also service to add other tasks, or events.

        // Scm repo get since revision...
    }

}
