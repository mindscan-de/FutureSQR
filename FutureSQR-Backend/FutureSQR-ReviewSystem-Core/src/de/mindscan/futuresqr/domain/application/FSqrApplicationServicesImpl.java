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
package de.mindscan.futuresqr.domain.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import de.mindscan.futuresqr.domain.configuration.FSqrSystemInstanceConfiguration;
import de.mindscan.futuresqr.domain.configuration.impl.FSqrSystemInstanceConfigurationImpl;
import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.connection.impl.FSqrSqliteDatabaseConnectionImpl;
import de.mindscan.futuresqr.domain.repository.FSqrCodeReviewRepository;
import de.mindscan.futuresqr.domain.repository.FSqrDatabaseBackedRepository;
import de.mindscan.futuresqr.domain.repository.FSqrDiscussionThreadRepository;
import de.mindscan.futuresqr.domain.repository.FSqrScmProjectConfigurationRepository;
import de.mindscan.futuresqr.domain.repository.FSqrScmProjectRevisionRepository;
import de.mindscan.futuresqr.domain.repository.FSqrScmRepositoryServices;
import de.mindscan.futuresqr.domain.repository.FSqrScmUserRepository;
import de.mindscan.futuresqr.domain.repository.FSqrUserToProjectRepository;
import de.mindscan.futuresqr.domain.repository.impl.FSqrCodeReviewRepositoryImpl;
import de.mindscan.futuresqr.domain.repository.impl.FSqrDiscussionThreadRepositoryImpl;
import de.mindscan.futuresqr.domain.repository.impl.FSqrScmProjectConfigurationRepositoryImpl;
import de.mindscan.futuresqr.domain.repository.impl.FSqrScmProjectRevisionRepositoryImpl;
import de.mindscan.futuresqr.domain.repository.impl.FSqrScmRepositoryServicesImpl;
import de.mindscan.futuresqr.domain.repository.impl.FSqrScmUserRepositoryImpl;
import de.mindscan.futuresqr.domain.repository.impl.FSqrUserToProjectRepositoryImpl;

/**
 * 
 */
public class FSqrApplicationServicesImpl implements FSqrApplicationServices {

    private FSqrScmProjectConfigurationRepositoryImpl configurationRepository;
    private FSqrScmProjectRevisionRepositoryImpl revisionRepository;
    private FSqrScmUserRepositoryImpl userRepository;
    private FSqrCodeReviewRepositoryImpl reviewRepository;
    private FSqrUserToProjectRepositoryImpl userToProjectRepository;
    private FSqrSystemInstanceConfigurationImpl systemConfiguration;
    private FSqrDiscussionThreadRepositoryImpl discussionRepository;
    private FSqrScmRepositoryServicesImpl scmRepositoryServices;
    private FSqrSqliteDatabaseConnectionImpl databaseConnection;

    /**
     * 
     */
    public FSqrApplicationServicesImpl() {
        // Overall System Startup Configuration (Core)
        this.systemConfiguration = new FSqrSystemInstanceConfigurationImpl();
        this.databaseConnection = new FSqrSqliteDatabaseConnectionImpl();

        // All the individual parts of the Review System (Domain Core)
        this.configurationRepository = new FSqrScmProjectConfigurationRepositoryImpl();
        this.revisionRepository = new FSqrScmProjectRevisionRepositoryImpl();
        this.userRepository = new FSqrScmUserRepositoryImpl();
        this.reviewRepository = new FSqrCodeReviewRepositoryImpl();
        this.userToProjectRepository = new FSqrUserToProjectRepositoryImpl();
        this.discussionRepository = new FSqrDiscussionThreadRepositoryImpl();
        this.scmRepositoryServices = new FSqrScmRepositoryServicesImpl();

        // we need to boot the instance
        // and then we actually need a way to provide some data from externalproviders, and then
        // finalize the boot of this application somehow.
        initializeServiceInstances( this );
    }

    void initializeServiceInstances( FSqrApplicationServices services ) {
        // TODO: get all repos and check if application setters and then
        // initialize through a loop.
        this.systemConfiguration.setApplicationServices( services );
        this.databaseConnection.setApplicationServices( services );
        this.configurationRepository.setApplicationServices( services );
        this.revisionRepository.setApplicationServices( services );
        this.userRepository.setApplicationServices( services );
        this.reviewRepository.setApplicationServices( services );
        this.userToProjectRepository.setApplicationServices( services );
        this.discussionRepository.setApplicationServices( services );
        this.scmRepositoryServices.setApplicationServices( services );
    }

    // TODO we want to provide some dataprovider ability, the application can ask, e.g. on restart or on demand, 
    //      when the data is not yet in memory (e.g. not cached)
    // TODO when we provide a data provider, and a data provider is not set, it will invoke the initial/essential data

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmProjectConfigurationRepository getConfigurationRepository() {
        return this.configurationRepository;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmProjectRevisionRepository getRevisionRepository() {
        return this.revisionRepository;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmUserRepository getUserRepository() {
        return this.userRepository;
    }

    /**
     * @return the reviewRepository
     */
    @Override
    public FSqrCodeReviewRepository getReviewRepository() {
        return reviewRepository;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrUserToProjectRepository getUserToProjectRepository() {
        return userToProjectRepository;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrSystemInstanceConfiguration getSystemConfiguration() {
        return systemConfiguration;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrDiscussionThreadRepository getDiscussionThreadRepository() {
        return discussionRepository;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmRepositoryServices getScmRepositoryServices() {
        return scmRepositoryServices;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrDatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Collection<FSqrDatabaseBackedRepository> getDatabaseBackedRepositories() {
        ArrayList<FSqrDatabaseBackedRepository> result = new ArrayList<>();

        Arrays.stream( getAllServiceRepos() ).forEach( repo -> {
            if (repo instanceof FSqrDatabaseBackedRepository) {
                result.add( (FSqrDatabaseBackedRepository) repo );
            }
        } );

        return result;
    }

    private Object[] getAllServiceRepos() {
        return new Object[] { this.systemConfiguration, this.databaseConnection, this.configurationRepository, this.revisionRepository, this.userRepository,
                        this.reviewRepository, this.userToProjectRepository, this.discussionRepository, this.scmRepositoryServices };
    }
}
