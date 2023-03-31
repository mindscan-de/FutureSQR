package de.mindscan.futuresqr.domain.repository.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;

public class InMemoryCacheSystemUserTableImplTest {

    private static final String USERID_11111_11111_11111 = "11111-11111-11111";

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
        boolean result = userTable.isLoginNamePresent( "testaccount" );

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

}
