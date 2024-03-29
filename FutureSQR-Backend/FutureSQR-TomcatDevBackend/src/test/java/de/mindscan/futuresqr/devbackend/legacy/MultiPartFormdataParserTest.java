package de.mindscan.futuresqr.devbackend.legacy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class MultiPartFormdataParserTest {

    private static final String REQUESTBODY_REVISIONID_OPENINGUSERID = "-----------------------------139588989415101292624154993398\r\n" + // 
                    "Content-Disposition: form-data; name=\"revisionid\"\r\n" + // 
                    "\r\n" + // 
                    "19aee1fa31a7c55d998ede33bfd3f487f70fb898\r\n" + //  
                    "-----------------------------139588989415101292624154993398\r\n" + //  
                    "Content-Disposition: form-data; name=\"opening_userid\"\r\n" + //
                    "\r\n" + // 
                    "8ce74ee9-48ff-3dde-b678-58a632887e31\r\n" + // 
                    "-----------------------------139588989415101292624154993398--\r\n" + // 
                    "";

    @Test
    public void testParse_WithTwoArgumentsRevisionIdOpeningUserId_returnHasParameterRevidionid() throws Exception {
        String requestBody = REQUESTBODY_REVISIONID_OPENINGUSERID;
        // arrange
        MultiPartFormdataParser parser = MultiPartFormdataParser.createParser( requestBody );

        // act
        MultiPartFormdataParameters result = parser.parse();

        // assert
        assertThat( result.hasParameter( "revisionid" ), equalTo( true ) );
    }

    @Test
    public void testParse_WithTwoArgumentsRevisionIdOpeningUserId_returnRevidionidIs0x19aee1f() throws Exception {
        String requestBody = REQUESTBODY_REVISIONID_OPENINGUSERID;
        // arrange
        MultiPartFormdataParser parser = MultiPartFormdataParser.createParser( requestBody );

        // act
        MultiPartFormdataParameters result = parser.parse();

        // assert
        assertThat( result.getStringOrThrow( "revisionid" ), equalTo( "19aee1fa31a7c55d998ede33bfd3f487f70fb898" ) );
    }

    @Test
    public void testParse_WithTwoArgumentsRevisionIdOpeningUserId_returnHasParameterOpeningUserId() throws Exception {
        String requestBody = REQUESTBODY_REVISIONID_OPENINGUSERID;
        // arrange
        MultiPartFormdataParser parser = MultiPartFormdataParser.createParser( requestBody );

        // act
        MultiPartFormdataParameters result = parser.parse();

        // assert
        assertThat( result.hasParameter( "opening_userid" ), equalTo( true ) );
    }

    @Test
    public void testParse_WithTwoArgumentsRevisionIdOpeningUserId_returnOpeningUserIdIs() throws Exception {
        String requestBody = REQUESTBODY_REVISIONID_OPENINGUSERID;
        // arrange
        MultiPartFormdataParser parser = MultiPartFormdataParser.createParser( requestBody );

        // act
        MultiPartFormdataParameters result = parser.parse();

        // assert
        assertThat( result.getStringOrThrow( "opening_userid" ), equalTo( "8ce74ee9-48ff-3dde-b678-58a632887e31" ) );
    }

}
