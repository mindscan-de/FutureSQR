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
package de.mindscan.futuresqr.domain.databases;

import java.util.HashMap;
import java.util.Map;

import de.mindscan.futuresqr.domain.application.ApplicationServicesSetter;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;

/**
 * 
 */
public class FSqrScmUserRepositoryImpl implements ApplicationServicesSetter {

    private FSqrApplicationServices applicationServices;
    private Map<String, String> userHandleToUUID;
    private Map<String, FSqrSystemUser> uuidToSystemUser;
    private Map<String, String> loginnameToUuid;

    /**
     * 
     */
    public FSqrScmUserRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();
        this.userHandleToUUID = new HashMap<>();
        this.uuidToSystemUser = new HashMap<>();
        this.loginnameToUuid = new HashMap<>();
    }

    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;
    }

    public String getUserUUID( String authorId ) {
        return userHandleToUUID.getOrDefault( authorId, authorId );
    }

    public void addUserHandle( String authorHandle, String authorUUID ) {
        this.userHandleToUUID.putIfAbsent( authorHandle, authorUUID );
    }

    public void addUserEntry( FSqrSystemUser user ) {
        this.uuidToSystemUser.put( user.getUserUUID(), user );
        this.loginnameToUuid.put( user.getUserLoginName(), user.getUserUUID() );
    }

    public boolean isUserUUIDPresent( String uuid ) {
        return uuidToSystemUser.containsKey( uuid );
    }

    public boolean isLogonNamePresent( String logonName ) {
        return this.loginnameToUuid.containsKey( logonName );
    }

    public FSqrSystemUser getUserByUUID( String uuid ) {
        if (isUserUUIDPresent( uuid )) {
            return uuidToSystemUser.get( uuid );
        }
        return null;
    }
}
