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
package de.mindscan.futuresqr.devbackend.httpserver;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import de.mindscan.futuresqr.core.uuid.UuidUtil;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputAdminScmProjectConfigurationModel;
import de.mindscan.futuresqr.devbackend.legacy.MultiPartFormdataParameters;
import de.mindscan.futuresqr.devbackend.legacy.MultiPartFormdataParser;
import de.mindscan.futuresqr.domain.application.FSqrApplication;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.repository.FSqrDatabaseBackedRepository;
import de.mindscan.futuresqr.domain.repository.FSqrScmProjectConfigurationRepository;

/**
 * 
 */
@javax.ws.rs.Path( "/admin" )
public class AdminRESTfulService {

    @javax.ws.rs.Path( "/project/add" )
    @POST
    @Produces( MediaType.APPLICATION_JSON )
    public String postAddProject( String requestBody ) {
        MultiPartFormdataParameters postParams = MultiPartFormdataParser.createParser( requestBody ).parse();

        String repoURL = postParams.getStringOrThrow( "scmRepositoryURL" );
        String displayName = postParams.getStringOrThrow( "scmProjectDisplayName" );
        String projectId = postParams.getStringOrThrow( "scmProjectId" );
        String reviewPrefix = postParams.getStringOrThrow( "scmProjectReviewPrefix" );
        String description = postParams.getStringOrDefault( "scmProjectDescription", "" );

        FSqrScmProjectConfigurationRepository configurationRepository = FSqrApplication.getInstance().getServices().getConfigurationRepository();

        String projectUUID = UuidUtil.getRandomUUID().toString();

        FSqrScmProjectConfiguration newProjectConfiguration = new FSqrScmProjectConfiguration( projectId, displayName, projectUUID, 1 );
        newProjectConfiguration.setProjectDescription( description );
        newProjectConfiguration.setProjectReviewPrefix( reviewPrefix );
        newProjectConfiguration.setProjectRemoteRepoURL( repoURL );

        if (isValidProjectConfiguration( newProjectConfiguration )) {
            // actually we want to return the new project data, that we can then provide more configurations.
            // TODO: we want to provide the additional configurations after adding this project.
            configurationRepository.addScmProjectConfiguration( newProjectConfiguration );

            OutputAdminScmProjectConfigurationModel outputModel = new OutputAdminScmProjectConfigurationModel( newProjectConfiguration );

            Gson gson = new Gson();
            return gson.toJson( outputModel );
        }

        return "{}";
    }

    @javax.ws.rs.Path( "/project/{projectid}/configuration" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public String getProjectConfiguration( @PathParam( "projectid" ) String projectId ) {
        FSqrScmProjectConfigurationRepository configurationRepository = FSqrApplication.getInstance().getServices().getConfigurationRepository();

        FSqrScmProjectConfiguration projectConfiguration = configurationRepository.getProjectConfiguration( projectId );

        if (projectConfiguration != null) {
            OutputAdminScmProjectConfigurationModel outputModel = new OutputAdminScmProjectConfigurationModel( projectConfiguration );

            Gson gson = new Gson();
            return gson.toJson( outputModel );
        }
        return "{}";
    }

    @javax.ws.rs.Path( "/database/reinitDatabase" )
    @POST
    @Produces( MediaType.APPLICATION_JSON )
    public String postReinitDatabase( String requestBody ) {
        FSqrApplicationServices services = FSqrApplication.getInstance().getServices();

        Collection<FSqrDatabaseBackedRepository> allDbBackedRepos = services.getDatabaseBackedRepositories();
        for (FSqrDatabaseBackedRepository dbBackedRepo : allDbBackedRepos) {
            dbBackedRepo.reinitDatabaseTables();
        }

        return "{}";
    }

    @javax.ws.rs.Path( "/database/backupDatabase" )
    @POST
    @Produces( MediaType.APPLICATION_JSON )
    public String postBackupDatabase( String requestBody ) {
        FSqrApplicationServices services = FSqrApplication.getInstance().getServices();

        Collection<FSqrDatabaseBackedRepository> allDbBackedRepos = services.getDatabaseBackedRepositories();
        for (FSqrDatabaseBackedRepository dbBackedRepo : allDbBackedRepos) {
            // TODO: implement a general backup solution here?
            // dbBackedRepo.backupTables();
        }

        return "{}";
    }

    @javax.ws.rs.Path( "/database/restoreDatabase" )
    @POST
    @Produces( MediaType.APPLICATION_JSON )
    public String postRestoreDatabase( String requestBody ) {
        MultiPartFormdataParameters postParams = MultiPartFormdataParser.createParser( requestBody ).parse();

        String backupKey = postParams.getStringOrThrow( "backupKey" );

        FSqrApplicationServices services = FSqrApplication.getInstance().getServices();

        Collection<FSqrDatabaseBackedRepository> allDbBackedRepos = services.getDatabaseBackedRepositories();
        for (FSqrDatabaseBackedRepository dbBackedRepo : allDbBackedRepos) {
            // TODO: implement a general restore solution here?
            // dbBackedRepo.restoreTables();
        }

        return "{}";
    }

    private boolean isValidProjectConfiguration( FSqrScmProjectConfiguration newProjectConfiguration ) {
        return newProjectConfiguration != null;
    }
}
