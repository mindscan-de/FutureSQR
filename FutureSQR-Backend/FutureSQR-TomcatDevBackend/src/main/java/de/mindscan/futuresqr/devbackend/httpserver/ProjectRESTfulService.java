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

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import de.mindscan.futuresqr.devbackend.httpresponse.OutputProjectRevisionsModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputSimpleProjectInformation;
import de.mindscan.futuresqr.devbackend.projectdb.FSqrLazyProjectDatabaseImpl;
import de.mindscan.futuresqr.devbackend.userdb.FSqrLazyUserToProjectDatabaseImpl;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;

/**
 * 
 */
@javax.ws.rs.Path( "/project" )
public class ProjectRESTfulService {

    private static FSqrLazyProjectDatabaseImpl projectDB = new FSqrLazyProjectDatabaseImpl();

    @javax.ws.rs.Path( "/testme" )
    @GET
    @Produces( "application/json" )
    public String getTest_JSON() {
        return "{\"hello\":\"world\"}";
    }

    @javax.ws.rs.Path( "{projectid}/information" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public String getSimpleProjectInformation( @PathParam( "projectid" ) String projectId ) {
        FSqrScmProjectConfiguration projectConfiguration = projectDB.getProjectConfiguration( projectId );

        OutputSimpleProjectInformation response = transform( projectConfiguration );

        Gson gson = new Gson();
        return gson.toJson( response );
    }

    private OutputSimpleProjectInformation transform( FSqrScmProjectConfiguration projectConfiguration ) {
        OutputSimpleProjectInformation transformed = new OutputSimpleProjectInformation();

        transformed.projectID = projectConfiguration.getProjectId();
        transformed.projectDisplayName = projectConfiguration.getProjectDisplayName();
        transformed.projectDescription = projectConfiguration.getProjectDescription();
        transformed.projectIsStarred = FSqrLazyUserToProjectDatabaseImpl.isStarred( projectConfiguration.getProjectId() );
        transformed.projectUuid = projectConfiguration.getProjectUuid();

        return transformed;
    }

    @javax.ws.rs.Path( "{projectid}/recentcommits" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public String getProjectRevisions( @PathParam( "projectid" ) String projectId ) {
        if (projectDB.hasProjectLocalPath( projectId )) {
            OutputProjectRevisionsModel response = new OutputProjectRevisionsModel();

            // TODO get systemConfiguration / systemInstance
            // TODO calculate the ScmRevisionsForLocalGitRepo - should be figured out, whether GIT or SVN repo.
            // TODO convert from SCMRevisionModel to OoutputProjectRevisionsRevisionEntry
            // TODO calculate, whether a revision id in given projectid has been assigned to a review.

            Gson gson = new Gson();
            return gson.toJson( response );
        }

        // return empty revision.
        OutputProjectRevisionsModel response = new OutputProjectRevisionsModel();
        Gson gson = new Gson();
        return gson.toJson( response );
    }
}
