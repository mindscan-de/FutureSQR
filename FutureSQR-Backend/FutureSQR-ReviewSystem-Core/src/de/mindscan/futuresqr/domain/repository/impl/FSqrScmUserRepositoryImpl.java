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
package de.mindscan.futuresqr.domain.repository.impl;

import java.util.function.Function;

import de.mindscan.futuresqr.domain.application.ApplicationServicesSetter;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;
import de.mindscan.futuresqr.domain.repository.FSqrScmUserRepository;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheAlternateScmAliasTableImpl;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheSystemUserTableImpl;

/**
 * TODO: rework the repository to use a database alongside the in-memory cache.
 * 
 * The basic idea is to provide a "Factory" or source of truth, where the UserData and the Alternate SCM 
 * Aliases can be retrieved, when they aren't cached.
 * 
 * Also we must make sure, that if an object is going to be changed, we want to inform the repository,
 * that a persistent change to was applied, such that the database persistence can be updated. We want
 * to make the persistence two ways, currently it is one way, from the storage into the memory.
 * 
 * For now, this solution is good enough, to provide enough seams to integrate the database persistence
 * dependencies into each of the repositories.
 */
public class FSqrScmUserRepositoryImpl implements FSqrScmUserRepository, ApplicationServicesSetter {

    private FSqrApplicationServices applicationServices;

    private InMemoryCacheAlternateScmAliasTableImpl aliasScmNameCache;
    private InMemoryCacheSystemUserTableImpl systemUserCache;

    // Proof of Concept
    private Function<String, FSqrSystemUser> systemUserPersistenceLoader;

    // TODO: we need some filters?
    // TODO: we need some uuid list to FSqrSystemUser list implementation, which we need for some operations, so
    //       that we can save some local user data. / Sort this?

    /**
     * 
     */
    public FSqrScmUserRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();
        this.systemUserCache = new InMemoryCacheSystemUserTableImpl();
        this.aliasScmNameCache = new InMemoryCacheAlternateScmAliasTableImpl();
        this.setPersistenceSystemUserLoader( this::uninitializedPersistenceLoader );
    }

    private FSqrSystemUser uninitializedPersistenceLoader( String userid ) {
        return null;
    }

    @Override
    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;
    }

    @Override
    public String getUserUUID( String scmAlias ) {
        return aliasScmNameCache.getUserIdForScmAlias( scmAlias );
    }

    @Override
    public void addUserHandle( String scmAlias, String authorUUID ) {
        this.aliasScmNameCache.addScmAlias( scmAlias, authorUUID );
    }

    @Override
    public void addUserEntry( FSqrSystemUser user ) {
        this.systemUserCache.putSystemUser( user.getUserUUID(), user );
    }

    @Override
    public boolean isUserUUIDPresent( String uuid ) {
        if (systemUserCache.isCached( uuid )) {
            return true;
        }

        // TODO use a UserPersistenceProvider

        return systemUserCache.isCached( uuid );
    }

    @Override
    public boolean isLogonNamePresent( String logonName ) {
        if (this.systemUserCache.isLoginNamePresent( logonName )) {
            return true;
        }

        // actually if not yet present, we might want to attempt to load this user
        // but we want to have some timeout, if such a user can't be laoded, for some certain non-available values.
        // this should implement a kind of second chance algorithm.

        return false;
    }

    @Override
    public FSqrSystemUser getUserByUUID( String uuid ) {
        // TODO: another concern / can be solved later.
        // well, we also need to know, whether we loaded a different user, such that we can load the scm aliasnames
        // also. But maybe we can extend the loader with an "andThen" part, when we set the loader... 

        return this.systemUserCache.getSystemUser( uuid, this.systemUserPersistenceLoader );

//        // when user is not present, we want to retrieve this single user entry, with the system user loader
//        // actually this caching logic should be transferred to the systemUserCache. 
//        FSqrSystemUser loadedSystemUser = this.systemUserPersistenceLoader.apply( uuid );
//        if (loadedSystemUser != null) {
//            // then we want to cache this entry, to not reload this entry.
//            this.systemUserCache.putSystemUser( uuid, loadedSystemUser );
//        }
//
//        return loadedSystemUser;
    }

    // Proof of Concept
    // basic idea, on inverse dependency for providing persisted data, e.g from a database or from 
    // TODO: Start with idea, on how to provide data, if not cached.
    public void setPersistenceSystemUserLoader( Function<String, FSqrSystemUser> loader ) {
        if (loader != null) {
            this.systemUserPersistenceLoader = loader;
        }
    }
}
