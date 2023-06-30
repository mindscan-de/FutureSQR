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

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;

/**
 * 
 */
public class SqliteDatabaseTable {

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
            Collection<String> createdColumnDescriptions = new ArrayList<>();
            tableColumns.stream().map( this::buildCreateColunm ).forEach( createdColumnDescriptions::add );

            sqlBuilder.append( " ( " );
            sqlBuilder.append( String.join( ", ", createdColumnDescriptions ) );
            sqlBuilder.append( " ) " );
        }

        sqlBuilder.append( ";" );

        try (Statement statement = dbConnection.createStatement()) {
            String query = sqlBuilder.toString();
            statement.execute( query );
            System.out.println( query );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildCreateColunm( SqliteDatabaseTableColumn column ) {
        StringBuilder columnBuilder = new StringBuilder();

        // COLUMNNAME
        columnBuilder.append( column.getColumnName() );
        columnBuilder.append( " " );

        // TYPE - INT, TEXT, DATE
        columnBuilder.append( column.getColumnType().getDbTypeAsString() );
        columnBuilder.append( " " );

        // (NOT NULL)
        if (column.isNotNull()) {
            columnBuilder.append( "NOT NULL " );
        }

        // (PRIMARY KEY)
        if (column.isPrimaryKey()) {
            columnBuilder.append( "PRIMARY KEY " );
        }

        // (AUTOINCREMENT)
        if (column.isAutoincrement()) {
            columnBuilder.append( "AUTOINCREMENT" );
        }

        return columnBuilder.toString();
    }

    public void dropTable( FSqrDatabaseConnection connection ) {
        String query = "DROP TABLE IF EXISTS " + this.tableName + ";";

        try (Statement statement = connection.createStatement()) {
            statement.execute( query );
            System.out.println( query );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
