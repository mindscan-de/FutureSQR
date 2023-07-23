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

/**
 * Idea is to have tasks, which can be scheduled once, or which will be scheduled according to some configuration
 * e.g. check remote repository for updates.
 * 
 * These Tasks may be triggered by a webhook such that the pull operations can be reduced on the different 
 * repositories. May be we have a supervised task, which can then trigger the projects deeper down in the 
 * hierarchy. e.g. svn history on most upper point, then match with svn project configuration and then update
 * these seperately. instead of checking thousands of urls on the same svn server. 
 * 
 * Tasks can be also maintenance tasks, which will do backup and other long running maintenance operations.
 * 
 * Tasks can be also mailing tasks, in case someone is added to a review and such.
 * 
 * Tasks can be analysis tasks, which may index certain parts or which calculates code references and complex 
 * diffs according. Which can run machine learning for classification, and reviewer suggestions based on Blame
 * and file edits.
 * 
 * Tasks can be to index file context and VFS (e.g. for git, such that the code can be browsed)
 * 
 * Taks should be completeable, such that a progress can be measured, such that the ui can later rerequest.
 * 
 * We need TaskSchedulers, TaskExecutors (workers), TaskDeque/TaskQueues, etc. 
 * 
 * Tasks may be able to add tasks to the queue, or the completion triggers other tasks.
 * 
 * TODO: Futures --> this might be something for he core project?
 * 
 * Maybe also the task concept is concept for the Core project, but the individual fsqr related tasks should 
 * remain here.
 */
public interface FSqrBackgroundTask {

    void setSetTaskExecutionContext( FSqrTaskExecutionContext taskContext );

    /**
     * TODO: 
     * we need to provide an information about the Execution Context, such that a task can signal success
     * failure or any other kind of events. 
     * 
     */
    void execute();

}
