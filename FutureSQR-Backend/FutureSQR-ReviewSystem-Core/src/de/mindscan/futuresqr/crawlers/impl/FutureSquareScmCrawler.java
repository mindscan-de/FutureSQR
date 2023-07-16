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
package de.mindscan.futuresqr.crawlers.impl;

import java.util.Collection;

import de.mindscan.futuresqr.domain.application.FSqrApplication;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;
import de.mindscan.futuresqr.domain.repository.FSqrScmProjectConfigurationRepository;

/**
 * 
 */
public class FutureSquareScmCrawler {

    private FSqrApplication application;

    /**
     * 
     */
    public FutureSquareScmCrawler() {
        this.application = FSqrApplication.getInstance();
    }

    public void crawl() {
        FSqrApplicationServices services = this.application.getServices();

        FSqrScmProjectConfigurationRepository configurationRepository = services.getConfigurationRepository();

        Collection<FSqrScmProjectConfiguration> allActiveProjectConfigurations = configurationRepository.getAllActiveProjectConfigurations();

        for (FSqrScmProjectConfiguration scmProject : allActiveProjectConfigurations) {

            // TODO remove me: ignore everything except futuresqr project.
            if (!"futuresqr".equals( scmProject.getProjectId() )) {
                continue;
            }

            // actually we want to index this project history.
            // basically from newest to oldest, such that the newest are always available first in the database, also in case
            // it might be a real long running project

            // if something is archived we will do everything on special ui demand...
            // TODO, check if refresh intervall is exceeded, if not we should skip that
            // TODO, check if this project is retrievable (e.g. has a copy on disk) right now.

            // we actually want 
            // * retrieve the latest known revisionid (head) from database
            // TODO: ask projectRevisionPepository for newest known revision

            // * retrieve the head of a git/svn scm.
            FSqrRevisionFullChangeSet scmProjectHead = services.getScmRepositoryServices().getHeadRevisionFullChangeSetFromScm( scmProject.getProjectId() );

            if (scmProjectHead == null) {
                // TODO change return to "continue" later, when we are iterating over all scm projects.
                return;
            }

            //   according to repotype we have different collection and invocation strategies.....
            // * retrieve the scm history from since that revision - but, someone can come with a branch which started 
            //   earlier, and that needs to be indexed as well. 
            // * if not empty we want to insert this to our database
            // * maybe trigger a webhook or so, or to trigger some analytics/actions (e.g. automatically add revisions)
            //   actually add new work for to the work queue
            // * we may add another work queue item to retrieve the diffs ...  but lets do that later
        }

    }

    // TODO: we need the scm project configuration / e.g. refresh intervall,
    // 
    // for git and svn we must have different strategies, 
    //   like, for svn we need a server configuration, such that multiple projects can be observed at once, in cases
    //   where branches and projects are basically in the same "branches" folder, and checking for changes 
    // 

    // TODO we have a hen-egg situation
    // we want to index revision data, since latest indexed revision.... from zero... and insert these into the scm revisions table. 
    // for this we need some data in the scm revision table.... but in that case the newest commits first.... and then iterate to the oldest ones. 
}
