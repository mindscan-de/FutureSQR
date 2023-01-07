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
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

/**
 * 
 */
@javax.ws.rs.Path( "/user" )
public class LazyImplUserRESTfulService {

    @javax.ws.rs.Path( "/authenticate" )
    @POST
    @Produces( "application/json" )
    public String postLoginDataForAuthentication() {
        return "";
    }

    @javax.ws.rs.Path( "/reauthenticate" )
    @POST
    @Produces( "application/json" )
    public String postReauthenticationLoginData() {
        return "";
    }

    @javax.ws.rs.Path( "/crsf" )
    @GET
    @Produces( "application/json" )
    public String getCrsfToken() {
        return "";
    }

    @javax.ws.rs.Path( "/logout" )
    @POST
    @Produces( "application/json" )
    public String postLogoutData() {
        return "";
    }
}
