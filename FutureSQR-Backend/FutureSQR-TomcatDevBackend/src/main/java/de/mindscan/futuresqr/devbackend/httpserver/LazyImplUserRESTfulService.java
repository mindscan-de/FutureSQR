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

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import de.mindscan.futuresqr.devbackend.httpresponse.OutputCsrfTokenModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputLoginDataModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputStatusOkayModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputUserProjectEntry;
import de.mindscan.futuresqr.devbackend.legacy.MultiPartFormdataParameters;
import de.mindscan.futuresqr.devbackend.legacy.MultiPartFormdataParser;
import de.mindscan.futuresqr.devbackend.projectdb.FSqrLazyProjectDatabaseImpl;
import de.mindscan.futuresqr.domain.application.FSqrApplication;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;
import de.mindscan.futuresqr.domain.repository.FSqrScmUserRepository;
import de.mindscan.futuresqr.domain.repository.FSqrUserToProjectRepository;

/**
 * TODO: refactor me to review system core.
 */
@javax.ws.rs.Path( "/user" )
public class LazyImplUserRESTfulService {

    private FSqrScmUserRepository userRepository = FSqrApplication.getInstance().getServices().getUserRepository();

    // TODO: implement singleton, which loads the UserDatabase and the ProjectDatabase.
    //       is important to provide data to the review system core, as long as we dont
    //       have any persistence.

    // TODO: next scm project database. so that we at least have two reasons and two opportunities to learn.
    private static FSqrLazyProjectDatabaseImpl projectDB = new FSqrLazyProjectDatabaseImpl();

