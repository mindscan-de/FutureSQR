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
package de.mindscan.futuresqr.domain.databases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mindscan.futuresqr.core.uuid.UuidUtil;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThread;
import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThreadMessage;

/**
 * 
 */
public class FSqrDiscussionThreadRepositoryImpl {

    private FSqrApplicationServices applicationServices;

    private Map<String, FSqrDiscussionThread> threadTable = new HashMap<>();

    private Map<String, Map<String, ArrayList<String>>> projectAndRewviewToThreads = new HashMap<>();

    /**
     * 
     */
    public FSqrDiscussionThreadRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();
    }

    public void setApplicationServices( FSqrApplicationServices applicationServices ) {
        this.applicationServices = applicationServices;
    }

    public void createNewReviewThread( String projectId, String reviewId, String messageText, String messageAuthorUUID ) {
        FSqrDiscussionThread newThread = createNewThread( messageText, messageAuthorUUID );

        // insertThreadToDB
        threadTable.put( newThread.getDiscussionThreadUUID(), newThread );

        // 
        projectAndRewviewToThreads //
                        .computeIfAbsent( projectId, id -> new HashMap<>() )//
                        .computeIfAbsent( reviewId, id -> new ArrayList<String>() ) //
                        .add( newThread.getDiscussionThreadUUID() );
    }

    private FSqrDiscussionThread createNewThread( String messageText, String messageAuthorUUID ) {
        String newThreadUUID = UuidUtil.getRandomUUID().toString();

        FSqrDiscussionThreadMessage rootMessage = createRootMessage( newThreadUUID, messageText, messageAuthorUUID );
        FSqrDiscussionThread createdThread = new FSqrDiscussionThread( newThreadUUID, rootMessage, messageAuthorUUID );

        return createdThread;
    }

    private FSqrDiscussionThreadMessage createRootMessage( String newThreadUUID, String messageText, String messageAuthorUUID ) {
        FSqrDiscussionThreadMessage rootMessage = new FSqrDiscussionThreadMessage( messageText, messageAuthorUUID );

        rootMessage.setThreadUUID( newThreadUUID );
        return rootMessage;
    }

    public List<FSqrDiscussionThread> getDirectThreadsForReview( String projectId, String reviewId ) {
        ArrayList<FSqrDiscussionThread> result = new ArrayList<>();
        if (projectAndRewviewToThreads.containsKey( projectId )) {
            Map<String, ArrayList<String>> reviewsMapForProjectId = projectAndRewviewToThreads.get( projectId );
            if (reviewsMapForProjectId.containsKey( reviewId )) {
                List<String> threadList = reviewsMapForProjectId.get( reviewId );
                threadList.stream().forEach( tid -> result.add( threadTable.get( tid ) ) );
            }
        }

        return result;
    }

    public void updateMessage( String projectId, String reviewId, String threadUUID, String messageUUID, String newMessageText, String messageAuthorUUID ) {
        // check if project id 
        if (!projectAndRewviewToThreads.containsKey( projectId )) {
            return;
        }

        // check if reviewId exists
        if (!projectAndRewviewToThreads.get( projectId ).containsKey( reviewId )) {
            return;
        }

        // make sure this threadid is only in projectId and reviewId present, such that you can't edit 
        // someone eles's Threads in different projects/reviews
        if (!projectAndRewviewToThreads.get( projectId ).get( reviewId ).contains( threadUUID )) {
            return;
        }

        // update if threadUUID exists.
        if (threadTable.containsKey( threadUUID )) {
            FSqrDiscussionThread thread = threadTable.get( threadUUID );
            thread.updateMessage( messageUUID, newMessageText, messageAuthorUUID );
        }
    }

    public void replyToThread( String projectId, String reviewId, String threadUUID, String replytoMessageId, String messageText, String messageAuthorUUID ) {
        // check if project id 
        if (!projectAndRewviewToThreads.containsKey( projectId )) {
            return;
        }

        // check if reviewId exists
        if (!projectAndRewviewToThreads.get( projectId ).containsKey( reviewId )) {
            return;
        }

        // make sure this threadid is only in projectId and reviewId present, such that you can't edit 
        // someone eles's Threads in different projects/reviews
        if (!projectAndRewviewToThreads.get( projectId ).get( reviewId ).contains( threadUUID )) {
            return;
        }

        if (threadTable.containsKey( threadUUID )) {
            FSqrDiscussionThreadMessage message = createReplyMessage( threadUUID, replytoMessageId, messageText, messageAuthorUUID );
            FSqrDiscussionThread thread = threadTable.get( threadUUID );
            thread.addAsReplytoMessage( message );
        }
    }

    private FSqrDiscussionThreadMessage createReplyMessage( String threadUUID, String replytoMessageId, String messageText, String messageAuthorUUID ) {
        FSqrDiscussionThreadMessage replyMessage = new FSqrDiscussionThreadMessage( messageText, messageAuthorUUID );
        replyMessage.setThreadUUID( threadUUID );
        replyMessage.setReplyTo( replytoMessageId );

        return replyMessage;
    }
}
