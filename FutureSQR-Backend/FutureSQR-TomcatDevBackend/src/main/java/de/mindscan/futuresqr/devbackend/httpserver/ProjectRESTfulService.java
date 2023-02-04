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

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import de.mindscan.futuresqr.devbackend.httpresponse.OutputFileChangeInformation;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputFileContentForRevisionModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputFileHistoryModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputProjectRevisionsModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputProjectRevisionsRevisionEntry;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputRecentReviewsModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputSimpleProjectInformation;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputSingleCommitFullChangeSet;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputStatusOkayModel;
import de.mindscan.futuresqr.devbackend.legacy.MultiPartFormdataParameters;
import de.mindscan.futuresqr.devbackend.legacy.MultiPartFormdataParser;
import de.mindscan.futuresqr.devbackend.projectdb.FSqrLazyProjectDatabaseImpl;
import de.mindscan.futuresqr.domain.application.FSqrApplication;
import de.mindscan.futuresqr.domain.databases.FSqrCodeReviewRepositoryImpl;
import de.mindscan.futuresqr.domain.databases.FSqrScmProjectRevisionRepositoryImpl;
import de.mindscan.futuresqr.domain.databases.FSqrUserToProjectRepositoryImpl;
import de.mindscan.futuresqr.domain.model.FSqrCodeReview;
import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.FSqrRevisionFileChangeList;
import de.mindscan.futuresqr.domain.model.FSqrScmHistory;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;
import de.mindscan.futuresqr.domain.model.content.FSqrFileContentForRevision;
import de.mindscan.futuresqr.domain.model.history.FSqrFileHistory;

/**
 * 
 */
@javax.ws.rs.Path( "/project" )
public class ProjectRESTfulService {

    // TODO: get rid of it as soon as possible.
    private static final String HARDCODED_MINDSCAN_DE_UUID = "8ce74ee9-48ff-3dde-b678-58a632887e31";

    // -------------------------------------------------------------------------------------------
    // this should be provided by a web-application instance, instead of a new instance each time.
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

        // TODO: problem is that this project project information is user specific, because it may be starred by the user.
        // TODO: we might have to consider the current session context here.

        OutputSimpleProjectInformation response = transform( HARDCODED_MINDSCAN_DE_UUID, projectConfiguration );

