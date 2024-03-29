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
package de.mindscan.futuresqr.domain.repository.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;

/**
 * 
 */
public class InMemoryCacheSystemUserTableImpl {

    // search key: ( useruuid:string ) -> ( systemUser:FSqrSystemUser ) 
    private Map<String, FSqrSystemUser> uuidToSystemUserCache;
    private Map<String, String> loginnameToUuid;

    /**
     * 
     */
    public InMemoryCacheSystemUserTableImpl() {
        this.uuidToSystemUserCache = new HashMap<>();
        this.loginnameToUuid = new HashMap<>();
    }

    public boolean isCached( String userUuid ) {
        return uuidToSystemUserCache.containsKey( userUuid );
    }

    public void putSystemUser( String userUuid, FSqrSystemUser systemUser ) {
        this.uuidToSystemUserCache.put( userUuid, systemUser );
        this.loginnameToUuid.put( systemUser.getUserLoginName(), userUuid );
    }

    public boolean isLoginNamePresent( String logonName ) {
        return loginnameToUuid.containsKey( logonName );
    }

    public FSqrSystemUser getSystemUser( String userUuid ) {
        if (isCached( userUuid )) {
            return this.uuidToSystemUserCache.get( userUuid );
        }

        return null;
    }

    public FSqrSystemUser getSystemUser( String userUuid, Function<String, FSqrSystemUser> loadFunction ) {
        if (isCached( userUuid )) {
            return this.uuidToSystemUserCache.get( userUuid );
        }

        if (loadFunction != null) {
            // use this particular ugly implementation, because we also must register the login name, otherwise we just would use "computeIfAbsent"

            FSqrSystemUser loadedSystemUser = loadFunction.apply( userUuid );
            if (loadedSystemUser != null) {
                this.putSystemUser( userUuid, loadedSystemUser );
                return loadedSystemUser;
            }
        }

        return null;
    }

    public void updateUser( FSqrSystemUser user ) {
        this.putSystemUser( user.getUserUUID(), user );
    }

}
