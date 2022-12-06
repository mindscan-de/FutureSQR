package de.mindscan.futuresqr.core.uuid;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class UuidUtilTest {

    @Test
    public void testNAMESPACEOID_static_equalsRfcOIDNamespaceOID() throws Exception {
        // arrange
        // act
        UUID result = UuidUtil.NAMESPACE_OID;

        // assert
        assertThat( result.toString(), Matchers.equalTo( "6ba7b812-9dad-11d1-80b4-00c04fd430c8" ) );
    }

    @Test
    public void testBuildUUIDByNamespace_StringFutureSQR_isEqualToPythonProjectFutureSqrNamespaceUuid() throws Exception {
        // arrange
        // act
        UUID result = UuidUtil.buildUUIDByNamespace( UuidUtil.NAMESPACE_OID, "FutureSQR" );

        // assert
        assertThat( result.toString(), Matchers.equalTo( "1c92b013-e68d-36b1-a3a9-98d6658ca4e0" ) );
    }

    @Test
    public void testUuidForUserName_mindscanbanned_UUIDAsInPython() {
        // arrange
        // act
        UUID result = UuidUtil.uuidForUserName( "mindscan-banned" );

        // assert
        assertThat( result.toString(), Matchers.equalTo( "6822a80d-1854-304c-a26d-81acd2c008f3" ) );
    }

    @Test
    public void testUuidForUserName_rbreunung_UUIDAsInPython() {
        // arrange
        // act
        UUID result = UuidUtil.uuidForUserName( "rbreunung" );

        // assert
        assertThat( result.toString(), Matchers.equalTo( "35c94b55-559f-30e4-a2f4-ee16d31fc276" ) );
    }

}