        Gson gson = new Gson();
        return gson.toJson( response );
    }

    private OutputSimpleProjectInformation transform( String userUUID, FSqrScmProjectConfiguration projectConfiguration ) {
        OutputSimpleProjectInformation transformed = new OutputSimpleProjectInformation();

        transformed.projectID = projectConfiguration.getProjectId();
        transformed.projectDisplayName = projectConfiguration.getProjectDisplayName();
        transformed.projectDescription = projectConfiguration.getProjectDescription();
        transformed.projectUuid = projectConfiguration.getProjectUuid();

        FSqrUserToProjectRepositoryImpl userToProjectRepository = FSqrApplication.getInstance().getServices().getUserToProjectRepository();
        transformed.projectIsStarred = userToProjectRepository.isStarred( userUUID, projectConfiguration.getProjectId() );

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

    // TODO see python code
    // TODO: @app.get("/FutureSQR/rest/project/{projectid}/recentcommitsfromrevid/{fromrevisionid}", response_class=JSONResponse)    

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

    @javax.ws.rs.Path( "{projectid}/filehistory" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public String getParticularFileHistory( @PathParam( "projectid" ) String projectId, @PathParam( "revisionid" ) String revisionId,
                    @QueryParam( "filepath" ) String filePath ) {
        if (projectDB.hasProjectLocalPath( projectId )) {
            FSqrScmProjectRevisionRepositoryImpl revisionRepository = FSqrApplication.getInstance().getServices().getRevisionRepository();
            FSqrFileHistory fileHistory = revisionRepository.getParticularFileHistory( projectId, revisionId, filePath );

            OutputFileHistoryModel response = new OutputFileHistoryModel( fileHistory );
            Gson gson = new Gson();
            return gson.toJson( response );

        }

        OutputFileHistoryModel response = new OutputFileHistoryModel();
        Gson gson = new Gson();
        return gson.toJson( response );
    }

    // TODO: @app.get("/FutureSQR/rest/project/{projectid}/reviewdiff/{reviewid}", response_class=JSONResponse) <<-- refactor from this
    // TODO: @app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/diff", response_class=JSONResponse) <<--  refactor to this

    // TODO: @app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/filelist", response_class=JSONResponse)

    // TODO: @app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/information", response_class=JSONResponse)

    // TODO: @app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/revisiondetails", response_class=JSONResponse)

    // TODO: @app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/suggestedreviewers", response_class=JSONResponse)

    // TODO: @app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/threads", response_class=JSONResponse)

    @javax.ws.rs.Path( "{projectid}/recentreviews" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public String getRecentReviews( @PathParam( "projectid" ) String projectId ) {
        if (projectDB.hasProjectLocalPath( projectId )) {
            // TODO: implement loading the open and recently reviews.
            FSqrCodeReviewRepositoryImpl reviewRepository = FSqrApplication.getInstance().getServices().getReviewRepository();

            List<FSqrCodeReview> openReviews = reviewRepository.selectOpenReviews( projectId );
            List<FSqrCodeReview> closedReviews = reviewRepository.selectRecentlyClosedReviews( projectId );

            OutputRecentReviewsModel response = new OutputRecentReviewsModel( openReviews, closedReviews );

            Gson gson = new Gson();
            return gson.toJson( response );
        }

        OutputRecentReviewsModel response = new OutputRecentReviewsModel();

        Gson gson = new Gson();
        return gson.toJson( response );
    }

    // @app.post("/FutureSQR/rest/project/{projectid}/star", response_class=JSONResponse)
    @javax.ws.rs.Path( "{projectid}/star" )
    @POST
    @Produces( MediaType.APPLICATION_JSON )
    public String postProjectStarred( @PathParam( "projectid" ) String projectId, String requestBody ) {
        // System.out.println( requestBody );
        MultiPartFormdataParameters postParams = MultiPartFormdataParser.createParser( requestBody ).parse();

        if (projectDB.hasProjectLocalPath( projectId )) {
            // TODO: replace this getStringOrThrow to force userid as soon as we have a working parser
            String userid = postParams.getStringOrDefault( "userid", HARDCODED_MINDSCAN_DE_UUID );

            FSqrApplication.getInstance().getServices().getUserToProjectRepository().starProject( userid, projectId );
        }

        return "";
    }

    // @app.post("/FutureSQR/rest/project/{projectid}/unstar", response_class=JSONResponse)
    @javax.ws.rs.Path( "{projectid}/unstar" )
    @POST
    @Produces( MediaType.APPLICATION_JSON )
    public String postProjectUnstarred( @PathParam( "projectid" ) String projectId, String requestBody ) {
        // System.out.println( requestBody );
        MultiPartFormdataParameters postParams = MultiPartFormdataParser.createParser( requestBody ).parse();

        if (projectDB.hasProjectLocalPath( projectId )) {
            // TODO: replace this getStringOrThrow to force userid as soon as we have a working parser 
            String userid = postParams.getStringOrDefault( "userid", HARDCODED_MINDSCAN_DE_UUID );

            FSqrApplication.getInstance().getServices().getUserToProjectRepository().unstarProject( userid, projectId );
        }

        return "";
    }

    // TODO: @app.post *
    // @app.post("/FutureSQR/rest/project/{projectid}/review/{reviewid}/createthread", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/{reviewid}/replythread", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/{reviewid}/editmessage", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/create", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/close", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/reopen", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/delete", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/addreviewer", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/removereviewer", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/approvereview", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/concernreview", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/resetreview", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/appendrevision", response_class=JSONResponse)
    // @app.post("/FutureSQR/rest/project/{projectid}/review/removerevision", response_class=JSONResponse)
    // 

    @javax.ws.rs.Path( "{projectid}/updatecache" )
    @POST
    @Produces( MediaType.APPLICATION_JSON )
    public String postUpdateCache( @PathParam( "projectid" ) String projectId ) {
        if (projectDB.hasProjectLocalPath( projectId )) {
            FSqrScmProjectRevisionRepositoryImpl revisionRepository = FSqrApplication.getInstance().getServices().getRevisionRepository();

            revisionRepository.updateProjectCache( projectId );

            OutputStatusOkayModel response = new OutputStatusOkayModel();

            Gson gson = new Gson();
            return gson.toJson( response );
        }

        OutputStatusOkayModel response = new OutputStatusOkayModel();

        Gson gson = new Gson();
        return gson.toJson( response );

    }

}
