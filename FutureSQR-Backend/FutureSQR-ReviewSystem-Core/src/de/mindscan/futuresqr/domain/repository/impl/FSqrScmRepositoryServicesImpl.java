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
package de.mindscan.futuresqr.domain.repository.impl;

import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.configuration.impl.FSqrScmConfigrationProvider;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectType;
import de.mindscan.futuresqr.domain.repository.FSqrScmRepositoryServices;
import de.mindscan.futuresqr.scmaccess.ScmAccessFactory;
import de.mindscan.futuresqr.scmaccess.ScmRepositoryServicesProvider;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

/**
 * 
 */
public class FSqrScmRepositoryServicesImpl implements FSqrScmRepositoryServices {

    private FSqrApplicationServices applicationServices;
    private ScmRepositoryServicesProvider gitScmRepositoryServicesProvider;

    /**
     * 
     */
    public FSqrScmRepositoryServicesImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();

        this.gitScmRepositoryServicesProvider = ScmAccessFactory.getEmptyRepositoryServicesProvider();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;

        this.gitScmRepositoryServicesProvider = ScmAccessFactory
                        .getGitRepositoryServicesProvider( new FSqrScmConfigrationProvider( services.getSystemConfiguration() ) );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void updateProjectCache( String projectId ) {
        // TODO: This is just a helper as long as we have no crawler, which retrieves the data from the SCM and puts 
        //       these information into a database.

        // -----------------------------------------
        // TODO: refactor this to Database retrieval
        // -----------------------------------------
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );
            String branchName = scmConfiguration.getScmGitAdminConfiguration().getDefaultBranchName();

            if (branchName != null && !branchName.trim().isEmpty()) {
                gitScmRepositoryServicesProvider.updateProjectCache( scmRepository, branchName );
            }
            else {
                System.out.println( "[updateProjectCache] - branchName is empty - must be fixed." );
            }
        }
    }

    private FSqrScmProjectConfiguration toScmConfiguration( String projectId ) {
        // this is already cached...
        return applicationServices.getConfigurationRepository().getProjectConfiguration( projectId );
    }

    private ScmRepository toScmRepository( FSqrScmProjectConfiguration scmConfiguration ) {

        // TDOO: 
        // - we need A factory, which can translate some FSqrScmProjectConfiguration to a useful ScmRepository Object
        // - we should not do this here....
        // depending on the type, we might want to distinguish between different needs according to the repository type.

        String repoCachePath = applicationServices.getSystemConfiguration().getSystemRepoCachePath();

        if (scmConfiguration.isScmProjectType( FSqrScmProjectType.git )) {
            if (scmConfiguration.hasLocalRepoPath()) {
                // not yet nice but better than before.
                return new ScmRepository( repoCachePath + scmConfiguration.getScmGitAdminConfiguration().localPath );
            }
        }

        // TODO: this has to be fixed soon...
        ScmRepository result = new ScmRepository( repoCachePath + "FutureSQR" );

        return result;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void cloneCheckoutToProjectCache( String projectId ) {
        // TODO Auto-generated method stub

    }

}
