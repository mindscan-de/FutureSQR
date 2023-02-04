package de.mindscan.futuresqr.domain.databases;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class FSqrUserToProjectRepositoryImplTest {

    private static final String KNOWN_USER_ID = "KNOWN_USER_ID";
    private static final String ANOTHER_USER_ID = "ANOTHER_USER_ID";

    private static final String PROJECT_1 = "PROJECT_1";
    private static final String PROJECT_2 = "PROJECT_2";

    @Test
    public void testGetAllStarredProjectsForUser_Ctor_CollectionIsEmpty() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = new FSqrUserToProjectRepositoryImpl();

        // act
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_ID );

        // assert
        assertThat( result, empty() );
    }

    @Test
    public void testStarProject_CtorThenStarKnownProject_CollectionIsNotEmpty() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = new FSqrUserToProjectRepositoryImpl();

        // act
        repository.starProject( KNOWN_USER_ID, PROJECT_1 );

        // assert
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_ID );
        assertThat( result, not( empty() ) );
    }

    @Test
    public void testGetAllStarredProjectsForUser_CtorStarForKnownUserRequestForAnotherUser_CollectionIsEmpty() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = new FSqrUserToProjectRepositoryImpl();
        repository.starProject( KNOWN_USER_ID, PROJECT_1 );

        // act
        Set<String> result = repository.getAllStarredProjectsForUser( ANOTHER_USER_ID );

        // assert
        assertThat( result, empty() );
    }

    @Test
    public void testStarProject_CtorThenStarProject1_CollectionContainsProject1() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = new FSqrUserToProjectRepositoryImpl();

        // act
        repository.starProject( KNOWN_USER_ID, PROJECT_1 );

        // assert
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_ID );
        assertThat( result, contains( PROJECT_1 ) );
    }

    @Test
    public void testStarProject_CtorThenStarProject2_CollectionContainsProject2() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = new FSqrUserToProjectRepositoryImpl();

        // act
        repository.starProject( KNOWN_USER_ID, PROJECT_2 );

        // assert
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_ID );
        assertThat( result, contains( PROJECT_2 ) );
    }

    @Test
    public void testStarProject_CtorThenStarProject1and2_CollectionContainsBothProjects() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = new FSqrUserToProjectRepositoryImpl();

        // act
        repository.starProject( KNOWN_USER_ID, PROJECT_1 );
        repository.starProject( KNOWN_USER_ID, PROJECT_2 );

        // assert
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_ID );
        assertThat( result, containsInAnyOrder( PROJECT_1, PROJECT_2 ) );
    }

    @Test
    public void testUnstarProject_CtorStarTwoProjects1and2UnstarProject2_CollectionContainsProject1() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = new FSqrUserToProjectRepositoryImpl();
        repository.starProject( KNOWN_USER_ID, PROJECT_1 );
        repository.starProject( KNOWN_USER_ID, PROJECT_2 );

        // act
        repository.unstarProject( KNOWN_USER_ID, PROJECT_2 );

        // assert
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_ID );
        assertThat( result, contains( PROJECT_1 ) );
    }

    @Test
    public void testUnstarProject_CtorStarTwoProjects1and2UnstarProject1_CollectionContainsProject2() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = new FSqrUserToProjectRepositoryImpl();
        repository.starProject( KNOWN_USER_ID, PROJECT_1 );
        repository.starProject( KNOWN_USER_ID, PROJECT_2 );

        // act
        repository.unstarProject( KNOWN_USER_ID, PROJECT_1 );

        // assert
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_ID );
        assertThat( result, contains( PROJECT_2 ) );
    }

}
