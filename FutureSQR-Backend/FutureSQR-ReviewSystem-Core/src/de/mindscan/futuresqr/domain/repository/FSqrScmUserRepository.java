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
package de.mindscan.futuresqr.domain.repository;

import java.util.Collection;

import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;

/**
 * 
 */
public interface FSqrScmUserRepository extends FSqrRepository, FSqrDatabaseBackedRepository {

    boolean isLogonNamePresent( String logonName );

    boolean isUserUUIDPresent( String uuid );

    void addUserEntry( FSqrSystemUser user );

    void addUserHandle( String scmAlias, String authorUUID );

    String getUserUUID( String scmAlias );

    FSqrSystemUser getUserByUUID( String uuid );

    FSqrSystemUser getUserByLogonName( String username );

    Collection<FSqrSystemUser> getAllUsers();

    FSqrSystemUser banUser( String uuid );

    FSqrSystemUser unbanUser( String uuid );

}
