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

import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.configuration.impl.FSqrScmConfigrationProvider;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectType;
import de.mindscan.futuresqr.domain.model.m2m.ScmRepositoryFactory;
import de.mindscan.futuresqr.scmaccess.ScmAccessFactory;
import de.mindscan.futuresqr.scmaccess.ScmRepositoryServicesProvider;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;
import de.mindscan.futuresqr.tasks.FSqrBackgroundTaskBase;

/**
 * 
 */
public class UpdateProjectCacheTask extends FSqrBackgroundTaskBase {

    private String projectIdentifier;

    /**
     * 
     */
    public UpdateProjectCacheTask( String projectIdentifier ) {
        this.projectIdentifier = projectIdentifier;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void taskExecute() {

        this.updateProject( projectIdentifier );

    }

    public void updateProject( String projectId ) {
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );
            String branchName = scmConfiguration.getScmGitAdminConfiguration().getDefaultBranchName();

            if (branchName != null && !branchName.trim().isEmpty()) {
                FSqrApplicationServices applicationServices = this.getApplicationServices();

                ScmRepositoryServicesProvider gitScmRepositoryServicesProvider = ScmAccessFactory
                                .getGitRepositoryServicesProvider( new FSqrScmConfigrationProvider( applicationServices.getSystemConfiguration() ) );

                gitScmRepositoryServicesProvider.updateProjectCache( scmRepository, branchName );
            }
            else {
                System.out.println( "[updateProjectCache] - branchName is empty - must be fixed." );
            }
        }

    }

    private FSqrScmProjectConfiguration toScmConfiguration( String projectId ) {
        FSqrApplicationServices applicationServices = this.getApplicationServices();

        return applicationServices.getConfigurationRepository().getProjectConfiguration( projectId );
    }

    private ScmRepository toScmRepository( FSqrScmProjectConfiguration scmConfiguration ) {
        FSqrApplicationServices applicationServices = this.getApplicationServices();

        return ScmRepositoryFactory.toScmRepository( applicationServices.getSystemConfiguration(), scmConfiguration );
    }

}
