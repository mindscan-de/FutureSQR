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
import de.mindscan.futuresqr.domain.databases.FSqrAlternateScmAliasesDatabaseTable;
import de.mindscan.futuresqr.domain.databases.FSqrUserDatabase;
import de.mindscan.futuresqr.domain.databases.impl.FSqrAlternateScmAliasesDatabaseTableImpl;
import de.mindscan.futuresqr.domain.databases.impl.FSqrUserDatabaseImpl;
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

    // Proof of Concept - this will be derived from the database session for now it is good enough for a POC
    private FSqrUserDatabase userDatabaseAccess;
    private FSqrAlternateScmAliasesDatabaseTable userAliasesDatabaseTable;

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

        // TODO: the userdatabase actually should provide persistenceSystemUserLoader...
        // TODO: maybe use a Factory of the application service to get the system user persistence loader.
        this.userDatabaseAccess = new FSqrUserDatabaseImpl();
        this.userAliasesDatabaseTable = new FSqrAlternateScmAliasesDatabaseTableImpl();
    }

    private FSqrSystemUser uninitializedPersistenceLoader( String userid ) {
        return null;
    }

    private FSqrSystemUser initializedDatabaseLoader( String userUuid ) {
        return this.userDatabaseAccess.selectUserByUUID( userUuid );
    }

    @Override
    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;

        //. we may have to reinitialize the userdatabase, or we provide a constructor with the application servcies....
        this.setPersistenceSystemUserLoader( this::initializedDatabaseLoader );
    }

    @Override
    public String getUserUUID( String scmAlias ) {
        return aliasScmNameCache.getUserIdForScmAlias( scmAlias, userAliasesDatabaseTable::getUuidForScmAlias );
    }

    @Override
    public void addUserHandle( String scmAlias, String authorUUID ) {
        this.userAliasesDatabaseTable.insertUserAlias( scmAlias, authorUUID );
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

        // TODO: test if a negative answer for this uuid is cached and save unsuccessful database operation.

        // getUserByUUID operation will invoke the systemUserPersistenceLoader.
        FSqrSystemUser user = getUserByUUID( uuid );
        if (user != null) {
            return true;
        }

        // TODO: Actually we should cache the negative answer for some time.

        return false;
    }

    @Override
    public boolean isLogonNamePresent( String logonName ) {
        if (this.systemUserCache.isLoginNamePresent( logonName )) {
            return true;
        }

        // TODO: test if a negative answer for this logonname is cached and save unsuccessful database operation.

        if (userDatabaseAccess.isLoginNamePresent( logonName )) {
            return true;
        }

        // TODO: actually we should cache the negative answer for some time.

        return false;
    }

    @Override
    public FSqrSystemUser getUserByUUID( String uuid ) {
        return this.systemUserCache.getSystemUser( uuid, this.systemUserPersistenceLoader );
    }

    // Proof of Concept
    // basic idea, on inverse dependency for providing persisted data, e.g from a database or from 
    // TODO: Start with idea, on how to provide data, if not cached.
    public void setPersistenceSystemUserLoader( Function<String, FSqrSystemUser> loader ) {
        if (loader != null) {
            this.systemUserPersistenceLoader = loader;
        }
        else {
            this.systemUserPersistenceLoader = this::uninitializedPersistenceLoader;
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrSystemUser getUserByLogonName( String username ) {
        FSqrSystemUser userEntry = this.userDatabaseAccess.selectUserByLoginName( username );

        if (userEntry != null) {
            // If someone requested this user by logon, we will need this user soon in the system cache as well...
            this.systemUserCache.putSystemUser( userEntry.getUserUUID(), userEntry );
        }

        return userEntry;
    }
}
