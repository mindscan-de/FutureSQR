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
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import de.mindscan.futuresqr.devbackend.httpresponse.OutputCsrfTokenModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputLoginDataModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputStatusOkayModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputUserProjectEntry;
import de.mindscan.futuresqr.devbackend.projectdb.FSqrLazyProjectDatabaseImpl;
import de.mindscan.futuresqr.devbackend.userdb.FSqrLazyUserDBEntry;
import de.mindscan.futuresqr.devbackend.userdb.FSqrLazyUserDatabaseImpl;
import de.mindscan.futuresqr.domain.application.FSqrApplication;
import de.mindscan.futuresqr.domain.databases.FSqrUserToProjectRepositoryImpl;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;

/**
 * 
 */
@RestController
@RequestMapping( "/rest/user" )
public class LazyImplUserRESTfulService {

    private static FSqrLazyUserDatabaseImpl userDB = new FSqrLazyUserDatabaseImpl();
    private static FSqrLazyProjectDatabaseImpl projectDB = new FSqrLazyProjectDatabaseImpl();

    @PostMapping( path = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE )
    // @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public OutputLoginDataModel postLoginDataForAuthentication( //
                    @RequestPart( "username" ) String username, //
                    @RequestPart( "password" ) String password ) {

        return postLoginDataForAuthenticationInternal( username, password );
    }

    private OutputLoginDataModel postLoginDataForAuthenticationInternal( String username, String password ) {
        // #1 check if user is present in the userdatabase
        if (!userDB.hasUser( username )) {
            // todo provide a 404 and a good response
            throw new RuntimeException( "No such user or not authenticated. " + " username:'" + username + "'; password:'" + password + "'" );
        }

        // #2 get user entry using the username
        FSqrLazyUserDBEntry userEntry = userDB.getUserEntryByLogonName( username );

        // #3 TODO: register the user as an authenticated user

        OutputLoginDataModel response = new OutputLoginDataModel();
        response.uuid = userEntry.uuid;
        response.loginname = userEntry.loginname;
        response.displayname = userEntry.displayname;
        response.avatarlocation = userEntry.avatarlocation;
        response.email = userEntry.email;

        // #4 figure out the roles and featureflags for this user
        // #5 if admin, add admin role to capabilities

        // #6 return
        if ("mindscan-de".equals( username )) {
            response.capabilities.roles.add( "admin" );
        }

        return response;
    }

    @PostMapping( path = "/reauthenticate", params = MediaType.APPLICATION_JSON_VALUE )
    public OutputLoginDataModel postReauthenticationLoginData( @RequestPart( "assumedusername" ) String assumedUserName ) {

        return postReauthenticationLoginData_internal( assumedUserName );
    }

    private OutputLoginDataModel postReauthenticationLoginData_internal( String assumedUserName ) {
        // TODO: reimplement python #postReauthenticateLoginData

        // #1 session handling and session checking for this alleged account.
        if (isAuthSession( assumedUserName )) {

            // #2 get user entry using the username
            FSqrLazyUserDBEntry userEntry = userDB.getUserEntryByLogonName( assumedUserName );

            // #3 TODO: register the user as an authenticated user

            OutputLoginDataModel response = new OutputLoginDataModel();
            response.uuid = userEntry.uuid;
            response.loginname = userEntry.loginname;
            response.displayname = userEntry.displayname;
            response.avatarlocation = userEntry.avatarlocation;
            response.email = userEntry.email;

            // #4 figure out the roles and featureflags for this user
            // #5 if admin, add admin role to capabilities

            // #6 return
            if ("mindscan-de".equals( assumedUserName )) {
                response.capabilities.roles.add( "admin" );
            }

            return response;

        }
        OutputLoginDataModel response = new OutputLoginDataModel();

        return response;
    }

    private boolean isAuthSession( String assumedUserName ) {
        // TODO implement a real session handling
        return true;
    }

    @GetMapping( path = "/csrf", produces = MediaType.APPLICATION_JSON_VALUE )
    public OutputCsrfTokenModel getCrsfToken() {
        OutputCsrfTokenModel response = new OutputCsrfTokenModel();

        return response;
    }

    @PostMapping( path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE )
    public OutputStatusOkayModel postLogoutData( //
                    @RequestPart( "username" ) String userName ) {
        // TODO: reimplement python #postLogoutData

        OutputStatusOkayModel response = new OutputStatusOkayModel();

        return response;
    }

    @GetMapping( path = "/starredprojects", produces = MediaType.APPLICATION_JSON_VALUE )
    public List<OutputUserProjectEntry> getUserStarredProjects( @RequestParam( "userid" ) String userUUID ) {
        Collection<FSqrScmProjectConfiguration> allProjects = projectDB.getAllProjects();

        FSqrUserToProjectRepositoryImpl userToProjectRepository = FSqrApplication.getInstance().getServices().getUserToProjectRepository();
        Set<String> starredProjects = userToProjectRepository.getAllStarredProjectsForUser( userUUID );

        // TODO actually also filter the accessible projects, since they could be
        // starred, before
        // user lost access.
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

        return response;
    }

    @GetMapping( path = "/allaccessibleprojects", produces = MediaType.APPLICATION_JSON_VALUE )
    public <T> List<OutputUserProjectEntry> getUserAccessibleProjects( @RequestParam String userUUID ) {
        Collection<FSqrScmProjectConfiguration> allProjects = projectDB.getAllProjects();

        // TODO actually also filter the accessible projects, since they could be
        // starred, before
        // user lost access.
        List<OutputUserProjectEntry> response = allProjects.stream()//
                        .map( c -> transform( userUUID, c ) ) //
                        .collect( Collectors.toList() );
        response.sort( new Comparator<OutputUserProjectEntry>() {
            @Override
            public int compare( OutputUserProjectEntry o1, OutputUserProjectEntry o2 ) {
                return o1.project_display_name.compareTo( o2.project_display_name );
            }
        } );
        return response;
    }

    private OutputUserProjectEntry transform( String userUUID, FSqrScmProjectConfiguration configuration ) {
        FSqrUserToProjectRepositoryImpl userToProjectRepository = FSqrApplication.getInstance().getServices().getUserToProjectRepository();

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
    // TODO: /userdictionary
}
