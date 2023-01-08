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

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

import com.google.gson.Gson;

import de.mindscan.futuresqr.devbackend.httpresponse.OutputCsrfTokenModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputLoginDataModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputStatusOkayModel;
import de.mindscan.futuresqr.devbackend.userdb.FSqrLazyUserDBEntry;
import de.mindscan.futuresqr.devbackend.userdb.FSqrLazyUserDatabaseImpl;

/**
 * 
 */
@javax.ws.rs.Path( "/user" )
public class LazyImplUserRESTfulService {

    private FSqrLazyUserDatabaseImpl userDB = new FSqrLazyUserDatabaseImpl();

    @javax.ws.rs.Path( "/authenticate" )
    @POST
    @Produces( "application/json" )
    public String postLoginDataForAuthentication( //
                    @FormParam( "username" ) String userName, //
                    @FormParam( "password" ) String password ) {
        // TODO: reimplement python #postLoginData

        // #1 check if user is present in the userdatabase
        if (!userDB.hasUser( userName )) {
            // todo provide a 404 and a good response
            throw new RuntimeException( "No such user or not authenticated." );
        }

        // #2 get user entry using the username
        FSqrLazyUserDBEntry userEntry = userDB.getUserEntryByLogonName( userName );

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
        if ("mindscan-de".equals( userName )) {
            response.capabilities.roles.add( "admin" );
        }

        Gson gson = new Gson();
        return gson.toJson( response );
    }

    @javax.ws.rs.Path( "/reauthenticate" )
    @POST
    @Produces( "application/json" )
    public String postReauthenticationLoginData( //
                    @FormParam( "assumedusername" ) String assumedUserName ) {
        // TODO: reimplement python #postReauthenticateLoginData   

        OutputLoginDataModel response = new OutputLoginDataModel();

        Gson gson = new Gson();
        return gson.toJson( response );
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

    // TODO: /ban
    // TODO: /unban
    // TODO: /add
    // TODO: /adminuserlist
    // TODO: /userdictionary
}
