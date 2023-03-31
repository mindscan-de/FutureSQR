package de.mindscan.futuresqr.domain.repository.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;

public class InMemoryCacheSystemUserTableImplTest {

    private static final String TESTACCOUNT_LOGONNAME = "testaccount";
    private static final String TESTACCOUNT_LOGONNAME2 = "testaccount2";
    private static final String USERID_11111_11111_11111 = "11111-11111-11111";
    private static final String USERID_22222_11111_11111 = "22222-11111-11111";

    @Test
    public void testInMemoryCacheSystemUserTableImpl() throws Exception {
        // arrange
        // act
        // assert
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();
    }

    @Test
    public void testIsCached_OnlyCtorRequestUnknownUser_returnsFalse() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();

        // act
        boolean result = userTable.isCached( USERID_11111_11111_11111 );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsLoginNamePresent_OnlyCtorRequestUnknownUser_returnsFalse() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();

        // act
        boolean result = userTable.isLoginNamePresent( TESTACCOUNT_LOGONNAME );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testGetSystemUserString_OnlyCtorRequestUnknownUser_returnsNull() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();

        // act
        FSqrSystemUser result = userTable.getSystemUser( USERID_11111_11111_11111 );

        // assert
        assertThat( result, nullValue() );
    }

    @Test
    public void testPutSystemUser_PutUserGetUser_returnsSameUser() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();
        FSqrSystemUser expectedSystemUser = new FSqrSystemUser( USERID_11111_11111_11111, TESTACCOUNT_LOGONNAME, "", "a@b.cc.local" );

        // act
        userTable.putSystemUser( USERID_11111_11111_11111, expectedSystemUser );

        // assert
        FSqrSystemUser result = userTable.getSystemUser( USERID_11111_11111_11111 );
        assertThat( result, is( sameInstance( expectedSystemUser ) ) );
    }

    @Test
    public void testGetSystemUser_SetupUserGetUser_returnsSameUser() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();
        FSqrSystemUser expectedSystemUser = new FSqrSystemUser( USERID_11111_11111_11111, TESTACCOUNT_LOGONNAME, "", "a@b.cc.local" );
        userTable.putSystemUser( USERID_11111_11111_11111, expectedSystemUser );

        // act
        FSqrSystemUser result = userTable.getSystemUser( USERID_11111_11111_11111 );

        // assert
        assertThat( result, is( sameInstance( expectedSystemUser ) ) );
    }

    @Test
    public void testIsCached_SetupUser_returnsTrue() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();
        FSqrSystemUser expectedSystemUser = new FSqrSystemUser( USERID_11111_11111_11111, TESTACCOUNT_LOGONNAME, "", "a@b.cc.local" );
        userTable.putSystemUser( USERID_11111_11111_11111, expectedSystemUser );

        // act
        boolean result = userTable.isCached( USERID_11111_11111_11111 );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testIsLoginNamePresent_SetupUser_returnsTrue() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();
        FSqrSystemUser expectedSystemUser = new FSqrSystemUser( USERID_11111_11111_11111, TESTACCOUNT_LOGONNAME, "", "a@b.cc.local" );
        userTable.putSystemUser( USERID_11111_11111_11111, expectedSystemUser );

        // act
        boolean result = userTable.isLoginNamePresent( TESTACCOUNT_LOGONNAME );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testGetSystemUser_CtorOnlyUnknownUserAndLoaderFunctionIsNull_returnsNull() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();

        // act
        FSqrSystemUser result = userTable.getSystemUser( USERID_11111_11111_11111, null );

        // assert
        assertThat( result, nullValue() );
    }

    @Test
    public void testGetSystemUser_CtorOnlyUnknownUserAndLoaderFunctionReturnsNull_returnsNull() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();

        // act
        FSqrSystemUser result = userTable.getSystemUser( USERID_11111_11111_11111, ( uid ) -> null );

        // assert
        assertThat( result, nullValue() );
    }

    @Test
    public void testGetSystemUser_CtorOnlyUnknownUserAndLoaderFunctionReturnsUser_returnsSameUser() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();
        FSqrSystemUser expectedSystemUser = new FSqrSystemUser( USERID_11111_11111_11111, TESTACCOUNT_LOGONNAME, "", "a@b.cc.local" );

        // act
        FSqrSystemUser result = userTable.getSystemUser( USERID_11111_11111_11111, ( uid ) -> expectedSystemUser );

        // assert
        assertThat( result, is( sameInstance( expectedSystemUser ) ) );
    }

    @Test
    public void testGetSystemUser_SetupUser_returnsExpecteduser() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();
        FSqrSystemUser expectedSystemUser = new FSqrSystemUser( USERID_11111_11111_11111, TESTACCOUNT_LOGONNAME, "", "a@b.cc.local" );
        FSqrSystemUser nonExpectedSystemUser = new FSqrSystemUser( USERID_22222_11111_11111, TESTACCOUNT_LOGONNAME2, "", "a2@b.cc.local" );
        userTable.putSystemUser( USERID_11111_11111_11111, expectedSystemUser );

        // act
        FSqrSystemUser result = userTable.getSystemUser( USERID_11111_11111_11111, ( uid ) -> nonExpectedSystemUser );

        // assert
        assertThat( result, is( sameInstance( expectedSystemUser ) ) );
    }

    @Test
    public void testIsLoginNamePresent_SetupUserUsingLoaderFunction_returnsTrue() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();
        FSqrSystemUser expectedSystemUser = new FSqrSystemUser( USERID_11111_11111_11111, TESTACCOUNT_LOGONNAME, "", "a@b.cc.local" );
        userTable.getSystemUser( USERID_11111_11111_11111, ( uid ) -> expectedSystemUser );

        // act
        boolean result = userTable.isLoginNamePresent( TESTACCOUNT_LOGONNAME );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testIsCached_SetupUserUsingLoaderFunction_returnsTrue() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();
        FSqrSystemUser expectedSystemUser = new FSqrSystemUser( USERID_11111_11111_11111, TESTACCOUNT_LOGONNAME, "", "a@b.cc.local" );
        userTable.getSystemUser( USERID_11111_11111_11111, ( uid ) -> expectedSystemUser );

        // act
        boolean result = userTable.isCached( USERID_11111_11111_11111 );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testGetSystemUser_SetupUserUsingLoaderFunction_returnsSameUser() throws Exception {
        // arrange
        InMemoryCacheSystemUserTableImpl userTable = new InMemoryCacheSystemUserTableImpl();
        FSqrSystemUser expectedSystemUser = new FSqrSystemUser( USERID_11111_11111_11111, TESTACCOUNT_LOGONNAME, "", "a@b.cc.local" );
        userTable.getSystemUser( USERID_11111_11111_11111, ( uid ) -> expectedSystemUser );

        // act
        FSqrSystemUser result = userTable.getSystemUser( USERID_11111_11111_11111 );

        // assert
        assertThat( result, is( sameInstance( expectedSystemUser ) ) );
    }

}
