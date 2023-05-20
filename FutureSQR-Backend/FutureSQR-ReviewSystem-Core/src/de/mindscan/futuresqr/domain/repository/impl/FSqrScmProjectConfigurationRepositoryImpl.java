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

import java.util.Collection;
import java.util.function.Function;

import de.mindscan.futuresqr.domain.application.ApplicationServicesSetter;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.databases.FSqrScmConfigurationDatabaseTable;
import de.mindscan.futuresqr.domain.databases.impl.FSqrScmConfigurationDatabaseTableImpl;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.repository.FSqrScmProjectConfigurationRepository;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheProjectConfigurationTableImpl;

/**
 * This provides the in-memory repository for the Source Code Management Project Configurations.
 * 
 * For performance reasons, we should load these once, because they are used the whole tiem, and 
 * use the initially provided project configurations.
 * 
 * This is where we store all the Project configurations and their type of SCM associated 
 * with this particular scm project. It provides an index of all projects and such.
 * 
 * This object should not be serializable.
 * 
 * TODO: rework the repository to use a database instead of the in-memory + scm data pull implementation
 */
public class FSqrScmProjectConfigurationRepositoryImpl implements FSqrScmProjectConfigurationRepository, ApplicationServicesSetter {

    private FSqrApplicationServices applicationServices;

    // search key ( projectid:string ) -> ( projectconfiguration:FSqrScmProjectConfiguration )
    private InMemoryCacheProjectConfigurationTableImpl scmProjectConfigurationCache;

    // Proof of Concept - SCM Configuration from a "persistent" storage.
    private FSqrScmConfigurationDatabaseTable scmConfigurationDatabase;

    // Proof of concept
    private Function<String, FSqrScmProjectConfiguration> configurationPersistenceLoader;

    /**
     * 
     */
    public FSqrScmProjectConfigurationRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();
        this.scmProjectConfigurationCache = new InMemoryCacheProjectConfigurationTableImpl();
        this.setConfigurationPersistenceLoader( this::uninitializedPersistenceLoader );

        // TODO: finally begin to implement a persistent storage for the Scm Project Configurations
        // TODO: maybe use a factory to derive this instance from the application Services.
        this.scmConfigurationDatabase = new FSqrScmConfigurationDatabaseTableImpl();
    }

    private FSqrScmProjectConfiguration uninitializedPersistenceLoader( String projectId ) {
        return null;
    }

    private FSqrScmProjectConfiguration initializedDatabaseLoader( String projectId ) {
        return this.scmConfigurationDatabase.selectScmConfigurationByProjectId( projectId );
    }

    @Override
    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;

        // w may have to reinitialite the userdatabase and the cache and such.
        this.setConfigurationPersistenceLoader( this::initializedDatabaseLoader );
    }

    // TODO use an alternate constructor with a projectConfigurationInitialProvider, which 
    //      provides the configurations, when this class is initialized

    @Override
    public Collection<FSqrScmProjectConfiguration> getAllProjectConfigurations() {
        // TODO: Do we want to cache these entries in the cache, or will we just do them when they are requested?
        return this.scmConfigurationDatabase.selectAllScmConfigurations();

        //  TODO implement that the select statement on the persistence instead of the cache.
        // return scmProjectConfigurationCache.getAllCachedScmConfigurations();
    }

    @Override
    public FSqrScmProjectConfiguration getProjectConfiguration( String projectId ) {
        return scmProjectConfigurationCache.getScmConfiguration( projectId, this.configurationPersistenceLoader );

        // TODO implement that select statement on the persistence instead of only the cache.

        // return scmProjectConfigurationCache.getScmConfiguration( projectId, this.configurationPersistenceLoader );
    }

    @Override
    public boolean hasProjectConfiguration( String projectId ) {
        if (scmProjectConfigurationCache.isCached( projectId )) {
            return true;
        }

        // TODO: test if negative answer for this project id is cached and save unsuccessful

        // try to retrieve with loader, if unsuccessful we might want to cache the absence
        FSqrScmProjectConfiguration projectConfiguration = this.getProjectConfiguration( projectId );
        if (projectConfiguration != null) {
            return true;
        }

        // TODO: Actually we should cahce the negative answer for some time.

        return false;
    }

    @Override
    public String getNewProjectReviewIdentifier( String projectId ) {
        if (!hasProjectConfiguration( projectId )) {
            throw new RuntimeException( "ProjectId is unknown" );
        }

        FSqrScmProjectConfiguration projectConfiguration = scmProjectConfigurationCache.getScmConfiguration( projectId, this.configurationPersistenceLoader );

        if (projectConfiguration == null) {
            throw new RuntimeException( "ProjectId is unknown" );
        }

        return projectConfiguration.createNewReviewIdentifierWithPrefix();
    }

    @Override
    public void addScmProjectConfiguration( FSqrScmProjectConfiguration projectConfiguration ) {
        String projectId = projectConfiguration.getProjectId();

        if (projectId == null || projectId.trim().isEmpty()) {
            return;
        }

        // do not add a second time - for now.
        if (hasProjectConfiguration( projectConfiguration.getProjectId() )) {
            return;
        }

        this.scmProjectConfigurationCache.putProjectConfiguration( projectId, projectConfiguration );
    }

    public void setConfigurationPersistenceLoader( Function<String, FSqrScmProjectConfiguration> loader ) {
        if (loader != null) {
            this.configurationPersistenceLoader = loader;
        }
        else {
            this.configurationPersistenceLoader = this::uninitializedPersistenceLoader;
        }

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean hasProjectLocalPath( String projectId ) {
        if (!hasProjectConfiguration( projectId )) {
            return false;
        }

        return getProjectConfiguration( projectId ).hasLocalRepoPath();
    }

}
