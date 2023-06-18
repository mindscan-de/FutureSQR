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

import java.util.Collection;

import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.databases.FSqrAlternateScmAliasesDatabaseTable;
import de.mindscan.futuresqr.domain.databases.FSqrUserTable;
import de.mindscan.futuresqr.domain.databases.impl.FSqrAlternateScmAliasesDatabaseTableImpl;
import de.mindscan.futuresqr.domain.databases.impl.FSqrUserTableImpl;
import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;
import de.mindscan.futuresqr.domain.repository.FSqrScmUserRepository;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheAlternateScmAliasTableImpl;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheSystemUserTableImpl;

/**
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
public class FSqrScmUserRepositoryImpl implements FSqrScmUserRepository {

    private FSqrApplicationServices applicationServices;

    private InMemoryCacheAlternateScmAliasTableImpl aliasScmNameCache;
    private InMemoryCacheSystemUserTableImpl systemUserCache;

    private FSqrUserTable systemUserTable;
    private FSqrAlternateScmAliasesDatabaseTable userAliasesDatabaseTable;

    /**
     * 
     */
    public FSqrScmUserRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();
        this.systemUserCache = new InMemoryCacheSystemUserTableImpl();
        this.aliasScmNameCache = new InMemoryCacheAlternateScmAliasTableImpl();

        this.systemUserTable = new FSqrUserTableImpl();
        this.userAliasesDatabaseTable = new FSqrAlternateScmAliasesDatabaseTableImpl();
    }

    @Override
    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;

        this.userAliasesDatabaseTable.setDatbaseConnection( this.applicationServices.getDatabaseConnection() );
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

        if (systemUserTable.isLoginNamePresent( logonName )) {
            return true;
        }

        // TODO: actually we should cache the negative answer for some time.

        return false;
    }

    @Override
    public FSqrSystemUser getUserByUUID( String uuid ) {
        return this.systemUserCache.getSystemUser( uuid, this.systemUserTable::selectUserByUUID );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrSystemUser getUserByLogonName( String username ) {
        FSqrSystemUser userEntry = this.systemUserTable.selectUserByLoginName( username );

        if (userEntry != null) {
            // If someone requested this user by logon, we will need this user soon in the system cache as well...
            this.systemUserCache.putSystemUser( userEntry.getUserUUID(), userEntry );
        }

        return userEntry;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void reinitDatabaseTables() {
        this.userAliasesDatabaseTable.createTable();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Collection<FSqrSystemUser> getAllUsers() {
        return this.systemUserTable.selectAllUsers();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrSystemUser banUser( String uuid ) {
        FSqrSystemUser user = this.getUserByUUID( uuid );

        if (user != null) {
            user.setBanned( true );
            // TODO:  write back the user entry to the database.
            // this.systemUserTable.updateUser(user);
            // this.systemUserCache.updateUser(user);
        }

        return user;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrSystemUser unbanUser( String uuid ) {
        FSqrSystemUser user = this.getUserByUUID( uuid );

        if (user != null) {
            user.setBanned( false );
            // TODO:  write back the user entry to the database.
            // this.systemUserTable.updateUser(user);
            // this.systemUserCache.updateUser(user);
        }

        return user;
    }
}
