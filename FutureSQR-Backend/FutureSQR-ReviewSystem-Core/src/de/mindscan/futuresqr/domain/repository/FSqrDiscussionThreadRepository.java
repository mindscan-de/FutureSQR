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

import java.util.Collection;

import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThread;

/**
 * 
 */
public interface FSqrDiscussionThreadRepository extends FSqrRepository, FSqrDatabaseBackedRepository {

    // create

    FSqrDiscussionThread createNewReviewThread( String projectId, String reviewId, String messageText, String messageAuthorUUID );

    // TODO: we want to create a Thread bound to the file, actually a code reference and to the thread.
    // This will associate this discussion thread with a project and a review, as well with a code reference, such that this accessible two ways.
    // this allows us to find a review for a file resource, as well as looking for the thread if we found a thread.
    // a code reference will be branchname, filename, revisionid, filerevisionid, linenumber, code fragment (for matching), maybe the selected codefragment BPEEncoded?),
    // FSqrDiscussionThread createNewReviewThreadOnCode( String projectId, String reviewId, String messageText, String messageAuthorUUID, String branchname,
    //                String filename, String revisionId, String fileReviosionId );
    // Maybe use a code referenceid uuid? a code reference may reference its parent code code reference?
    // 

    // update: todo maybe return the whole updated discussion thread?

    void editMessage( String projectId, String reviewId, String threadUUID, String messageUUID, String newMessageText, String messageAuthorUUID );

    // replyto: todo maybe return the whole updated discussion thread?

    void replyToThread( String projectId, String reviewId, String threadUUID, String replytoMessageId, String messageText, String messageAuthorUUID );

    // All threads for review 

    Collection<FSqrDiscussionThread> getDirectThreadsForReview( String projectId, String reviewId );

    // TODO: Maybe...., when we added a message to a thread

    // TODO: select a single thread... e.g. in case of a code review
    // FSqrDiscussionThread getDiscussionThread( String threadId );

    // TODO: select all (unclosed) threads for a list of files in a branch,
    // TODO: also handle the renames?
    // Collection<FSqrDiscussionThread> getCodeReklatedDiscussionThreads( String projectId, String branch, Collection<String> filenames );

}
