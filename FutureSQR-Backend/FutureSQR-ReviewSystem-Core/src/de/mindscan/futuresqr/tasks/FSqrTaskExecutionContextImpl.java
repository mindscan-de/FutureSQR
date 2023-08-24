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
package de.mindscan.futuresqr.tasks;

import de.mindscan.futuresqr.core.dispatcher.EventDispatcher;
import de.mindscan.futuresqr.core.dispatcher.TaskDispatcher;
import de.mindscan.futuresqr.domain.application.FSqrApplication;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;

/**
 * 
 */
public class FSqrTaskExecutionContextImpl implements FSqrTaskExecutionContext {

    private TaskDispatcher taskDispatcher;
    private EventDispatcher eventDispatcher;

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrApplicationServices getServices() {
        return FSqrApplication.getInstance().getServices();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setTaskDispatcher( TaskDispatcher taskDispatcher ) {
        this.taskDispatcher = taskDispatcher;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setEventDispatcher( EventDispatcher eventDispatcher ) {
        this.eventDispatcher = eventDispatcher;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public TaskDispatcher getTaskDispatcher() {
        return taskDispatcher;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

}
