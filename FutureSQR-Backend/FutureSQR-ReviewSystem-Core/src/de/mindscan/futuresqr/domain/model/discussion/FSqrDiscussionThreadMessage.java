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
import java.util.List;
import java.util.function.Consumer;

/**
 * 
 */
public class FSqrDiscussionThreadMessage {

    private String messageUUID = "";
    private String threadUUID = "";

    private String messageText = "";
    private String messageAuthorUUID = "";
    private String replyToMessageUUID = "";

    private List<FSqrDiscussionThreadMessage> directReplies = new ArrayList<>();

    // TODO: lastEditorUUID
    // TODO: lastEditTimestamp
    // TODO: createdTimestamp

    public FSqrDiscussionThreadMessage() {
    }

    public FSqrDiscussionThreadMessage( String messageText, String messageAuthorUUID ) {
        this.messageText = messageText;
        this.messageAuthorUUID = messageAuthorUUID;
    }

    public String getMessageAuthorUUID() {
        return messageAuthorUUID;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUUID() {
        return messageUUID;
    }

    public String getReplyToMessageUUID() {
        return replyToMessageUUID;
    }

    public String getThreadUUID() {
        return threadUUID;
    }

    public void setThreadUUID( String newThreadUUID ) {
        this.threadUUID = newThreadUUID;
    }

    public void updateMessage( String newMessageText, String messageAuthorUUID ) {
        // TODO set edited by / and also add timestamp when change was made.
        this.messageText = newMessageText;
    }

    public void setReplyTo( String replytoMessageId ) {
        this.replyToMessageUUID = replytoMessageId;
    }

    public List<FSqrDiscussionThreadMessage> getDirectReplies() {
        return this.directReplies;
    }

    public void addReply( FSqrDiscussionThreadMessage reply ) {
        if (reply == this) {
            return;
        }

        this.directReplies.add( reply );
    }

    public void traversePreOrder( Consumer<String> consumer ) {
        consumer.accept( this.getMessageUUID() );

        for (FSqrDiscussionThreadMessage fSqrDiscussionThreadMessage : directReplies) {
            fSqrDiscussionThreadMessage.traversePreOrder( consumer );
        }
    }

}
