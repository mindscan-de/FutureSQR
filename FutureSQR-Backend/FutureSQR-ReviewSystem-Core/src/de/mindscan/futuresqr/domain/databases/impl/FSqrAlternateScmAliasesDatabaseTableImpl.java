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
import java.util.Map;

import de.mindscan.futuresqr.domain.databases.FSqrAlternateScmAliasesDatabaseTable;

/**
 * This is currently a fake implementation of the SCM Aliases Database table, where each user can have different
 * SCM Aliases basically in case of different accounts on different SCM Repositories.
 * 
 *  This is half way through to implement an access for a real database table.
 */
public class FSqrAlternateScmAliasesDatabaseTableImpl implements FSqrAlternateScmAliasesDatabaseTable {

    private Map<String, String> tmpScmToUUIDMap;

    /**
     * 
     */
    public FSqrAlternateScmAliasesDatabaseTableImpl() {
        this.tmpScmToUUIDMap = new HashMap<>();

        // TODO: remove me, when we have a database and a database session object.
        initHardcodedData();
    }

    /**
     * 
     */
    protected void initHardcodedData() {
        insertUserAlias( "mindscan-de", "8ce74ee9-48ff-3dde-b678-58a632887e31" );
        insertUserAlias( "Maxim Gansert", "8ce74ee9-48ff-3dde-b678-58a632887e31" );
        insertUserAlias( "someoneelsa", "f5fc8449-3049-3498-9f6b-ce828515bba2" );
        insertUserAlias( "mindscan-banned", "6822a80d-1854-304c-a26d-81acd2c008f3" );
        insertUserAlias( "rbreunung", "35c94b55-559f-30e4-a2f4-ee16d31fc276" );
        insertUserAlias( "Robert Breunung", "35c94b55-559f-30e4-a2f4-ee16d31fc276" );
    }

    @Override
    public void insertUserAlias( String aliasName, String userUuid ) {
        this.tmpScmToUUIDMap.put( aliasName, userUuid );
    }

    @Override
    public String getUuidForScmAlias( String scmAlias ) {
        return tmpScmToUUIDMap.getOrDefault( scmAlias, scmAlias );
    }

}
