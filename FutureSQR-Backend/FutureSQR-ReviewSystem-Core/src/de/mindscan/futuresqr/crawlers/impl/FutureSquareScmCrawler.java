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

import de.mindscan.futuresqr.crawlers.tasks.ScanIndexScmRevisionsBackwardsTask;
import de.mindscan.futuresqr.crawlers.tasks.ScanIndexScmRevisionsForwardTask;
import de.mindscan.futuresqr.domain.application.FSqrApplication;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;
import de.mindscan.futuresqr.domain.repository.FSqrScmProjectConfigurationRepository;
import de.mindscan.futuresqr.tasks.FSqrBackgroundTaskBase;

/**
 * TODO: EVENT STORMING:
 * probably need to do some - event storming wallpaper - to build this event based crawler mechanism, such that
 * it works autonomous. And it may also be triggered by ui interactions by some request events.
 */
public class FutureSquareScmCrawler {

    private FSqrApplication application;
    private FSqrTaskScheduler taskScheduler;
    private FSqrTaskRunner taskRunner;

    /**
     * 
     */
    public FutureSquareScmCrawler() {
        this.application = FSqrApplication.getInstance();

        this.taskScheduler = new FSqrTaskSchedulerImpl();
        this.taskRunner = new FSqrTaskRunnerImpl();
    }

    public void run() {
        // TODO setup the application

        // TODO then call crawl.
        // this.taskScheduler.schedule(new CrawlProjectOnBranchTask());

        // run all queried tasks
        while (this.taskScheduler.hasQueriedTasks()) {
            this.taskRunner.runQueriedTask( this.taskScheduler.popQueriedTask() );
        }
    }

    public void crawl() {
        FSqrApplicationServices services = this.application.getServices();

        FSqrScmProjectConfigurationRepository configurationRepository = services.getConfigurationRepository();

        Collection<FSqrScmProjectConfiguration> allActiveProjectConfigurations = configurationRepository.getAllActiveProjectConfigurations();

        for (FSqrScmProjectConfiguration scmProject : allActiveProjectConfigurations) {
            String projectId = scmProject.getProjectId();

            // TODO remove me: ignore everything except futuresqr project.
            if (!"futuresqr".equals( projectId )) {
                System.out.println( "Skipping project '" + projectId + "'" );
                continue;
            }

            System.out.println( "Crawling project '" + projectId + "'" );

            // TODO: also limit indexing to one branch at a time.
            String projectBranch = scmProject.getProjectDefaultBranch();

            System.out.println( "Crawling branch '" + projectBranch + "'" );

            // TODO: check that this project has a checkout available / 
            // if not we must initiate the checkout to local cache...
            // maybe add something to execution pipeline and then go to continue.

            // actually we want to index this project history.
            // basically from newest to oldest, such that the newest are always available first in the database, also in case
            // it might be a real long running project with many revision...

            // if something is archived we will do everything on special ui demand...
            // TODO, check if refresh intervall is exceeded, if not we should skip that
            // TODO, check if this project is retrievable (e.g. has a copy on disk) right now.

            // ----------------
            // we actually want
            // ----------------

            // * retrieve the head of a git/svn scm.
            FSqrRevisionFullChangeSet scmProjectHead = services.getScmRepositoryServices().getHeadRevisionFullChangeSetFromScm( projectId );
            if (scmProjectHead == null) {
                System.out.println( "No HeadRevision in Scm available. For '" + projectId + "'" );
                // probably because this project is not correct on disc or empty...?
                // maybe we need a pull...
                continue;
            }

            System.out.println( "Found Head Revison (SCM) '" + scmProjectHead.getRevisionId() + "' for '" + projectId + "'" );

            // * retrieve the latest known revisionid (head) from database
            // TODO: ask projectRevisionPepository for newest known revision
            // 
            FSqrRevision dbProjectHeadRevision = services.getRevisionRepository().retrieveHeadRevision( projectId, projectBranch );
            if (dbProjectHeadRevision == null) {
                System.out.println( "No known head revision in database for '" + projectId + "'" );
                // no entry in database so we must do maybe we should start inserting the revisions into the revision table....
                // i mean only if there is something in the project head...
                // maybe we should instantiate a new task and add that task to our work queue.

                // index backwards
                // index backward until either too old or when done.
                // background task 
                // not only we want to crawl the revision info, but also the diffs and other info as well / even though we might not keep the diffs forever 
                // and maybe not in a backup/restore/install situation.  but the revisions must be known, and the files which were touched by this revisions
                // this is for analytics of ownership of the revisions.
                // maybe also run the analytics for each (forward indexed revision, to whether to add the revision or not)

                scheduleTask( new ScanIndexScmRevisionsBackwardsTask( projectId, projectBranch, scmProjectHead.getRevisionId() ) );

                continue;
            }

            System.out.println( "Found Head revision (SQL) '" + dbProjectHeadRevision.getRevisionId() + "' for '" + projectId + "' " );

            // TODO: else now compare these two revisions
            if (scmProjectHead.getRevisionId().equals( dbProjectHeadRevision.getRevisionId() )) {
                // easy both are equal, we can skip indexing more.
                continue;
            }

            // here we know that the top of scm and the top of database do not match
            // well we should index from last known revision known to the database to 
            // the head revision.
            scheduleTask( new ScanIndexScmRevisionsForwardTask( projectId, projectBranch, dbProjectHeadRevision.getRevisionId() ) );

            // TODO: we have to access the default branches and all other branches

            //   according to repotype we have different collection and invocation strategies.....
            // * retrieve the scm history from since that revision - but, someone can come with a branch which started 
            //   earlier, and that needs to be indexed as well. 
            // * if not empty we want to insert this to our database
            // * maybe trigger a webhook or so, or to trigger some analytics/actions (e.g. automatically add revisions)
            //   actually add new work for to the work queue
            // * we may add another work queue item to retrieve the diffs ...  but lets do that later

        }

    }

    private void scheduleTask( FSqrBackgroundTaskBase task ) {
        this.taskScheduler.schedule( task );
    }

    // TODO we have a hen-egg situation
    // we want to index revision data, since latest indexed revision.... from zero... and insert these into the scm revisions table. 
    // for this we need some data in the scm revision table.... but in that case the newest commits first.... and then iterate to the oldest ones. 
}
