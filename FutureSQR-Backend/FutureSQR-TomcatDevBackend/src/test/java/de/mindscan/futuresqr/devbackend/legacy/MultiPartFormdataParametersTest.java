package de.mindscan.futuresqr.devbackend.legacy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MultiPartFormdataParametersTest {

    private static final String UNKNOWN_KEY = "unknownKey";

    private static final String KNOWN_KEY = "knownKey";

    @Test
    public void testGetString_CtorOnlyGetUnknownPaameter_expectNull() throws Exception {
        // arrange
        MultiPartFormdataParameters parameters = new MultiPartFormdataParameters();

        // act
        String result = parameters.getString( UNKNOWN_KEY );

        // assert
        assertThat( result, nullValue() );
    }

    @Test
    public void testGetString_CtorOnlyGetknownpaameterButNotInitialized_expectNull() throws Exception {
        // arrange
        MultiPartFormdataParameters parameters = new MultiPartFormdataParameters();

        // act
        String result = parameters.getString( KNOWN_KEY );

        // assert
        assertThat( result, nullValue() );
    }

    @Test
    public void testGetString_CtorAddKnownParameterWithValueX_expectValueX() throws Exception {
        // arrange
        MultiPartFormdataParameters parameters = new MultiPartFormdataParameters();
        String expectedValueX = "ValueX";
        parameters.addParameter( KNOWN_KEY, expectedValueX );

        // act
        String result = parameters.getString( KNOWN_KEY );

        // assert
        assertThat( result, equalTo( expectedValueX ) );
    }

    @Test
    public void testGetStringOrThrow_CtorUnknownParameter_throwsException() throws Exception {
        // arrange
        MultiPartFormdataParameters parameters = new MultiPartFormdataParameters();

        // act
        // assert
        assertThrows( RuntimeException.class, () -> {
            parameters.getStringOrThrow( UNKNOWN_KEY );
        } );
    }

    @Test
    public void testGetStringOrThrow_CtorUnknownParameterButSetKnownParameter_throwsException() throws Exception {
        // arrange
        MultiPartFormdataParameters parameters = new MultiPartFormdataParameters();
        parameters.addParameter( KNOWN_KEY, "ValueX" );

        // act
        // assert
        assertThrows( RuntimeException.class, () -> {
            parameters.getStringOrThrow( UNKNOWN_KEY );
        } );
    }

    @Test
    public void testGetStringOrThrow_CtorKnownParameter_retursExpectedValueX() throws Exception {
        // arrange
        MultiPartFormdataParameters parameters = new MultiPartFormdataParameters();
        String expectedValueX = "ValueX";
        parameters.addParameter( KNOWN_KEY, expectedValueX );

        // act
        String result = parameters.getStringOrThrow( KNOWN_KEY );

        // assert
        assertThat( result, equalTo( expectedValueX ) );
    }

    @Test
    public void testGetStringOrDefault_CtorUnknownParameter_returnsDefaultValue() throws Exception {
        // arrange
        MultiPartFormdataParameters parameters = new MultiPartFormdataParameters();

        // act
        String result = parameters.getStringOrDefault( UNKNOWN_KEY, "defaultValue" );

        // assert
        assertThat( result, equalTo( "defaultValue" ) );
    }

    @Test
    public void testGetStringOrDefault_CtorKnownKeyKnownValue_returnsKnownKnownValue() throws Exception {
        // arrange
        MultiPartFormdataParameters parameters = new MultiPartFormdataParameters();
        parameters.addParameter( KNOWN_KEY, "knownValue" );

        // act
        String result = parameters.getStringOrDefault( KNOWN_KEY, "defaultValue" );

        // assert
        assertThat( result, equalTo( "knownValue" ) );
    }

}
