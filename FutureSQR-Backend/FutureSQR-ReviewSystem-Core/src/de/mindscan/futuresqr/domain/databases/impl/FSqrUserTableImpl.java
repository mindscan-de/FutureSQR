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
import de.mindscan.futuresqr.domain.databases.FSqrUserTable;
import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;

/**
 * This is a hard-coded user database, which we want to transform into a real user persistence, but we also
 * want to explore how to separate these two concerns correctly and how the update logic should be handled
 * by using the user database /  user repository first.
 */
public class FSqrUserTableImpl implements FSqrUserTable {

    private static final boolean INITMODE_ENABLED = true;

    // table name
    private static final String SYSTEM_USERS_TABLENAME = "SystemUsers";

    // columns
    private static final String UUID_PK_COLUMN = "uuid";
    private static final String LOGINNAME_COLUMN = "userLoginName";
    private static final String DISPLAYNAME_COLUMN = "userDisplayName";
    private static final String EMAIL_COLUMN = "userEmail";
    private static final String AVATAR_LOCATION_COLUMN = "avatarLocation";
    private static final String ISBANNED_COLUMN = "isBanned";
    // TODO: CREATED DATE_COLUMN
    // TODO: MODIFIED DATE COLUMN
    // TODO: BANNED DATE COLUMN

    // sql-statements
    private static final String CREATE_TABLE_SYSTEM_USERS = //
                    "CREATE TABLE " + SYSTEM_USERS_TABLENAME + // 
                                    " ( " + UUID_PK_COLUMN + //
                                    ", " + LOGINNAME_COLUMN + //
                                    ", " + DISPLAYNAME_COLUMN + //
                                    ", " + EMAIL_COLUMN + //
                                    ", " + AVATAR_LOCATION_COLUMN + //
                                    ", " + ISBANNED_COLUMN + ");";

    private static final String DROP_TABLE_IF_EXISTS = // 
                    "DROP TABLE IF EXISTS " + SYSTEM_USERS_TABLENAME + ";";

    private static final String INSERT_SYSTEM_USER_PS = //
                    "INSERT INTO " + SYSTEM_USERS_TABLENAME + //
                                    " ( " + UUID_PK_COLUMN + //
                                    ", " + LOGINNAME_COLUMN + //
                                    ", " + DISPLAYNAME_COLUMN + //
                                    ", " + EMAIL_COLUMN + //
                                    ", " + AVATAR_LOCATION_COLUMN + //
                                    ", " + ISBANNED_COLUMN + " ) VALUES ( ?1,?2,?3,?4,?5,?6 )";

    private static final String UPDATE_SYSTEM_USER_PS = //
                    "UPDATE " + SYSTEM_USERS_TABLENAME + //
                                    " SET " + DISPLAYNAME_COLUMN + "=?2 " + //
                                    ", " + EMAIL_COLUMN + "=?3 " + //
                                    ", " + AVATAR_LOCATION_COLUMN + "=?4" + //
                                    ", " + ISBANNED_COLUMN + " =?5 " + //
                                    " WHERE " + UUID_PK_COLUMN + "=?1;";

    private static final String SELECT_SYSTEM_USER_BY_UUID_PS = //
                    "SELECT * FROM " + SYSTEM_USERS_TABLENAME + " WHERE " + UUID_PK_COLUMN + "=?1;";

    private static final String SELECT_SYSTEM_USER_BY_LOGINNAME_PS = //
                    "SELECT * FROM " + SYSTEM_USERS_TABLENAME + " WHERE " + LOGINNAME_COLUMN + "=?1;";

    private static final String SELECT_ALL_USERS_PS = //
                    // TODO ORDER BY CREATED DATE.
                    "SELECT * FROM " + SYSTEM_USERS_TABLENAME + "; ";

    private FSqrDatabaseConnection connection;

    private static final FSqrSystemUser MINDSCAN_DE = new FSqrSystemUser( "8ce74ee9-48ff-3dde-b678-58a632887e31", "mindscan-de", "Maxim Gansert",
                    "contact@themail.local", false, "/FutureSQR/assets/avatars/8ce74ee9-48ff-3dde-b678-58a632887e31.256px.jpg" );

