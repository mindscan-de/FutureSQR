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

import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.tasks.FSqrBackgroundTaskBase;

/**
 * We actually have two scan and index strategies for scm revisions. 
 * 
 * When we commit and update the revisions we start at the newest known revision 
 * in the database for a certain project with a certain revision. We need this to 
 * provide maximum usability such that a recovery from a database loss 
 * 
 * or first startup and also 
 * recoverability in case the update is interrupted somehow.
 */
public class ScanIndexScmRevisionsBackwardsTask extends FSqrBackgroundTaskBase {

    private String projectIdentifier;
    private String projectBranch;
    private String startRevision;

    /**
     * 
     */
    public ScanIndexScmRevisionsBackwardsTask( String projectIdentifier, String projectBranch, String startRevision ) {
        this.projectIdentifier = projectIdentifier;
        this.projectBranch = projectBranch;
        this.startRevision = startRevision;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        FSqrApplicationServices services = getTaskContext().getServices();

        services.getScmRepositoryServices().getRecentRevisionHistoryStartingFrom( projectIdentifier, startRevision );
    }

}
