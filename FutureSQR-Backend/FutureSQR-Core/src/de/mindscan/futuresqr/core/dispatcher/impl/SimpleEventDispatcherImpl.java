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
package de.mindscan.futuresqr.core.dispatcher.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.mindscan.futuresqr.core.dispatcher.EventDispatcher;
import de.mindscan.futuresqr.core.events.FSqrEvent;
import de.mindscan.futuresqr.core.events.FSqrEventListener;

/**
 * 
 */
public class SimpleEventDispatcherImpl implements EventDispatcher {

    private Map<Class<? extends FSqrEvent>, Set<FSqrEventListener>> listenerMap;

    /**
     * 
     */
    public SimpleEventDispatcherImpl() {
        this.listenerMap = new HashMap<>();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void registerEventListener( Class<? extends FSqrEvent> eventClass, FSqrEventListener listener ) {
        if (eventClass == null || listener == null) {
            return;
        }

        // TODO: synchronize 
        listenerMap.computeIfAbsent( eventClass, k -> new HashSet<>() ).add( listener );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void dispatchEvent( FSqrEvent event ) {
        if (event == null) {
            return;
        }

        Class<? extends FSqrEvent> eventClass = event.getClass();

        // maybe also call the parent classes listeners in case of someone is subscribed to the base classes
        // but this can be saved for later.
        // TODO: implement parent class listener support.

        // eventClass.getSuperclass(); && instance of FSqrEvent
        Set<FSqrEventListener> invokedListeners = new HashSet<>();

        invokeEventListenersForClass( event, eventClass, invokedListeners );
    }

    public void invokeEventListenersForClass( FSqrEvent event, Class<? extends FSqrEvent> eventClass, Set<FSqrEventListener> invokedListeners ) {
        // TODO: synchronize this access
        Set<FSqrEventListener> eventListeners = new HashSet<>( listenerMap.get( eventClass ) );

        if (eventListeners == null) {
            return;
        }

        // call (all) the event handler(s) for this class.
        for (FSqrEventListener eventListener : eventListeners) {
            // Did we call this listener already ?
            if (invokedListeners.contains( eventListener )) {
                continue;
            }

            try {
                eventListener.handleEvent( event );
                invokedListeners.add( eventListener );
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
