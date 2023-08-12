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
package de.mindscan.futuresqr.core.dispatcher;

import de.mindscan.futuresqr.core.events.FSqrEvent;
import de.mindscan.futuresqr.core.events.FSqrThreadNopEvent;
import de.mindscan.futuresqr.core.events.FSqrThreadSigAbortRequestEvent;
import de.mindscan.futuresqr.core.queue.ThreadBoundArrayDeque;
import de.mindscan.futuresqr.core.thread.FSqrThread;

/**
 *  
 */
public class EventDispatcherThread extends FSqrThread {

    private ThreadBoundArrayDeque<FSqrEvent> eventQueue;
    private EventDispatcher eventDispatcher;
    private volatile boolean shutdown = false;

    public EventDispatcherThread( EventDispatcher eventDispatcher ) {
        super( "FSqr-EventDispatcher-Thread" );

        this.eventDispatcher = eventDispatcher;
        this.eventQueue = new ThreadBoundArrayDeque<FSqrEvent>( this );

        // update the event dispatcher with the particular event Queue able to suspend and resume this thread. 
        this.eventDispatcher.setEventQueue( this.eventQueue );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            while (!shutdown) {
                // this will suspend this event dispatcher thread if this queue is empty
                // and is resumed after any thread added an element to the deque.
                FSqrEvent eventToHandle = eventQueue.poll();

                // if it was empty the last time, we must ignore it.
                if (eventToHandle == null) {
                    continue;
                }

                if (eventToHandle instanceof FSqrThreadNopEvent) {
                    // No operation event (e.g. for testing and unlocking the EventDispatcherThread
                    continue;
                }

                // a SigAbortEvent, gracefully shuts down the EventDispatcherThread
                // another way would be to just invoke stop() on this thread from the outside.
                if (eventToHandle instanceof FSqrThreadSigAbortRequestEvent) {
                    break;
                }

                // actually we have a handle event method to call instead...
                // and dispatch event should put that into the queue

                // also this part and this event means, handleEvent runs in he event dispatcher thread. 
                eventDispatcher.handleEvent( eventToHandle );
            }
        }
        catch (Exception e) {
            // TODO: add logging some when later.
            e.printStackTrace();
        }
    }

    public void shutdown() {
        FSqrEvent shutDownEvent = new FSqrThreadSigAbortRequestEvent();
        this.shutdown = true;
        this.eventQueue.add( shutDownEvent );
    }
}
