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

import java.util.Collections;
import java.util.List;

import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.FSqrScmHistory;
import de.mindscan.futuresqr.domain.repository.FSqrScmRepositoryServices;
import de.mindscan.futuresqr.tasks.FSqrBackgroundTaskBase;

/**
 * We actually have two scan and index strategies for scm revisions. 
 * 
 * When we commit and update the revisions we start at the oldest revision known
 * to the database for a certain project with a certain revision.  
 */
public class ScanIndexScmRevisionsForwardTask extends FSqrBackgroundTaskBase {

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

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        // TODO: set execution context / also we want a kind of repository implementation related to crawler only code

        // use a different crawler services repository... 
        FSqrScmRepositoryServices scmRepositoryServices = getTaskContext().getServices().getScmRepositoryServices();

        FSqrScmHistory newRevisions = scmRepositoryServices.getRecentRevisionHistoryStartingFrom( projectIdentifier, startRevision );

        List<FSqrRevision> revisionsCopy = newRevisions.getRevisionsCopy();

        // TODO do some automatization here - 
        // automatic also time is not perfect because different clocks may different in real world.
        // reverse the order such that from oldest to newest - and only index non indexed versions, 
        // idea is if this is interrupted somehow, the index can continue later.
        // should be done automatically sort by commitdate or by parent version...
        Collections.reverse( revisionsCopy );

        for (FSqrRevision fSqrRevision : revisionsCopy) {
            // TODO index this revision in database if this revision is not present in the database
            // doublecheck here or in the database call.

            // TODO: queue in an analytics task for each new revision? or just emit an event, such that a task can
            //       subscribe to this event and then can the analytics decide whether to do somethign (this sounds 
            //       more useful to me instead of just triggering the task.
            // TODO: sounds we need an event dispatcher and an event subscription model.
            // TODO: that also means we need access to the event dispatching mechanism.
        }
    }

}