    /**
     * 
     */
    public FSqrUserTableImpl() {
        // intentionally left blank
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setDatbaseConnection( FSqrDatabaseConnection connection ) {
        this.connection = connection;
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

    /** 
     * {@inheritDoc}
     */
    @Override
    public void insertUser( FSqrSystemUser user ) {
        try (PreparedStatement insertPS = this.connection.createPreparedStatement( INSERT_SYSTEM_USER_PS )) {
            insertPS.setString( 1, user.getUserUUID() );
            insertPS.setString( 2, user.getUserLoginName() );
            insertPS.setString( 3, user.getUserDisplayName() );
            insertPS.setString( 4, user.getUserEmail() );
            insertPS.setString( 5, user.getAvatarLocation() );
            insertPS.setBoolean( 6, user.isBanned() );

            insertPS.addBatch();
            insertPS.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void updateUser( FSqrSystemUser user ) {
        try (PreparedStatement updatePS = this.connection.createPreparedStatement( UPDATE_SYSTEM_USER_PS )) {
            updatePS.setString( 1, user.getUserUUID() );
            updatePS.setString( 2, user.getUserDisplayName() );
            updatePS.setString( 3, user.getUserEmail() );
            updatePS.setString( 4, user.getAvatarLocation() );
            updatePS.setBoolean( 5, user.isBanned() );

            updatePS.addBatch();
            updatePS.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrSystemUser selectUserByUUID( String uuid ) {
        try (PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_SYSTEM_USER_BY_UUID_PS )) {
            selectPS.setString( 1, uuid );
            try (ResultSet resultSet = selectPS.executeQuery()) {
                if (resultSet.next()) {
                    return toSystemUser( resultSet );
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (INITMODE_ENABLED && uuid.equals( MINDSCAN_DE.getUserUUID() )) {
            return MINDSCAN_DE;
        }

        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrSystemUser selectUserByLoginName( String loginName ) {
        if (loginName == null) {
            return null;
        }

        try (PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_SYSTEM_USER_BY_LOGINNAME_PS )) {
            selectPS.setString( 1, loginName );
            try (ResultSet resultSet = selectPS.executeQuery()) {
                if (resultSet.next()) {
                    return toSystemUser( resultSet );
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (INITMODE_ENABLED && loginName.equals( MINDSCAN_DE.getUserLoginName() )) {
            return MINDSCAN_DE;
        }

        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Collection<FSqrSystemUser> selectAllUsers() {
        List<FSqrSystemUser> result = new ArrayList<>();

        try (PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_ALL_USERS_PS )) {
            try (ResultSet resultSet = selectPS.executeQuery()) {
                while (resultSet.next()) {
                    result.add( toSystemUser( resultSet ) );
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (INITMODE_ENABLED && result.isEmpty()) {
            result.add( MINDSCAN_DE );
            return result;
        }

        return result;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isLoginNamePresent( String logonName ) {
        if (logonName.equals( MINDSCAN_DE.getUserLoginName() )) {
            return true;
        }

        return selectUserByLoginName( logonName ) != null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void createTable() {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate( DROP_TABLE_IF_EXISTS );
            statement.executeUpdate( CREATE_TABLE_SYSTEM_USERS );
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
        // TODO Auto-generated method stub
    }

    private FSqrSystemUser toSystemUser( ResultSet resultSet ) throws Exception {
        String uuid = resultSet.getString( UUID_PK_COLUMN );
        String loginName = resultSet.getString( LOGINNAME_COLUMN );
        String displayname = resultSet.getString( DISPLAYNAME_COLUMN );
        String email = resultSet.getString( EMAIL_COLUMN );
        String avatarlocation = resultSet.getString( AVATAR_LOCATION_COLUMN );
        boolean isBanned = resultSet.getBoolean( ISBANNED_COLUMN );

        return new FSqrSystemUser( uuid, loginName, displayname, email, isBanned, avatarlocation );
    }
}
