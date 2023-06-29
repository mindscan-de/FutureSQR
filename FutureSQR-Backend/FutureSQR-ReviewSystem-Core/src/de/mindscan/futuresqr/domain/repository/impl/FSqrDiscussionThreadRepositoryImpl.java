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
package de.mindscan.futuresqr.domain.repository.impl;

import java.util.Collection;

import de.mindscan.futuresqr.core.uuid.UuidUtil;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.databases.FSqrDiscussionThreadDatabaseTable;
import de.mindscan.futuresqr.domain.databases.FSqrDiscussionThreadIdsTable;
import de.mindscan.futuresqr.domain.databases.impl.FSqrDiscussionThreadDatabaseTableImpl;
import de.mindscan.futuresqr.domain.databases.impl.FSqrDiscussionThreadIdsTableImpl;
import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThread;
import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThreadMessage;
import de.mindscan.futuresqr.domain.repository.FSqrDiscussionThreadRepository;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheDiscussionThreadIdsTableImpl;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheDiscussionThreadTableImpl;

/**
 * This repository class contains access functions to discussion threads attached to
 * code reviwes. 
 */
public class FSqrDiscussionThreadRepositoryImpl implements FSqrDiscussionThreadRepository {

    private FSqrApplicationServices applicationServices;

    // search key: ( threaduuid:string ) -> thread:FSqrDiscussionThread
    private InMemoryCacheDiscussionThreadTableImpl uuidToThreadsCache;
    private FSqrDiscussionThreadDatabaseTable discussionThreadTable;

    // search key: ( projectId:string , reviewId:string ) -> List of DiscussionThread Ids:ArrayList<String>
    private InMemoryCacheDiscussionThreadIdsTableImpl projectAndReviewToThreadsCache;
    private FSqrDiscussionThreadIdsTable projectAndReviewsToThreadsTable;

    /**
     * 
     */
    public FSqrDiscussionThreadRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();

        // lists of threads for a project together and review id
        this.projectAndReviewToThreadsCache = new InMemoryCacheDiscussionThreadIdsTableImpl();
        this.projectAndReviewsToThreadsTable = new FSqrDiscussionThreadIdsTableImpl();

        // uuid to Threads table
        this.uuidToThreadsCache = new InMemoryCacheDiscussionThreadTableImpl();
        this.discussionThreadTable = new FSqrDiscussionThreadDatabaseTableImpl();
    }

    @Override
    public void setApplicationServices( FSqrApplicationServices applicationServices ) {
        this.applicationServices = applicationServices;

        this.discussionThreadTable.setDatbaseConnection( this.applicationServices.getDatabaseConnection() );
        this.projectAndReviewsToThreadsTable.setDatbaseConnection( this.applicationServices.getDatabaseConnection() );
    }

    @Override
    public FSqrDiscussionThread createNewReviewThread( String projectId, String reviewId, String messageText, String messageAuthorUUID ) {
        FSqrDiscussionThread newThread = createNewThread( messageText, messageAuthorUUID );

        // insertThreadToDB
        this.uuidToThreadsCache.putDiscussionThread( newThread.getDiscussionThreadUUID(), newThread );
        this.discussionThreadTable.insertDiscussionThread( newThread );

        this.projectAndReviewToThreadsCache.addDiscussionThread( projectId, reviewId, newThread.getDiscussionThreadUUID() );
        this.projectAndReviewsToThreadsTable.addDiscussionThread( projectId, reviewId, newThread.getDiscussionThreadUUID() );

        return newThread;
    }

    private FSqrDiscussionThread createNewThread( String messageText, String messageAuthorUUID ) {
        String newThreadUUID = UuidUtil.getRandomUUID().toString();

        FSqrDiscussionThreadMessage rootMessage = createRootMessage( newThreadUUID, messageText, messageAuthorUUID );
        FSqrDiscussionThread createdThread = new FSqrDiscussionThread( newThreadUUID, rootMessage, messageAuthorUUID );

        return createdThread;
    }

    private FSqrDiscussionThreadMessage createRootMessage( String newThreadUUID, String messageText, String messageAuthorUUID ) {
        FSqrDiscussionThreadMessage rootMessage = new FSqrDiscussionThreadMessage( UuidUtil.getRandomUUID().toString(), messageText, messageAuthorUUID );

        rootMessage.setThreadUUID( newThreadUUID );
        return rootMessage;
    }

    @Override
    public Collection<FSqrDiscussionThread> getDirectThreadsForReview( String projectId, String reviewId ) {
        Collection<String> discussionThreadUUIDs = projectAndReviewToThreadsCache.getDiscussionThreadUUIDs( projectId, reviewId,
                        projectAndReviewsToThreadsTable::selectDiscussionThreads );

        return this.uuidToThreadsCache.lookupThreads( discussionThreadUUIDs, discussionThreadTable::selectDiscussionThread );
    }

    @Override
    public void editMessage( String projectId, String reviewId, String threadUUID, String messageUUID, String newMessageText, String messageAuthorUUID ) {
        if (!this.projectAndReviewToThreadsCache.hasDiscussionThreadUUID( projectId, reviewId, threadUUID,
                        projectAndReviewsToThreadsTable::selectDiscussionThreads )) {
            return;
        }

        FSqrDiscussionThread thread = uuidToThreadsCache.getDiscussionThread( threadUUID, discussionThreadTable::selectDiscussionThread );
        if (thread != null) {
            // update only if threadUUID exists 
            thread.updateMessage( messageUUID, newMessageText, messageAuthorUUID );
            this.discussionThreadTable.updateThread( thread );
        }
    }

    @Override
    public void replyToThread( String projectId, String reviewId, String threadUUID, String replytoMessageId, String messageText, String messageAuthorUUID ) {
        if (!this.projectAndReviewToThreadsCache.hasDiscussionThreadUUID( projectId, reviewId, threadUUID,
                        projectAndReviewsToThreadsTable::selectDiscussionThreads )) {
            return;
        }

        FSqrDiscussionThread thread = uuidToThreadsCache.getDiscussionThread( threadUUID, discussionThreadTable::selectDiscussionThread );
        if (thread != null) {
            // reply only if threadUUID exists
            FSqrDiscussionThreadMessage message = createReplyMessage( threadUUID, replytoMessageId, messageText, messageAuthorUUID );
            thread.addAsReplytoMessage( message );
            this.discussionThreadTable.updateThread( thread );
        }
    }

    private FSqrDiscussionThreadMessage createReplyMessage( String threadUUID, String replytoMessageId, String messageText, String messageAuthorUUID ) {
        FSqrDiscussionThreadMessage replyMessage = new FSqrDiscussionThreadMessage( UuidUtil.getRandomUUID().toString(), messageText, messageAuthorUUID );
        replyMessage.setThreadUUID( threadUUID );
        replyMessage.setReplyTo( replytoMessageId );

        return replyMessage;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void reinitDatabaseTables() {
        this.discussionThreadTable.createTable();
        this.projectAndReviewsToThreadsTable.createTable();
    }
}
