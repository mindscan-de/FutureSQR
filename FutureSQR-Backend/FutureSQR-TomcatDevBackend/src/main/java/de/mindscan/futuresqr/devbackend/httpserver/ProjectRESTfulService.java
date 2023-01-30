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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import de.mindscan.futuresqr.devbackend.httpresponse.OutputFileChangeInformation;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputFileContentForRevisionModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputProjectRevisionsModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputProjectRevisionsRevisionEntry;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputSimpleProjectInformation;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputSingleCommitFullChangeSet;
import de.mindscan.futuresqr.devbackend.projectdb.FSqrLazyProjectDatabaseImpl;
import de.mindscan.futuresqr.devbackend.userdb.FSqrLazyUserToProjectDatabaseImpl;
import de.mindscan.futuresqr.domain.application.FSqrApplication;
import de.mindscan.futuresqr.domain.databases.FSqrScmProjectRevisionRepositoryImpl;
import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.FSqrRevisionFileChangeList;
import de.mindscan.futuresqr.domain.model.FSqrScmHistory;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;
import de.mindscan.futuresqr.domain.model.content.FSqrFileContentForRevision;

/**
 * 
 */
@javax.ws.rs.Path( "/project" )
public class ProjectRESTfulService {

    // this should be provided by a web-application instance, instead of a new instance each time.
    private static FSqrLazyProjectDatabaseImpl projectDB = new FSqrLazyProjectDatabaseImpl();
    private static FSqrLazyUserToProjectDatabaseImpl userToProjectDB = new FSqrLazyUserToProjectDatabaseImpl();

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
        transformed.projectIsStarred = userToProjectDB.isStarred( projectConfiguration.getProjectId() );
        transformed.projectUuid = projectConfiguration.getProjectUuid();

        return transformed;
    }

    @javax.ws.rs.Path( "{projectid}/recentcommits" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public String getProjectRevisions( @PathParam( "projectid" ) String projectId ) {
        if (projectDB.hasProjectLocalPath( projectId )) {
            OutputProjectRevisionsModel response = new OutputProjectRevisionsModel();

            FSqrScmProjectRevisionRepositoryImpl revisionProvider = FSqrApplication.getInstance().getServices().getRevisionRepository();
            FSqrScmHistory scmHistory = revisionProvider.getRecentRevisionHistory( projectId );

            scmHistory.getRevisions().stream().forEach( rev -> response.revisions.add( translate( rev ) ) );

            Gson gson = new Gson();
            return gson.toJson( response );
        }

        // return empty revision.
        OutputProjectRevisionsModel response = new OutputProjectRevisionsModel();
        Gson gson = new Gson();
        return gson.toJson( response );
    }

    private OutputProjectRevisionsRevisionEntry translate( FSqrRevision rev ) {
        return new OutputProjectRevisionsRevisionEntry( rev );
    }

    @javax.ws.rs.Path( "{projectid}/revision/{revisionid}/information" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public String getRevisionInformation( @PathParam( "projectid" ) String projectId, @PathParam( "revisionid" ) String revisionId ) {
        if (projectDB.hasProjectLocalPath( projectId )) {
            FSqrScmProjectRevisionRepositoryImpl revisionProvider = FSqrApplication.getInstance().getServices().getRevisionRepository();
            FSqrRevision revisionInfo = revisionProvider.getSimpleRevisionInformation( projectId, revisionId );

            OutputProjectRevisionsRevisionEntry response = new OutputProjectRevisionsRevisionEntry( revisionInfo );
            Gson gson = new Gson();
            return gson.toJson( response );
        }

        OutputProjectRevisionsRevisionEntry response = new OutputProjectRevisionsRevisionEntry();

        Gson gson = new Gson();
        return gson.toJson( response );
    }

    @javax.ws.rs.Path( "{projectid}/revisionfilelist/{revisionid}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public String getRevisionFileList( @PathParam( "projectid" ) String projectId, @PathParam( "revisionid" ) String revisionId ) {
        if (projectDB.hasProjectLocalPath( projectId )) {
            FSqrScmProjectRevisionRepositoryImpl revisionProvider = FSqrApplication.getInstance().getServices().getRevisionRepository();
            FSqrRevisionFileChangeList fileChangeList = revisionProvider.getRevisionFileChangeList( projectId, revisionId );

            OutputFileChangeInformation response = new OutputFileChangeInformation( fileChangeList );
            Gson gson = new Gson();
            return gson.toJson( response );
        }

        OutputFileChangeInformation response = new OutputFileChangeInformation();
        Gson gson = new Gson();
        return gson.toJson( response );
    }

    @javax.ws.rs.Path( "{projectid}/revisiondiff/{revisionid}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public String getRevisionFullChangeset( @PathParam( "projectid" ) String projectId, @PathParam( "revisionid" ) String revisionId ) {
        if (projectDB.hasProjectLocalPath( projectId )) {
            FSqrScmProjectRevisionRepositoryImpl revisionRepository = FSqrApplication.getInstance().getServices().getRevisionRepository();
            FSqrRevisionFullChangeSet fullChangeSet = revisionRepository.getRevisionFullChangeSet( projectId, revisionId );

            OutputSingleCommitFullChangeSet response = new OutputSingleCommitFullChangeSet( fullChangeSet );

            Gson gson = new Gson();
            return gson.toJson( response );
        }

        OutputSingleCommitFullChangeSet response = new OutputSingleCommitFullChangeSet();

        Gson gson = new Gson();
        return gson.toJson( response );
    }

    @javax.ws.rs.Path( "{projectid}/revisionfilecontent/{revisionid}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public String getRevisionFileContent( @PathParam( "projectid" ) String projectId, @PathParam( "revisionid" ) String revisionId,
                    @QueryParam( "filepath" ) String filePath ) {
        if (projectDB.hasProjectLocalPath( projectId )) {
            FSqrScmProjectRevisionRepositoryImpl revisionRepository = FSqrApplication.getInstance().getServices().getRevisionRepository();
            FSqrFileContentForRevision fileContent = revisionRepository.getFileContentForRevision( projectId, revisionId, filePath );

            OutputFileContentForRevisionModel response = new OutputFileContentForRevisionModel( fileContent );
            Gson gson = new Gson();
            return gson.toJson( response );
        }

        OutputFileContentForRevisionModel response = new OutputFileContentForRevisionModel();
        Gson gson = new Gson();
        return gson.toJson( response );
    }

//  @javax.ws.rs.Path( "{projectid}/recentreviews" )
//  @GET
//  @Produces( MediaType.APPLICATION_JSON )
//  public String getRecentReviews( @PathParam( "projectid" ) String projectId ) {
//      // TODO: implement me
//
//    Object response = null;
//
//      Gson gson = new Gson();
//      return gson.toJson( response );
//  }

}
