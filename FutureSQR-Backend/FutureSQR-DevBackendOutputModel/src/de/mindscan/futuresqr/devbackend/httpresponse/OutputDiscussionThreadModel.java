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
package de.mindscan.futuresqr.devbackend.httpresponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThread;
import de.mindscan.futuresqr.domain.model.discussion.FSqrDiscussionThreadMessage;

/**
 * TODO: actually we have two types of discussions generally review related and those which are embedded to code.
 * this should be distinguished. But for the output model this doesn't matter at all right now.
 * And then there are inherited discussions which are embedded to code, but were not marked as resolved.
 * 
 * For now this is the most simple model, which should be good enough.
 */
public class OutputDiscussionThreadModel {

    public String threadId = "";
    public String authorId = "";
    public List<String> messagesId = new ArrayList<>();
    public Map<String, OutputDiscussionMessageModel> messages = new HashMap<>();

    public OutputDiscussionThreadModel() {
    }

    public OutputDiscussionThreadModel( FSqrDiscussionThread thread ) {
        this.threadId = thread.getDiscussionThreadUUID();
        this.authorId = thread.getDiscussionThreadAuthor();

        this.messagesId = new ArrayList<>( thread.getMessageOrder() );

        this.messages = new HashMap<>();
        addMessages( thread.getDiscussionMessages() );
    }

    private void addMessages( Map<String, FSqrDiscussionThreadMessage> discussionMessages ) {
        discussionMessages.values().stream().forEach( msg -> this.messages.put( msg.getMessageUUID(), new OutputDiscussionMessageModel( msg ) ) );
    }
}
