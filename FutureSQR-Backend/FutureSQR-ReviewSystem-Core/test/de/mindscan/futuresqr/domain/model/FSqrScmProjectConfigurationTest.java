package de.mindscan.futuresqr.domain.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.jupiter.api.Test;

public class FSqrScmProjectConfigurationTest {

    @Test
    public void testCreateNewReviewIndex_CtorAutoIndexIsOne_returnsOneForFirstCall() throws Exception {
        // arrange
        FSqrScmProjectConfiguration configuration = new FSqrScmProjectConfiguration( "test", "Test DisplayName", "11111-1111-11111", 1 );

        // act
        int result = configuration.createNewReviewIndex();

        // assert
        assertThat( result, equalTo( 1 ) );
    }

    @Test
    public void testCreateNewReviewIndex_CtorAutoIndexIsOneTwoCalls_returnsTwoForSecondCall() throws Exception {
        // arrange
        FSqrScmProjectConfiguration configuration = new FSqrScmProjectConfiguration( "test", "Test DisplayName", "11111-1111-11111", 1 );

        // act
        configuration.createNewReviewIndex();
        int result = configuration.createNewReviewIndex();

        // assert
        assertThat( result, equalTo( 2 ) );
    }

    @Test
    public void testCreateNewReviewIndex_CtorAutoIndexIsOneHundred_returnsOneHundredForFirstCall() throws Exception {
        // arrange
        FSqrScmProjectConfiguration configuration = new FSqrScmProjectConfiguration( "test", "Test DisplayName", "11111-1111-11111", 100 );

        // act
        int result = configuration.createNewReviewIndex();

        // assert
        assertThat( result, equalTo( 100 ) );
    }

    @Test
    public void testCreateNewReviewIdentifierWithPrefix_CTorAutoIndexOneHundred_returnsOneHundredForFirstCall() throws Exception {
        // arrange
        FSqrScmProjectConfiguration configuration = new FSqrScmProjectConfiguration( "test", "Test DisplayName", "11111-1111-11111", 100 );

        // act
        String result = configuration.createNewReviewIdentifierWithPrefix();

        // assert
        assertThat( result, equalTo( "CR-100" ) );
    }

    @Test
    public void testCreateNewReviewIdentifierWithPrefix_CTorAutoIndexTwoHundred_returnsTwoHundredForFirstCall() throws Exception {
        // arrange
        FSqrScmProjectConfiguration configuration = new FSqrScmProjectConfiguration( "test", "Test DisplayName", "11111-1111-11111", 200 );

        // act
        String result = configuration.createNewReviewIdentifierWithPrefix();

        // assert
        assertThat( result, equalTo( "CR-200" ) );
    }

    @Test
    public void testGetScmProjectType_CtorOnly_returnsScmProjectTypeIsNone() throws Exception {
        // arrange
        FSqrScmProjectConfiguration configuration = new FSqrScmProjectConfiguration( "test", "Test DisplayName", "11111-1111-11111", 200 );

        // act
        FSqrScmProjectType result = configuration.getScmProjectType();

        // assert
        assertThat( result, equalTo( FSqrScmProjectType.none ) );
    }

    @Test
    public void testGetProjectReviewPrefix_CtorOnly_returnsDefaultCodeReviewPrefix() throws Exception {
        // arrange
        FSqrScmProjectConfiguration configuration = new FSqrScmProjectConfiguration( "test", "Test DisplayName", "11111-1111-11111", 1 );

        // act
        String result = configuration.getProjectReviewPrefix();

        // assert
        assertThat( result, equalTo( FSqrScmProjectConfiguration.DEFAULT_CODEREVIEW_PREFIX ) );
    }

    @Test
    public void testSetProjectReviewPrefix_CTorThenSetToCORE() throws Exception {
        // arrange
        String expectedValueCORE = "CORE-";
        FSqrScmProjectConfiguration configuration = new FSqrScmProjectConfiguration( "test", "Test DisplayName", "11111-1111-11111", 1 );

        // act
        configuration.setProjectReviewPrefix( expectedValueCORE );

        // assert
        String result = configuration.getProjectReviewPrefix();
        assertThat( result, equalTo( expectedValueCORE ) );
    }

    @Test
    public void testGetProjectId_CtorWithTestProject_expectSameTestProjectIdentifier() throws Exception {
        // arrange
        String expectedValueTestProjectId = "test-project";
        FSqrScmProjectConfiguration configuration = new FSqrScmProjectConfiguration( expectedValueTestProjectId, "Test DisplayName", "11111-1111-11111", 1 );

        // act
        String result = configuration.getProjectId();

        // assert
        assertThat( result, is( sameInstance( expectedValueTestProjectId ) ) );
    }

}
