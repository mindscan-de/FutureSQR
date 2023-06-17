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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.FSqrAlternateScmAliasesDatabaseTable;

/**
 * Implementation of the SCM aliases table. each user can have different
 * SCM Aliases basically in case of different accounts on different SCM Repositories.
 */
public class FSqrAlternateScmAliasesDatabaseTableImpl implements FSqrAlternateScmAliasesDatabaseTable {

    // TableName
    private static final String SCM_ALIASES_TABLENAME = "ScmUserAliases";

    // Column Names
    private static final String COLUMN_USER_UUID = "userUuid";
    private static final String COLUMN_USER_ALIASNAME = "aliasName";

    // SQL-Statements
    private static final String DROP_TABLE_IF_EXISTS = //
                    "DROP TABLE IF EXISTS " + SCM_ALIASES_TABLENAME + ";";

    private static final String CREATE_TABLE_SCM_ALIASES = //
                    "CREATE TABLE " + SCM_ALIASES_TABLENAME + //
                                    " (" + COLUMN_USER_UUID + //
                                    ", " + COLUMN_USER_ALIASNAME + "); ";

    private static final String INSERT_SCM_ALIASNAME_PS = //
                    "INSERT INTO " + SCM_ALIASES_TABLENAME + //
                                    " (" + COLUMN_USER_UUID + //
                                    ", " + COLUMN_USER_ALIASNAME + " ) VALUES (?1, ?2);";

    private static final String REMOVE_SCM_ALIASNAME_PS = //
                    "DELETE FROM " + SCM_ALIASES_TABLENAME + //
                                    " WHERE ( " + COLUMN_USER_UUID + "=?1 AND " + COLUMN_USER_ALIASNAME + "=?2);";

    private static final String SELECT_UUID_FOR_SCMALIAS = //
                    "SELECT " + COLUMN_USER_UUID + " FROM " + SCM_ALIASES_TABLENAME + //
                                    " WHERE " + COLUMN_USER_ALIASNAME + "=?1;";

    private static final String SELECT_ALIASES_FOR_USER = //
                    "SELECT " + COLUMN_USER_ALIASNAME + " FROM " + SCM_ALIASES_TABLENAME + //
                                    " WHERE " + COLUMN_USER_UUID + "=?1 " + //
                                    " ORDER BY " + COLUMN_USER_ALIASNAME + ";";

    private FSqrDatabaseConnection connection;

    /**
     * 
     */
    public FSqrAlternateScmAliasesDatabaseTableImpl() {
        // intentionally left blank
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setDatbaseConnection( FSqrDatabaseConnection connection ) {
        this.connection = connection;
    }

    @Override
    public void insertUserAlias( String aliasName, String userUuid ) {
        try {
            PreparedStatement insertAliasPS = this.connection.createPreparedStatement( INSERT_SCM_ALIASNAME_PS );
            insertAliasPS.setString( 1, userUuid );
            insertAliasPS.setString( 2, aliasName );

            insertAliasPS.addBatch();
            insertAliasPS.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void removeUserAlias( String aliasName, String userUuid ) {
        try {
            PreparedStatement removeAliasPS = this.connection.createPreparedStatement( REMOVE_SCM_ALIASNAME_PS );
            removeAliasPS.setString( 1, userUuid );
            removeAliasPS.setString( 2, aliasName );

            removeAliasPS.addBatch();
            removeAliasPS.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUuidForScmAlias( String scmAlias ) {
        String result = scmAlias;

        try {
            PreparedStatement selectUuidPS = this.connection.createPreparedStatement( SELECT_UUID_FOR_SCMALIAS );

            selectUuidPS.setString( 1, scmAlias );

            ResultSet resultSet = selectUuidPS.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString( COLUMN_USER_UUID );
            }
            resultSet.close();
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getAllScmAliasesForUserUuid( String userUuid ) {
        List<String> result = new ArrayList<>();

        try {
            PreparedStatement selectAllAliasesPS = this.connection.createPreparedStatement( SELECT_ALIASES_FOR_USER );

            selectAllAliasesPS.setString( 1, userUuid );

            ResultSet resultSet = selectAllAliasesPS.executeQuery();
            while (resultSet.next()) {
                result.add( resultSet.getString( COLUMN_USER_ALIASNAME ) );
            }
            resultSet.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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

    /** 
     * {@inheritDoc}
     */
    @Override
    public void createTable() {
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate( DROP_TABLE_IF_EXISTS );
            statement.executeUpdate( CREATE_TABLE_SCM_ALIASES );
            initHardcodedData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        // intentionally left blank
    }

}
