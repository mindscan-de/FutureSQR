package de.mindscan.futuresqr.devbackend.legacy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

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

}
