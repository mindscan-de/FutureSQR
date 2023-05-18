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

import java.util.Collection;
import java.util.HashSet;

import de.mindscan.futuresqr.domain.application.ApplicationServicesSetter;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.repository.FSqrUserToProjectRepository;
import de.mindscan.futuresqr.domain.repository.cache.InMemoryCacheUserStarredProjectTableImpl;

/**
 * Idea here is to collect methods and data and such related to user to project relations. 
 * This may be refactored any time.
 * 
 * Also we might also provide methods, to calculate which project a user has access to,
 * because this is related to the user to project relationship as well but a different
 * attribute.
 * 
 * TODO: rework the repository to use a database instead of the in-memory + scm data pull implementation
 */
public class FSqrUserToProjectRepositoryImpl implements FSqrUserToProjectRepository, ApplicationServicesSetter {

    private FSqrApplicationServices applicationServices;

    private InMemoryCacheUserStarredProjectTableImpl starredProjectsCache;

    /**
     * 
     */
    public FSqrUserToProjectRepositoryImpl() {
        this.starredProjectsCache = new InMemoryCacheUserStarredProjectTableImpl();
        this.applicationServices = new FSqrApplicationServicesUnitialized();
    }

    @Override
    public void setApplicationServices( FSqrApplicationServices applicationServices ) {
        this.applicationServices = applicationServices;
    }

    // public interface should not be able to modify internal HashSet.
    @Override
    public Collection<String> getAllStarredProjectsForUser( String userId ) {
        // MUST check if userId exists, otherwise a denial of service is possible
        if (!isValidUser( userId )) {
            return new HashSet<>();
        }

        if (this.starredProjectsCache.isCached( userId )) {
            return new HashSet<>( this.starredProjectsCache.getStarredProjects( userId ) );
        }

        return new HashSet<>();
    }

    @Override
    public void starProject( String userId, String projectId ) {
        // MUST check if userId exists, otherwise a denial of service is possible
        if (!isValidUser( userId )) {
            return;
        }

        this.starredProjectsCache.addStarredProject( userId, projectId );
    }

    @Override
    public void unstarProject( String userId, String projectId ) {
        // MUST check if userId exists, otherwise a denial of service is possible
        if (!isValidUser( userId )) {
            return;
        }

        this.starredProjectsCache.removeStarredProject( userId, projectId );
    }

    @Override
    public boolean isStarred( String userId, String projectId ) {
        // MUST check if userId exists, otherwise a denial of service is possible
        if (!isValidUser( userId )) {
            return false;
        }

        return this.starredProjectsCache.isStarred( userId, projectId );
    }

    // TODO: maybe we also want the reverse table, where we look at the project, and want to know who gave a star to this project

    private boolean isValidUser( String userUUID ) {
        return this.applicationServices.getUserRepository().isUserUUIDPresent( userUUID );
    }

}
