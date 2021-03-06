'''
Created on 24.07.2022

MIT License

Copyright (c) 2022 Maxim Gansert

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

@autor: Maxim Gansert
'''

THREADS_THREAD_ID          = 'threadId'
THREADS_PK_THREAD_ID       = THREADS_THREAD_ID 
THREADS_FK_AUTHOR_ID       = 'authorId'
THREADS_FK_MESSAGES        = 'messagesId'
# used for unpacking the data... with a map.
THREADS_MESSAGES           = 'messages'

MESSAGES_MESSAGE_ID        = 'messageId'
MESSAGES_PK_MESSAGES_ID    = MESSAGES_MESSAGE_ID
MESSAGES_FK_THREAD_ID      = 'threadId'
MESSAGES_MESSAGE           = 'message'
MESSAGES_FK_AUTHOR_ID      = 'authorId'
MESSAGES_REPLY_TO_ID       = 'replyToMsgId'
