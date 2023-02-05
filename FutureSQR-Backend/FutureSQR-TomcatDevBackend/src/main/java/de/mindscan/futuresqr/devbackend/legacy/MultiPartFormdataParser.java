/**
 * 
 * MIT License
 *
 * Copyright (c) 2023 Maxim Gansert, Mindscan
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
package de.mindscan.futuresqr.devbackend.legacy;

import java.io.InputStream;
import java.util.Arrays;

/**
 * Because we use Tomcat 7 right now, we don't have access to Servlet version 3 and 
 * multipart formdata is not supported by default before version 3. We use Tomcat 7 
 * because it is simple to use with maven for rapid development. I don't want to 
 * change the developer backend right now. I will live with this deficit for some 
 * time until the backend is good enough to invest time to fix the http-rest-backend
 * to a more recent version.
 * 
 * I currently don't want to spend time for ramp up, to a different framework.
 * 
 * We won't support much features anyways.
 */
public class MultiPartFormdataParser {

    private String requestBody;

    public static MultiPartFormdataParser createParser( String requestBody ) {
        return new MultiPartFormdataParser( requestBody );
    }

    /**
     * 
     */
    public MultiPartFormdataParser( InputStream is ) {

    }

    public MultiPartFormdataParser( String requestBody ) {
        this.requestBody = requestBody;

    }

    public MultiPartFormdataParameters parse() {
        MultiPartFormdataParameters parameters = new MultiPartFormdataParameters();

        System.out.println( "---> start RequestBody string --->" );
        System.out.println( requestBody );
        System.out.println( "---> stop RequestBody (string) --->" );
        System.out.println( Arrays.toString( requestBody.getBytes() ) );
        System.out.println( "---> stop RequestBody (bytearray) --->" );

        // TODO: parse header and isolate boundary

        // advance boundary and then parse name and then parse value

        //check if last boundary which has an additional "--" attached. 

        return parameters;
    }
}
