/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.futuresqr.core.uuid;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * 
 */
public class UuidUtil {

    // RFC 4122 - https://www.rfc-editor.org/rfc/rfc4122.html#section-4.3
    public static final UUID NAMESPACE_OID = UUID.fromString( "6ba7b812-9dad-11d1-80b4-00c04fd430c8" );

    public static final UUID FUTURESQR_NAMESPACE_OID = buildUUIDByNamespace( NAMESPACE_OID, "FutureSQR" );
    public static final UUID SYSCONFIG_NAMESPACE_OID = buildUUIDByNamespace( FUTURESQR_NAMESPACE_OID, "SystemInstance" );
    public static final UUID USERNAMES_NAMESPACE_OID = buildUUIDByNamespace( FUTURESQR_NAMESPACE_OID, "SystemUsers" );

    public static UUID buildUUIDByNamespace( UUID namespace, String name ) {
        long nsMsb = namespace.getMostSignificantBits();
        long nsLsb = namespace.getLeastSignificantBits();
        byte[] nameBytes = name.getBytes();

        return UUID.nameUUIDFromBytes( joinNameSpaceBytes( nsMsb, nsLsb, nameBytes ) );
    }

    public static UUID uuidForUserName( String name ) {
        return buildUUIDByNamespace( USERNAMES_NAMESPACE_OID, name );
    }

    public static UUID getRandomUUID() {
        return UUID.randomUUID();
    }

    private static byte[] joinNameSpaceBytes( long nsMsb, long nsLsb, byte[] nameBytes ) {
        ByteBuffer bb = ByteBuffer.allocate( 8 + 8 + nameBytes.length );

        bb.put( longToBytes( nsMsb ) );
        bb.put( longToBytes( nsLsb ) );
        bb.put( nameBytes );

        return bb.array();
    }

    private static byte[] longToBytes( long l ) {
        byte[] result = new byte[Long.BYTES];
        for (int i = Long.BYTES - 1; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= Byte.SIZE;
        }
        return result;
    }
}
