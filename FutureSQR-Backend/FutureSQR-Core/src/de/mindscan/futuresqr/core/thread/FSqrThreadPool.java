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
package de.mindscan.futuresqr.core.thread;

/**
 * 
 */
public interface FSqrThreadPool {

    /**
     * Provides information whether the pooled WorkerThread queue is not empty.
     * @return <code>true</code> if a WorkerThread is available to be assigned to a Task, <code>false</code> otherwise, for example in case a shutdown for the pool is initiated.
     */
    boolean isWorkerThreadAvailable();

    /**
     * This will prepare all threads in the Pool.
     */
    void initializeThreadPool();

    /**
     * This method returns the first available thread from the thread workers queue.
     * @return A borrowed thread ready to be used for a task. 
     * @throws IllegalStateException in case there is no thread to borrow. Test that {@link #isWorkerThreadAvailable()} is <code>true</code> before borrowing.
     */
    FSqrWorkerThread borrowThread();

}
