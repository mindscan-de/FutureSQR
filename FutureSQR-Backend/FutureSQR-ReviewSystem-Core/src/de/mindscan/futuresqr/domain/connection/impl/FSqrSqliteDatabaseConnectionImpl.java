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
package de.mindscan.futuresqr.domain.connection.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import de.mindscan.futuresqr.domain.application.ApplicationServicesSetter;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;

/**
 * 
 */
public class FSqrSqliteDatabaseConnectionImpl implements FSqrDatabaseConnection, ApplicationServicesSetter {

    private String connectionString;

    private Connection sqliteConnection;

    /**
     * 
     */
    public FSqrSqliteDatabaseConnectionImpl() {
        // intentionally left blank
    }

    @Override
    public void setApplicationServices( FSqrApplicationServices services ) {
        // TODO: this is where we can read the system configuration, to retrieve the database connection 
        //       information and when we can boot from here.
        // actually the boot should not be done here, but in a later stage, but for now this should be sufficient.

        connectionString = services.getSystemConfiguration().getDatabaseConnectionString();

        // TODO: this here should be part of a later lifecycle, anyhow - this is good enough right now.
        sqliteConnection = null;
        try {
            Class.forName( "org.sqlite.JDBC" );
            sqliteConnection = DriverManager.getConnection( connectionString );

        }
        catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return;
        }
        System.out.println( "XXXX: DB opened and closed successfully." );
    }

    public void shutdown() {
        try {
            if (sqliteConnection != null && !(sqliteConnection.isClosed())) {
                sqliteConnection.close();
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement createPreparedStatement( String statement ) throws SQLException {
        return sqliteConnection.prepareStatement( statement );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Statement createStatement( String statement ) {

        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Statement createStatement() throws SQLException {
        return sqliteConnection.createStatement();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void finishTransaction() {
        try {
            sqliteConnection.setAutoCommit( false );
            sqliteConnection.commit();
            sqliteConnection.setAutoCommit( true );
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
