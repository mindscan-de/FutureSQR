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
package de.mindscan.futuresqr.crawlers;

import de.mindscan.futuresqr.crawlers.tasks.UpdateProjectCacheTask;
import de.mindscan.futuresqr.tasks.FSqrBackgroundTask;
import de.mindscan.futuresqr.tasks.FSqrTaskExecutionContext;
import de.mindscan.futuresqr.tasks.FSqrTaskExecutionContextImpl;

/**
 * 
 */
public class CrawlerTaskFactory {
    // TODO: I don't particular like it but bootstrapping the whole SCM crawler application will be done later.
    private static FSqrTaskExecutionContext taskContext = new FSqrTaskExecutionContextImpl();

    /**
     * 
     */
    public CrawlerTaskFactory() {
        // TODO: initialize a taskContext
    }

    FSqrBackgroundTask getUpdateProjectCacheTask( String projectIdentifier ) {
        FSqrBackgroundTask task = new UpdateProjectCacheTask( projectIdentifier );

        init( task );

        return task;
    }

    /**
     * @param task
     */
    private void init( FSqrBackgroundTask task ) {
        task.setSetTaskExecutionContext( taskContext );
    }

    public FSqrTaskExecutionContext getTaskContext() {
        return taskContext;
    }
}
