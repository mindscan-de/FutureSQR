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
package de.mindscan.futuresqr.core.queue;

import java.util.ArrayDeque;

import de.mindscan.futuresqr.core.thread.FSqrThread;

/**
 * This is a deque, which will suspend the boundThread in case the deque is 
 * empty and it will wake up the boundThread in case an element is added to the
 * ArrayDeque. 
 *   
 */
public class ThreadBoundArrayDeque<E> extends ArrayDeque<E> {

    /**
     * 
     */
    private static final long serialVersionUID = 62854694897881364L;
    private FSqrThread boundThread;

    /**
     * 
     */
    public ThreadBoundArrayDeque( FSqrThread boundThread ) {
        this.boundThread = boundThread;
    }

    /** 
     * {@inheritDoc}
     */
    @SuppressWarnings( "removal" )
    @Override
    synchronized public void addFirst( E e ) {
        if (e == null) {
            return;
        }

        super.addFirst( e );

        boundThread.resume();
    }

    /** 
     * {@inheritDoc}
     */
    @SuppressWarnings( "removal" )
    @Override
    synchronized public void addLast( E e ) {
        if (e == null) {
            return;
        }

        super.addLast( e );

        boundThread.resume();
    }

    /** 
     * {@inheritDoc}
     */
    @SuppressWarnings( "removal" )
    @Override
    synchronized public E pollFirst() {
        E element = super.pollFirst();

        if (element == null) {
            boundThread.suspend();
        }

        return element;
    }

    /** 
     * {@inheritDoc}
     */
    @SuppressWarnings( "removal" )
    @Override
    synchronized public E pollLast() {
        E element = super.pollLast();

        if (element == null) {
            boundThread.suspend();
        }

        return element;
    }

}
