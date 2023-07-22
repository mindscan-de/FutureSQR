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

import java.util.List;

import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.FSqrRevisionFileChangeList;
import de.mindscan.futuresqr.domain.model.FSqrScmHistory;
import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;
import de.mindscan.futuresqr.domain.model.content.FSqrFileContentForRevision;
import de.mindscan.futuresqr.domain.model.history.FSqrFileHistory;

/**
 * 
 */
public interface FSqrScmProjectRevisionRepository extends FSqrRepository, FSqrDatabaseBackedRepository {

    // file history

    FSqrFileHistory getParticularFileHistory( String projectId, String revisionId, String filePath );

    // file content for revision

    FSqrFileContentForRevision getFileContentForRevision( String projectId, String revisionId, String filePath );

    // changeset info

    FSqrRevisionFullChangeSet getRevisionFullChangeSet( String projectId, String revisionId );

    FSqrRevisionFullChangeSet getRevisionFullChangeSet( String projectId, List<FSqrRevision> revisionList );

    FSqrRevisionFileChangeList getAllRevisionsFileChangeList( String projectId, List<FSqrRevision> revisions );

    FSqrRevisionFileChangeList getRevisionFileChangeList( String projectId, String revisionId );

    // revision info

    FSqrRevision getSimpleRevisionInformation( String projectId, String revisionId );

    FSqrRevision retrieveHeadRevision( String projectId, String projectBranch );

    // project history 

    FSqrScmHistory getRecentRevisionHistoryStartingFrom( String projectId, String fromRevision );

    FSqrScmHistory getRecentRevisionHistory( String projectId );

    // TODO: get most recent revision known to repository. - we would need this feature for a crawler.
    // TODO: when working with crawlers, we have to consider threading issues and locking issues as well.
    // TODO: maybe we should not use the project revision repositoy for wite to database interactions at 
    //       all.

}
