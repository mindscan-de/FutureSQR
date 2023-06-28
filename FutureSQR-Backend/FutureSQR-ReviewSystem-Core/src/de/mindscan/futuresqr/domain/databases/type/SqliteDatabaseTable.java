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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;

/**
 * 
 */
public class SqliteDatabaseTable {

    private static final String DROP_TABLE_IF_EXISTS_PS = "DROP TABLE IF EXISTS ?1 ;";

    private String tableName;

    private List<SqliteDatabaseTableColumn> tableColumns;

    /**
     * 
     */
    public SqliteDatabaseTable( /*SqliteDatabase database,*/ String tableName ) {
        this.tableName = tableName;
        this.tableColumns = new ArrayList<>();

        // TODO: we also need the database connection to execute the create and drop command, so the database connection should be part of the constructor.
        // database.addTableToDatabase(this);
    }

    public String tableName() {
        return tableName;
    }

    // TODO: addColumn
    // TODO: addColumns

    /**
     * @param sqliteDatabaseTableColumn
     */
    public void registerColumn( SqliteDatabaseTableColumn sqliteDatabaseTableColumn ) {
        // TODO: check we don't add duplicates?
        this.tableColumns.add( sqliteDatabaseTableColumn );
    }

    /**
     * @return
     */
    private Collection<SqliteDatabaseTableColumn> getColumns() {
        return new ArrayList<>( this.tableColumns );
    }

    public void createTable( FSqrDatabaseConnection dbConnection ) {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append( "CREATE TABLE " );
        sqlBuilder.append( this.tableName );

        Collection<SqliteDatabaseTableColumn> tableColumns = getColumns();
        if (!tableColumns.isEmpty()) {
            sqlBuilder.append( " ( " );

            // TODO: convert each column into a create-compatible description
            // TODO conversion here.
            Collection<String> createdColumnDescriptions = new ArrayList<>();

            sqlBuilder.append( String.join( ", ", createdColumnDescriptions ) );
            sqlBuilder.append( " ) " );
        }

        sqlBuilder.append( ";" );

        try (Statement statement = dbConnection.createStatement()) {
            statement.execute( sqlBuilder.toString() );
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dropTable( FSqrDatabaseConnection connection ) {
        String query = DROP_TABLE_IF_EXISTS_PS;

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
