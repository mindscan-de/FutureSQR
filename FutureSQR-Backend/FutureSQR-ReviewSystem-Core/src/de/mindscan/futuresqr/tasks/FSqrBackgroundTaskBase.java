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

import de.mindscan.futuresqr.core.task.FSqrTask;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;

/**
 * This is basically an adapter for the FSqrTask system, but extended for the
 * FSqrBackgroundTask related to SCM tasks, which may or may not need some time
 * to complete. 
 */
public abstract class FSqrBackgroundTaskBase extends FSqrTask implements FSqrBackgroundTask {

    private FSqrTaskExecutionContext taskContext;

    /**
     * 
     */
    public FSqrBackgroundTaskBase() {

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setSetTaskExecutionContext( FSqrTaskExecutionContext taskContext ) {
        this.taskContext = taskContext;

    }

    protected FSqrTaskExecutionContext getTaskContext() {
        return taskContext;
    }

    protected FSqrApplicationServices getApplicationServices() {
        return taskContext.getServices();
    }

    // ----------------------------------------
    // Interface of the FSqrTask implementation
    // ----------------------------------------

    /** 
     * {@inheritDoc}
     */
    @Override
    final public void prepare() {
        // TODO: provide application services for task preparation
        taskPrepare();
        super.prepare();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void taskPrepare() {
        // intentionally left blank
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    final public void run() {
        // TODO: actually we want to provide an event consumer, such that a task can create new indicative events
        // convert this call execution to a FSqrBackgroundTask
        taskExecute();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    final public void cleanup() {
        taskCleanup();
        super.cleanup();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void taskCleanup() {
        // intentionally left blank
    }

}
