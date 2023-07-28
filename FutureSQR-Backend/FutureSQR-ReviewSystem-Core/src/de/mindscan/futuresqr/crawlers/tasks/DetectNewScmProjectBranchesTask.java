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
package de.mindscan.futuresqr.crawlers.tasks;

import de.mindscan.futuresqr.tasks.FSqrBackgroundTaskBase;

/**
 * This Task is to detect new ScmProjectBranches every once in a while. Depending
 * whether it is a SVN or a git project we may have different strategies.
 * 
 * This task may be expensive if done for a single repository e.g. SVN, where we 
 * actually only need to observe a path high enough and do a "passive" discovery.
 * and compare each of the paths, whether it is a trunk or an already known branch.
 * add (a directory or svn copy operations will be able to create a new branch).
 * 
 * For git this needs to be done per project.
 * 
 * Instead of understanding this task as a always DetectNewScmProejctBranches, this
 * task may be initiated by the administrator by hand and is a once-task, when a
 * project is configured.
 * 
 * Maybe we need a SVNBranchDetector, such that branches can be detected passively,
 * and once a path is found, we then fire an event, that this a new branch was 
 * discovered for a project.
 * 
 * 
 */
public class DetectNewScmProjectBranchesTask extends FSqrBackgroundTaskBase {

    private String projectIdentifier;

    /**
     *  
     */
    public DetectNewScmProjectBranchesTask( String projectIdentifier ) {
        this.projectIdentifier = projectIdentifier;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        // TODO Auto-generated method stub
        // according to the scm we want to read all branches, and compare 
        // them to known branches
        // in case something is new we add this configuration to the current 
        // scm-configuration and emit a NewBranchDiscoveredEvent

    }

    // this is for passive discovery....
    // 
    // TODO: we need the scm project configuration / e.g. refresh intervall,
    // 
    // for git and svn we must have different strategies, 
    //   like, for svn we need a server configuration, such that multiple projects can be observed at once, in cases
    //   where branches and projects are basically in the same "branches" folder, and checking for changes 
    // 

}
