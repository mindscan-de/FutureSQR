package de.mindscan.futuresqr.domain.databases;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import java.util.Set;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesImpl;
import de.mindscan.futuresqr.domain.model.user.FSqrSystemUser;

public class FSqrUserToProjectRepositoryImplTest {

    private static final String ANOTHER_USER_UUID = "ffffffff-3049-3498-9f6b-ce828515bba2";
    private static final String KNOWN_USER_UUID = "88888888-48ff-3dde-b678-58a632887e31";

    private static final String KNOWN_USER_ID = "KNOWN_USER_ID";
    private static final String ANOTHER_USER_ID = "ANOTHER_USER_ID";

    private static final String PROJECT_1 = "PROJECT_1";
    private static final String PROJECT_2 = "PROJECT_2";

    @Test
    public void testGetAllStarredProjectsForUser_Ctor_CollectionIsEmpty() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = createInitializedUserToProjectrepository();

        // act
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_ID );

        // assert
        assertThat( result, empty() );
    }

    @Test
    public void testStarProject_CtorThenStarKnownProject_CollectionIsNotEmpty() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = createInitializedUserToProjectrepository();

        // act
        repository.starProject( KNOWN_USER_UUID, PROJECT_1 );

        // assert
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_UUID );
        assertThat( result, not( empty() ) );
    }

    @Test
    public void testGetAllStarredProjectsForUser_CtorStarForKnownUserRequestForAnotherUser_CollectionIsEmpty() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = createInitializedUserToProjectrepository();
        repository.starProject( KNOWN_USER_UUID, PROJECT_1 );

        // act
        Set<String> result = repository.getAllStarredProjectsForUser( ANOTHER_USER_UUID );

        // assert
        assertThat( result, empty() );
    }

    @Test
    public void testStarProject_CtorThenStarProject1_CollectionContainsProject1() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = createInitializedUserToProjectrepository();

        // act
        repository.starProject( KNOWN_USER_UUID, PROJECT_1 );

        // assert
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_UUID );
        assertThat( result, contains( PROJECT_1 ) );
    }

    @Test
    public void testStarProject_CtorThenStarProject2_CollectionContainsProject2() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = createInitializedUserToProjectrepository();

        // act
        repository.starProject( KNOWN_USER_UUID, PROJECT_2 );

        // assert
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_UUID );
        assertThat( result, contains( PROJECT_2 ) );
    }

    @Test
    public void testStarProject_CtorThenStarProject1and2_CollectionContainsBothProjects() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = createInitializedUserToProjectrepository();

        // act
        repository.starProject( KNOWN_USER_UUID, PROJECT_1 );
        repository.starProject( KNOWN_USER_UUID, PROJECT_2 );

        // assert
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_UUID );
        assertThat( result, containsInAnyOrder( PROJECT_1, PROJECT_2 ) );
    }

    @Test
    public void testUnstarProject_CtorStarTwoProjects1and2UnstarProject2_CollectionContainsProject1() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = createInitializedUserToProjectrepository();
        repository.starProject( KNOWN_USER_UUID, PROJECT_1 );
        repository.starProject( KNOWN_USER_UUID, PROJECT_2 );

        // act
        repository.unstarProject( KNOWN_USER_UUID, PROJECT_2 );

        // assert
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_UUID );
        assertThat( result, contains( PROJECT_1 ) );
    }

    @Test
    public void testUnstarProject_CtorStarTwoProjects1and2UnstarProject1_CollectionContainsProject2() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = createInitializedUserToProjectrepository();
        repository.starProject( KNOWN_USER_UUID, PROJECT_1 );
        repository.starProject( KNOWN_USER_UUID, PROJECT_2 );

        // act
        repository.unstarProject( KNOWN_USER_UUID, PROJECT_1 );

        // assert
        Set<String> result = repository.getAllStarredProjectsForUser( KNOWN_USER_UUID );
        assertThat( result, contains( PROJECT_2 ) );
    }

    @Test
    public void testIsStarred_StarProject1ForKownUser_returnsIsStarredForKnownUser() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = createInitializedUserToProjectrepository();
        repository.starProject( KNOWN_USER_UUID, PROJECT_1 );

        // act
        boolean result = repository.isStarred( KNOWN_USER_UUID, PROJECT_1 );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testIsStarred_StarProject2ForKownUserButRequestProject1_returnsIsNotStarredForKnownUser() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = createInitializedUserToProjectrepository();
        repository.starProject( KNOWN_USER_UUID, PROJECT_2 );

        // act
        boolean result = repository.isStarred( KNOWN_USER_UUID, PROJECT_1 );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsStarred_StarThenUnstarProject1ForKownUser_returnsIsNotStarredForKnownUser() throws Exception {
        // arrange
        FSqrUserToProjectRepositoryImpl repository = createInitializedUserToProjectrepository();
        repository.starProject( KNOWN_USER_UUID, PROJECT_1 );
        repository.unstarProject( KNOWN_USER_UUID, PROJECT_1 );

        // act
        boolean result = repository.isStarred( KNOWN_USER_UUID, PROJECT_1 );

        // assert
        assertThat( result, equalTo( false ) );
    }

    private FSqrUserToProjectRepositoryImpl createInitializedUserToProjectrepository() {
        FSqrApplicationServices applicationServices = new FSqrApplicationServicesImpl();

        FSqrSystemUser known_user = new FSqrSystemUser( KNOWN_USER_UUID, KNOWN_USER_ID, "test1", "email1" );
        FSqrSystemUser another_user = new FSqrSystemUser( ANOTHER_USER_UUID, ANOTHER_USER_ID, "test2", "email2" );

        applicationServices.getUserRepository().addUserEntry( known_user );
        applicationServices.getUserRepository().addUserEntry( another_user );

        return applicationServices.getUserToProjectRepository();
    }

}