    @javax.ws.rs.Path( "/authenticate" )
    @POST
    @Produces( "application/json" )
    // @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public String postLoginDataForAuthentication( //
                    String requestBody
//                    // EFF this shit - won't properly work value is null for some effing reason.....
//                    // EFF jakarta implementatuion - https://guntherrotsch.github.io/blog_2021/jaxrs-multipart-client.html
//                    @FormParam( "username" ) String username, //
//                    @FormParam( "password" ) String password 
    ) {
        MultiPartFormdataParameters parameters = MultiPartFormdataParser.createParser( requestBody ).parse();

        String username = parameters.getStringOrThrow( "username" );
        String password = parameters.getStringOrThrow( "password" );

        return postLoginDataForAuthentication( username, password );
    }

    private String postLoginDataForAuthentication( String username, String password ) {
        // #1 check if user is present in the userdatabase
        if (!this.userRepository.isLogonNamePresent( username )) {
            // todo provide a 404 and a good response
            throw new RuntimeException( "No such user or not authenticated. " + " username:'" + username + "'; password:'" + password + "'" );
        }

        // #2 get user entry using the username
        FSqrSystemUser userEntry = this.userRepository.getUserByLogonName( username );

        // #3 TODO: register the user as an authenticated user

        OutputLoginDataModel response = new OutputLoginDataModel( userEntry );

        // #4 figure out the roles and featureflags for this user
        // #5 if admin, add admin role to capabilities

        // #6 return
        if ("mindscan-de".equals( username )) {
            response.capabilities.roles.add( "admin" );
        }

        Gson gson = new Gson();
        return gson.toJson( response );
    }

    @javax.ws.rs.Path( "/reauthenticate" )
    @POST
    @Produces( "application/json" )
    public String postReauthenticationLoginData( String requestBody ) {
        MultiPartFormdataParameters parameters = MultiPartFormdataParser.createParser( requestBody ).parse();

        String assumedUserName = parameters.getStringOrThrow( "assumedusername" );

        return postReauthenticationLoginData_internal( assumedUserName );
    }

    private String postReauthenticationLoginData_internal( String assumedUserName ) {
        // TODO: reimplement python #postReauthenticateLoginData

        // #1 session handling and session checking for this alleged account.
        if (isAuthSession( assumedUserName )) {

            // #2 get user entry using the username
            FSqrSystemUser userEntry = this.userRepository.getUserByLogonName( assumedUserName );

            // #3 TODO: register the user as an authenticated user

            OutputLoginDataModel response = new OutputLoginDataModel( userEntry );

            // #4 figure out the roles and featureflags for this user
            // #5 if admin, add admin role to capabilities

            // #6 return
            if ("mindscan-de".equals( assumedUserName )) {
                response.capabilities.roles.add( "admin" );
            }

            Gson gson = new Gson();
            return gson.toJson( response );

        }
        OutputLoginDataModel response = new OutputLoginDataModel();

        Gson gson = new Gson();
        return gson.toJson( response );
    }

    private boolean isAuthSession( String assumedUserName ) {
        // TODO implement a real session handling
        return true;
    }

    @javax.ws.rs.Path( "/csrf" )
    @GET
    @Produces( "application/json" )
    public String getCrsfToken() {
        OutputCsrfTokenModel response = new OutputCsrfTokenModel();

        Gson gson = new Gson();
        return gson.toJson( response );
    }

    @javax.ws.rs.Path( "/logout" )
    @POST
    @Produces( "application/json" )
    public String postLogoutData( //
                    @FormParam( "username" ) String userName ) {
        // TODO: reimplement python #postLogoutData

        OutputStatusOkayModel response = new OutputStatusOkayModel();

        Gson gson = new Gson();
        return gson.toJson( response );
    }

    @javax.ws.rs.Path( "/starredprojects" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public String getUserStarredProjects( @QueryParam( "userid" ) String userUUID ) {
        Collection<FSqrScmProjectConfiguration> allProjects = projectDB.getAllProjects();

        FSqrUserToProjectRepository userToProjectRepository = FSqrApplication.getInstance().getServices().getUserToProjectRepository();
        Collection<String> starredProjects = userToProjectRepository.getAllStarredProjectsForUser( userUUID );

        System.out.println( "userid: '" + userUUID + "'" );
        System.out.println( "projects: " + Arrays.deepToString( starredProjects.toArray() ) );

        // TODO actually also filter the accessible projects, since they could be starred, before
        //      user lost access.
        List<OutputUserProjectEntry> response = allProjects.stream()//
                        .filter( x -> starredProjects.contains( x.getProjectId() ) )//
                        .map( c -> transform( userUUID, c ) ) //
                        .collect( Collectors.toList() );

        response.sort( new Comparator<OutputUserProjectEntry>() {
            @Override
            public int compare( OutputUserProjectEntry o1, OutputUserProjectEntry o2 ) {
                return o1.project_display_name.compareTo( o2.project_display_name );
            }
        } );

        Gson gson = new Gson();
        return gson.toJson( response );
    }

    @javax.ws.rs.Path( "/allaccessibleprojects" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public <T> String getUserAccessibleProjects( @QueryParam( "userid" ) String userUUID ) {
        Collection<FSqrScmProjectConfiguration> allProjects = projectDB.getAllProjects();

        // TODO actually also filter the accessible projects, since they could be starred, before
        //      user lost access.
        List<OutputUserProjectEntry> response = allProjects.stream()//
                        .map( c -> transform( userUUID, c ) ) //
                        .collect( Collectors.toList() );
        response.sort( new Comparator<OutputUserProjectEntry>() {
            @Override
            public int compare( OutputUserProjectEntry o1, OutputUserProjectEntry o2 ) {
                return o1.project_display_name.compareTo( o2.project_display_name );
            }
        } );
        Gson gson = new Gson();
        return gson.toJson( response );
    }

    private OutputUserProjectEntry transform( String userUUID, FSqrScmProjectConfiguration configuration ) {
        FSqrUserToProjectRepository userToProjectRepository = FSqrApplication.getInstance().getServices().getUserToProjectRepository();

        OutputUserProjectEntry transformed = new OutputUserProjectEntry();
        String projectId = configuration.getProjectId();
        transformed.project_id = projectId;
        transformed.project_display_name = configuration.getProjectDisplayName();
        transformed.description = configuration.getProjectDescription();

        transformed.is_starred = userToProjectRepository.isStarred( userUUID, projectId );

        return transformed;
    }

    // TODO: /ban
    // TODO: /unban
    // TODO: /add
    // TODO: /adminuserlist
    // TODO NEXT: /userdictionary 
    //      need this for add participant dialog - part (find user)  
}
