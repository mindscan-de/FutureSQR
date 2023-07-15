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

import de.mindscan.futuresqr.domain.application.FSqrApplication;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
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
        FSqrScmProjectConfiguration futureSqrProject = configurationRepository.getProjectConfiguration( "futuresqr" );

        // actually we want to index this project history.
        // basically from newest to oldest, such that the newest are always available first in the database, also in case
        // it might be a real long running project
    }

    // TODO: we need the system configuration
    // TODO: we need the scm project configuration / e.g. refresh intervall,
    // 
    // for git and svn we must have different strategies, 
    //   like, for svn we need a server configuration, such that multiple projects can be observed at once, in cases
    //   where branches and projects are basically in the same "branches" folder, and checking for changes 
    // 

    // TODO we have a hen-egg situation
    // we want to index revision data, since latest indexed revision.... from zero... and insert these into the scm revisions table. 
    // for this we need some data in the scm revision table.... 
}
