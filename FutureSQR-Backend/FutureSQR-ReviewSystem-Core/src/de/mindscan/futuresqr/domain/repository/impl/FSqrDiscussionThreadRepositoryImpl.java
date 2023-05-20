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

import java.util.ArrayList;
import java.util.List;

import de.mindscan.futuresqr.core.uuid.UuidUtil;
import de.mindscan.futuresqr.domain.application.ApplicationServicesSetter;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThread;
import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThreadMessage;
import de.mindscan.futuresqr.domain.repository.FSqrDiscussionThreadRepository;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheDiscussionThreadIdsTableImpl;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheDiscussionThreadTableImpl;

/**
 * TODO: rework the repository to use a database instead of the in-memory + scm data pull implementation
 */
public class FSqrDiscussionThreadRepositoryImpl implements FSqrDiscussionThreadRepository, ApplicationServicesSetter {

    private FSqrApplicationServices applicationServices;

    // search key: ( threaduuid:string ) -> thread:FSqrDiscussionThread
    private InMemoryCacheDiscussionThreadTableImpl uuidToThreadsCache;

    // search key: ( projectId:string , reviewId:string ) -> List of DiscussionThread Ids:ArrayList<String>
    private InMemoryCacheDiscussionThreadIdsTableImpl projectAndReviewToThreadsCache;

    /**
     * 
     */
    public FSqrDiscussionThreadRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();
        this.projectAndReviewToThreadsCache = new InMemoryCacheDiscussionThreadIdsTableImpl();
        this.uuidToThreadsCache = new InMemoryCacheDiscussionThreadTableImpl();
    }

    @Override
    public void setApplicationServices( FSqrApplicationServices applicationServices ) {
        this.applicationServices = applicationServices;
    }

    @Override
    public FSqrDiscussionThread createNewReviewThread( String projectId, String reviewId, String messageText, String messageAuthorUUID ) {
        FSqrDiscussionThread newThread = createNewThread( messageText, messageAuthorUUID );

        // insertThreadToDB
        this.uuidToThreadsCache.putDiscussionThread( newThread.getDiscussionThreadUUID(), newThread );
        this.projectAndReviewToThreadsCache.addDiscussionThread( projectId, reviewId, newThread.getDiscussionThreadUUID() );

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
    public List<FSqrDiscussionThread> getDirectThreadsForReview( String projectId, String reviewId ) {
        if (!this.projectAndReviewToThreadsCache.isCached( projectId, reviewId )) {
            return new ArrayList<>();
        }

        // TODO: lookup/translation from uuid to Discussion-Thread.
        // some may be cached some may not.

        // TODO: provide a lookup/load function, in case that the thread is not cached.

        List<String> discussionThreadUUIDs = projectAndReviewToThreadsCache.getDiscussionThreadUUIDs( projectId, reviewId );

        return this.uuidToThreadsCache.lookupThreads( discussionThreadUUIDs );
    }

    @Override
    public void editMessage( String projectId, String reviewId, String threadUUID, String messageUUID, String newMessageText, String messageAuthorUUID ) {
        if (!this.projectAndReviewToThreadsCache.hasDiscussionThreadUUID( projectId, reviewId, threadUUID )) {
            return;
        }

        // update if threadUUID exists.
        if (uuidToThreadsCache.isCached( threadUUID )) {

            // TODO: actually we don't need to check for caching, otherwise we want to get and compute if absent 

            FSqrDiscussionThread thread = uuidToThreadsCache.getDiscussionThread( threadUUID );
            thread.updateMessage( messageUUID, newMessageText, messageAuthorUUID );
        }
    }

    @Override
    public void replyToThread( String projectId, String reviewId, String threadUUID, String replytoMessageId, String messageText, String messageAuthorUUID ) {
        if (!this.projectAndReviewToThreadsCache.hasDiscussionThreadUUID( projectId, reviewId, threadUUID )) {
            return;
        }

        if (uuidToThreadsCache.isCached( threadUUID )) {
            FSqrDiscussionThreadMessage message = createReplyMessage( threadUUID, replytoMessageId, messageText, messageAuthorUUID );

            // TODO: actually we don't need to check for caching, otherwise we want to get and compute if absent

            FSqrDiscussionThread thread = uuidToThreadsCache.getDiscussionThread( threadUUID );
            thread.addAsReplytoMessage( message );
        }
    }

    private FSqrDiscussionThreadMessage createReplyMessage( String threadUUID, String replytoMessageId, String messageText, String messageAuthorUUID ) {
        FSqrDiscussionThreadMessage replyMessage = new FSqrDiscussionThreadMessage( UuidUtil.getRandomUUID().toString(), messageText, messageAuthorUUID );
        replyMessage.setThreadUUID( threadUUID );
        replyMessage.setReplyTo( replytoMessageId );

        return replyMessage;
    }
}
