package de.mindscan.futuresqr.devbackend.userdb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class FSqrLazyUserDatabaseImplTest {

    @Test
    public void testFSqrLazyUserDatabaseImpl_CTorOnly_expectNoExceptions() throws Exception {
        // arrange
        // act
        // assert
        FSqrLazyUserDatabaseImpl userdatabase = new FSqrLazyUserDatabaseImpl();
    }

    @Test
    public void testHasUser_mindscande_expectContainsUser() throws Exception {
        // arrange
        FSqrLazyUserDatabaseImpl userdatabase = new FSqrLazyUserDatabaseImpl();

        // act
        boolean result = userdatabase.hasUser( "mindscan-de" );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testHasUser_foobarde_expectDoesNotContainUser() throws Exception {
        // arrange
        FSqrLazyUserDatabaseImpl userdatabase = new FSqrLazyUserDatabaseImpl();

        // act
        boolean result = userdatabase.hasUser( "foobar-de" );

        // assert
        assertThat( result, equalTo( false ) );
    }

}
