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
package de.mindscan.futuresqr.domain.model.discussion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public class FSqrDiscussionThread {

    private String discussionThreadUUID = "";
    private String discussionThreadAuthor = "";

    private Map<String, FSqrDiscussionThreadMessage> discussionMessages = new HashMap<>();

    private FSqrDiscussionThreadMessage discussionThreadRootMessage = null;

    private boolean discussionThreadInlined = false;
    private long discussionThreadCreatedTimestamp = 0L;

    // private codeReference
    // private FSqrDiscussionThreadCodeReference codeReference;
    // what is a code reference? actually we must be able to have multiple code references for the same fsqr discussionthread

    // TODO: maybe it is useful to reference the project id and the review id, it is just for navigation 

    // TODO: whenWasThis thread created. / as long in seconds / milliseconds
    // TODO: is this discussion thread resolved
    // TODO: implement a tree structure for the discussion thread, instead of hacking the messageOrder.

    public FSqrDiscussionThread() {
        // intentionally left blank
    }

    public FSqrDiscussionThread( String newThreadUUID, FSqrDiscussionThreadMessage rootMessage, String messageAuthorUUID ) {
        this.discussionThreadUUID = newThreadUUID;
        this.discussionThreadAuthor = messageAuthorUUID;
        this.discussionMessages = new HashMap<>();
        this.discussionThreadRootMessage = null;
        this.discussionThreadCreatedTimestamp = rootMessage.getMessageCreatedTimestamp();

        this.addAsRootMessage( rootMessage );
    }

    private void addAsRootMessage( FSqrDiscussionThreadMessage rootMessage ) {
        this.discussionMessages.put( rootMessage.getMessageUUID(), rootMessage );
        this.discussionThreadRootMessage = rootMessage;
    }

    public void addAsReplytoMessage( FSqrDiscussionThreadMessage message ) {
        String replyToUUID = message.getReplyToMessageUUID();

        if (this.discussionMessages.containsKey( replyToUUID )) {
            // put the message into array.
            this.discussionMessages.put( message.getMessageUUID(), message );
            // insert the reply to the right parent message.
            this.discussionMessages.get( replyToUUID ).addReply( message );
        }
    }

    public String getDiscussionThreadUUID() {
        return discussionThreadUUID;
    }

    public String getDiscussionThreadAuthor() {
        return discussionThreadAuthor;
    }

    public Map<String, FSqrDiscussionThreadMessage> getDiscussionMessages() {
        return discussionMessages;
    }

    public List<String> getMessageOrder() {
        List<String> messageOrder = new ArrayList<String>();
        if (discussionThreadRootMessage != null) {
            this.discussionThreadRootMessage.traversePreOrder( messageOrder::add );
        }
        return messageOrder;
    }

    public void updateMessage( String messageUUID, String newMessageText, String messageAuthorUUID ) {
        if (discussionMessages.containsKey( messageUUID )) {
            FSqrDiscussionThreadMessage message = discussionMessages.get( messageUUID );
            message.updateMessage( newMessageText, messageAuthorUUID );
        }
    }

    public boolean isDiscussionThreadInlined() {
        return discussionThreadInlined;
    }

    public void setDiscussionThreadInlined( boolean discussionThreadInlined ) {
        this.discussionThreadInlined = discussionThreadInlined;
    }

    public long getDiscussionThreadCreatedTimestamp() {
        return discussionThreadCreatedTimestamp;
    }

}
