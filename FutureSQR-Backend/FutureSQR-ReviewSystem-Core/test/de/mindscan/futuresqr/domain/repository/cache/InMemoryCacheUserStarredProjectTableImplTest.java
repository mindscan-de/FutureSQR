package de.mindscan.futuresqr.domain.repository.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class InMemoryCacheUserStarredProjectTableImplTest {

    private static final String UUID_1111_1111_1111 = "1111-1111-1111";
    private static final String A_PROJECT_KEY = "AProject";
    private static final String B_PROJECT_KEY = "BProject";

    @Test
    public void testInMemoryCacheUserStarredProjectCacheImpl_createAninstance_expectNoExceptions() throws Exception {
        // arrange
        // act
        // assert
        InMemoryCacheUserStarredProjectTableImpl starredProjects = new InMemoryCacheUserStarredProjectTableImpl();
    }

    @Test
    public void testIsCached_CtorOnlyAskForUncachedUserUUID_returnsFalse() throws Exception {
        // arrange
        InMemoryCacheUserStarredProjectTableImpl starredProjects = new InMemoryCacheUserStarredProjectTableImpl();

        // act
        boolean result = starredProjects.isCached( UUID_1111_1111_1111 );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsCached_SetupKnownUserAndProject_returnsTrue() throws Exception {
        // arrange
        InMemoryCacheUserStarredProjectTableImpl starredProjects = new InMemoryCacheUserStarredProjectTableImpl();
        starredProjects.addStarredProject( UUID_1111_1111_1111, A_PROJECT_KEY );

        // act
        boolean result = starredProjects.isCached( UUID_1111_1111_1111 );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testIsStarred_CtorOnlyAskForUncachedUserUUID_returnFalse() throws Exception {
        // arrange
        InMemoryCacheUserStarredProjectTableImpl starredProjects = new InMemoryCacheUserStarredProjectTableImpl();

        // act
        boolean result = starredProjects.isStarred( UUID_1111_1111_1111, A_PROJECT_KEY );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsStarred_SetupKnownUserForDifferentProject_returnFalse() throws Exception {
        // arrange
        InMemoryCacheUserStarredProjectTableImpl starredProjects = new InMemoryCacheUserStarredProjectTableImpl();
        starredProjects.addStarredProject( UUID_1111_1111_1111, A_PROJECT_KEY );

        // act
        boolean result = starredProjects.isStarred( UUID_1111_1111_1111, B_PROJECT_KEY );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testAddStarred_SetupKnownUserForDifferentProject_returnFalse() throws Exception {
        // arrange
        InMemoryCacheUserStarredProjectTableImpl starredProjects = new InMemoryCacheUserStarredProjectTableImpl();

        // act
        starredProjects.addStarredProject( UUID_1111_1111_1111, A_PROJECT_KEY );

        // assert
        boolean result = starredProjects.isStarred( UUID_1111_1111_1111, B_PROJECT_KEY );
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsStarred_SetupKnownUserAndProjectRequestSameProject_returnTrue() throws Exception {
        // arrange
        InMemoryCacheUserStarredProjectTableImpl starredProjects = new InMemoryCacheUserStarredProjectTableImpl();
        starredProjects.addStarredProject( UUID_1111_1111_1111, A_PROJECT_KEY );

        // act
        boolean result = starredProjects.isStarred( UUID_1111_1111_1111, A_PROJECT_KEY );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testIsStarred_SetupKnownUserAddAndRemoveSameProject_returnFalse() throws Exception {
        // arrange
        InMemoryCacheUserStarredProjectTableImpl starredProjects = new InMemoryCacheUserStarredProjectTableImpl();
        starredProjects.addStarredProject( UUID_1111_1111_1111, A_PROJECT_KEY );
        starredProjects.removeStarredProject( UUID_1111_1111_1111, A_PROJECT_KEY );

        // act
        boolean result = starredProjects.isStarred( UUID_1111_1111_1111, A_PROJECT_KEY );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testRemoveStarredProject_UnCachedUser_thowsNoException() throws Exception {
        // arrange
        InMemoryCacheUserStarredProjectTableImpl starredProjects = new InMemoryCacheUserStarredProjectTableImpl();

        // act
        // assert
        starredProjects.removeStarredProject( UUID_1111_1111_1111, A_PROJECT_KEY );

    }

    @Test
    public void testGetStarredProjects_UncachedUser_returnsEmptyCollection() throws Exception {
        // arrange
        InMemoryCacheUserStarredProjectTableImpl starredProjects = new InMemoryCacheUserStarredProjectTableImpl();

        // act
        Set<String> result = starredProjects.getStarredProjects( UUID_1111_1111_1111 );

        // assert
        assertThat( result, empty() );
    }

    @Test
    public void testAddStarredProject_AddStarredProjectForUser_retrnsNonEmptyCollectionForSameUser() throws Exception {
        // arrange
        InMemoryCacheUserStarredProjectTableImpl starredProjects = new InMemoryCacheUserStarredProjectTableImpl();

        // act
        starredProjects.addStarredProject( UUID_1111_1111_1111, A_PROJECT_KEY );

        // assert
        Set<String> result = starredProjects.getStarredProjects( UUID_1111_1111_1111 );
        assertThat( result, not( empty() ) );
    }

    @Test
    public void testGetStarredProjects_AddStarredProjectForUser_retrnsNonEmptyCollectionForSameUser() throws Exception {
        // arrange
        InMemoryCacheUserStarredProjectTableImpl starredProjects = new InMemoryCacheUserStarredProjectTableImpl();
        starredProjects.addStarredProject( UUID_1111_1111_1111, A_PROJECT_KEY );

        // act
        Set<String> result = starredProjects.getStarredProjects( UUID_1111_1111_1111 );

        // assert
        assertThat( result, contains( A_PROJECT_KEY ) );
    }

}
