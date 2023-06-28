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
package de.mindscan.futuresqr.domain.databases.type;

import java.sql.PreparedStatement;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;

/**
 * 
 */
public class SqliteDatabaseTable {

    private String tableName;

    /**
     * 
     */
    public SqliteDatabaseTable( String tableName ) {
        this.tableName = tableName;

        // TODO: we also need the database connection to execute the create and drop command, so the database connection should be part of the constructor.  
    }

    public String tableName() {
        return tableName;
    }

    // TODO: addColumn
    // TODO: addColumns

    // TODO: use the columns 
    public void createTable() {

    }

    public void dropTable( FSqrDatabaseConnection connection ) {
        String query = "DROP TABLE IF EXISTS ?1 ;";

        try (PreparedStatement statementPS = connection.createPreparedStatement( query )) {
            statementPS.setString( 1, this.tableName );

            statementPS.addBatch();
            statementPS.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
