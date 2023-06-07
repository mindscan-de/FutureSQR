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
package de.mindscan.futuresqr.domain.databases.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.mindscan.futuresqr.domain.databases.FSqrUserTable;
import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;

/**
 * This is a hard-coded user database, which we want to transform into a real user persistence, but we also
 * want to explore how to separate these two concerns correctly and how the update logic should be handled
 * by using the user database /  user repository first.
 */
public class FSqrUserDatabaseTableImpl implements FSqrUserTable {

    private Map<String, FSqrSystemUser> tmpUserDatabaseTable;

    /**
     * 
     */
    public FSqrUserDatabaseTableImpl() {
        this.tmpUserDatabaseTable = new HashMap<>();

        // TODO: remove me, when we have a database and a database session object.
        initHardcodedData();
    }

    protected void initHardcodedData() {
        insertUser( new FSqrSystemUser( "35c94b55-559f-30e4-a2f4-ee16d31fc276", "rbreunung", "Robert Breunung", "rb@localhost", false,
                        "/FutureSQR/assets/avatars/35c94b55-559f-30e4-a2f4-ee16d31fc276.256px.jpg" ) );

        insertUser( new FSqrSystemUser( "6822a80d-1854-304c-a26d-81acd2c008f3", "mindscan-banned", "Maxim Gansert Banned Testuser", "mindscan@local.localhost",
                        true, "" ) );

        insertUser( new FSqrSystemUser( "8ce74ee9-48ff-3dde-b678-58a632887e31", "mindscan-de", "Maxim Gansert", "contact@themail.local", false,
                        "/FutureSQR/assets/avatars/8ce74ee9-48ff-3dde-b678-58a632887e31.256px.jpg" ) );

        insertUser( new FSqrSystemUser( "f5fc8449-3049-3498-9f6b-ce828515bba2", "someoneelsa", "Elsa Someone", "contact@elsamail.local", false,
                        "/FutureSQR/assets/avatars/f5fc8449-3049-3498-9f6b-ce828515bba2.256px.jpg" ) );
    }

    public void insertUser( FSqrSystemUser user ) {
        tmpUserDatabaseTable.put( user.getUserUUID(), user );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrSystemUser selectUserByUUID( String uuid ) {
        return tmpUserDatabaseTable.get( uuid );
    }

    @Override
    public FSqrSystemUser selectUserByLoginName( String loginName ) {
        if (loginName == null) {
            return null;
        }

        List<FSqrSystemUser> collected = tmpUserDatabaseTable.values().stream().filter( u -> loginName.equals( u.getUserLoginName() ) )
                        .collect( Collectors.toList() );

        if (collected.isEmpty()) {
            return null;
        }

        return collected.get( 0 );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isLoginNamePresent( String logonName ) {
        return !(tmpUserDatabaseTable.values().stream().noneMatch( u -> logonName.equals( u.getUserLoginName() ) ));
    }

}
