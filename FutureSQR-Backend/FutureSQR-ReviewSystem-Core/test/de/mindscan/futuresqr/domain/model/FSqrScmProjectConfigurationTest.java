package de.mindscan.futuresqr.domain.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

}
