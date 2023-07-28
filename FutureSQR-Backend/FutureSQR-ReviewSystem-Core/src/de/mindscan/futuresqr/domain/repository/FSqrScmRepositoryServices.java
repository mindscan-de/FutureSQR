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
package de.mindscan.futuresqr.domain.repository;

import de.mindscan.futuresqr.domain.model.FSqrScmHistory;
import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;

/**
 * TODO: MOVE ME when the time is right.
 * This part will be moved to the "crawlers" section, because the domain model / domain repository
 * should not deal with this low level scm stuff any more. The domain repository system shouold 
 * instead be able to issue a request to the crawler, which then deals with this request fully 
 * asynchronous. 
 */
public interface FSqrScmRepositoryServices extends FSqrRepository {

    // update local project cache.

    void updateProjectCache( String projectId );

    // clone and checkout project

    void cloneCheckoutToProjectCache( String projectId );

    // -------------------------------------------------------------------------------------------------------
    // collection for scm access only stuff moved from other code, which should solely depend on database code
    // TODO: maybe this code should be extracted to the crawler.
    // -------------------------------------------------------------------------------------------------------

    // get head revision from scm
    FSqrRevisionFullChangeSet getHeadRevisionFullChangeSetFromScm( String projectId );

    // get revisions since given revision.
    FSqrScmHistory getRecentRevisionHistoryStartingFrom( String projectId, String fromRevision );

}
