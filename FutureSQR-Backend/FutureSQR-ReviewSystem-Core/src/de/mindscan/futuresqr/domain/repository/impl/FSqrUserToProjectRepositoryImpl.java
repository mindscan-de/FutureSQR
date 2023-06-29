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
import java.util.HashSet;
import java.util.LinkedHashSet;

import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.databases.FSqrUserToProjectDatabaseTable;
import de.mindscan.futuresqr.domain.databases.impl.FSqrUserToProjectDatabaseTableImpl;
import de.mindscan.futuresqr.domain.repository.FSqrUserToProjectRepository;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheStringToAtomicIntegerImpl;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheUserStarredProjectTableImpl;

/**
 * Idea here is to provide methods and data and such related to user to project relations.
 * This can be the starred projects by user or starring users by projects.
 *  
 * MAYBE Later, or MAYBE somewhere else:
 * Also we might also provide methods, to calculate which project a user has access to,
 * because this is related to the user to project relationship as well but a different
 * attribute.
 * 
 */
public class FSqrUserToProjectRepositoryImpl implements FSqrUserToProjectRepository {

    private FSqrApplicationServices applicationServices;

    private InMemoryCacheUserStarredProjectTableImpl starredProjectsCache;
    private InMemoryCacheStringToAtomicIntegerImpl starredProjectCountsCache;

    private FSqrUserToProjectDatabaseTable userToProjectDatabaseTable;

    /**
     * 
     */
    public FSqrUserToProjectRepositoryImpl() {
        this.starredProjectsCache = new InMemoryCacheUserStarredProjectTableImpl();
        this.starredProjectCountsCache = new InMemoryCacheStringToAtomicIntegerImpl();
        this.applicationServices = new FSqrApplicationServicesUnitialized();
        this.userToProjectDatabaseTable = new FSqrUserToProjectDatabaseTableImpl();
    }

    @Override
    public void setApplicationServices( FSqrApplicationServices applicationServices ) {
        this.applicationServices = applicationServices;

        this.userToProjectDatabaseTable.setDatbaseConnection( this.applicationServices.getDatabaseConnection() );
    }

    @Override
    public Collection<String> getAllStarredProjectsForUser( String userId ) {
        if (!isValidUser( userId )) {
            return new HashSet<>();
        }

        // public interface should not be able to modify internal HashSet, therefore the copy
        return new LinkedHashSet<>( this.starredProjectsCache.getStarredProjects( userId, this.userToProjectDatabaseTable::selectAllStarredProjectsByUserId ) );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getAllStarringUsersForProject( String projectId ) {
        if (!isValidProject( projectId )) {
            return new HashSet<>();
        }

        // TODO cache for a short time, but it depends on the usage of this feature.

        return this.userToProjectDatabaseTable.selectAllStarringUsersForProject( projectId );
    }

    @Override
    public int getNumberOfStarsForProject( String projectId ) {
        return this.starredProjectCountsCache.getValue( projectId, userToProjectDatabaseTable::getNumberOfStarsForProject );
    }

    @Override
    public void starProject( String userId, String projectId ) {
        if (!isValidUser( userId )) {
            return;
        }

        this.userToProjectDatabaseTable.insertStar( userId, projectId );
        this.starredProjectsCache.addStarredProject( userId, projectId );
        this.starredProjectCountsCache.increase( projectId );
    }

    @Override
    public void unstarProject( String userId, String projectId ) {
        if (!isValidUser( userId )) {
            return;
        }

        this.userToProjectDatabaseTable.deleteStar( userId, projectId );
        this.starredProjectsCache.removeStarredProject( userId, projectId );
        this.starredProjectCountsCache.decrease( projectId );
    }

    @Override
    public boolean isStarred( String userId, String projectId ) {
        if (!isValidUser( userId )) {
            return false;
        }

        if (this.starredProjectsCache.isCached( userId )) {
            return this.starredProjectsCache.isStarred( userId, projectId );
        }

        return getAllStarredProjectsForUser( userId ).contains( projectId );
    }

    private boolean isValidProject( String projectId ) {
        // TODO: implement the test... and use the project repository.
        return true;
    }

    private boolean isValidUser( String userUUID ) {
        return this.applicationServices.getUserRepository().isUserUUIDPresent( userUUID );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void reinitDatabaseTables() {
        this.userToProjectDatabaseTable.createTable();
    }

}
