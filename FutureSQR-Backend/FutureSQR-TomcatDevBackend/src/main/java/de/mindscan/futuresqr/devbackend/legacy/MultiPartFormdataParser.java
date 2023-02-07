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
 * We won't support much features anyways. Means file upload will not be supported.
 * Just parse the absolute necessary minimum, to make this data processing work.
 * The Truth comes from the angular application, we will support what the angular
 * application challenges us with.
 * 
 * The boundary is basically transported in the request header, which we don't know.
 * So we parse the boundary from the first line of the request body - this might not
 * be exact but is still good enough. Also the default charset is buried in the 
 * request header - but we also don't want to go down this rabit hole for a developer
 * backend.   
 */
public class MultiPartFormdataParser {

    private String requestBody;

    public static MultiPartFormdataParser createParser( String requestBody ) {
        return new MultiPartFormdataParser( requestBody );
    }

    public static MultiPartFormdataParser createParserAndDump( String requestBody ) {
        return new MultiPartFormdataParser( requestBody ).dumpRequestBody();
    }

    /**
     * 
     */
    public MultiPartFormdataParser( InputStream is ) {

    }

    public MultiPartFormdataParser( String requestBody ) {
        this.requestBody = requestBody;
    }

    public MultiPartFormdataParser dumpRequestBody() {
        System.out.println( "---> start RequestBody string --->" );
        System.out.println( requestBody );
        System.out.println( "---> stop RequestBody (string) --->" );
        System.out.println( Arrays.toString( requestBody.getBytes() ) );
        System.out.println( "---> stop RequestBody (bytearray) --->" );

        return this;
    }

    public MultiPartFormdataParameters parse() {
        MultiPartFormdataParameters postParameters = new MultiPartFormdataParameters();

        // parse header and isolate boundary
        String[] boundaryArray = requestBody.split( "\\R", 2 );
        if (boundaryArray.length < 2) {
            return postParameters;
        }

        String boundary = boundaryArray[0];
        String[] requestParameters = requestBody.split( boundary + "(\\R)?" );

        // advance boundary and then parse name and then parse value
        for (String singlePostParameter : requestParameters) {

            // if this last element
            if (singlePostParameter.equals( "--" ) || singlePostParameter.startsWith( "--" + "\r\n" )) {
                break;
            }

            collectPostParameter( postParameters, singlePostParameter );
        }

        postParameters.addParameter( "revisionid", "19aee1fa31a7c55d998ede33bfd3f487f70fb898" );
        postParameters.addParameter( "opening_userid", "8ce74ee9-48ff-3dde-b678-58a632887e31" );

        return postParameters;
    }

    private void collectPostParameter( MultiPartFormdataParameters postParameters, String singlePostParameter ) {
        System.out.println( ">>>" + singlePostParameter + "<<<" );

        String remaining = singlePostParameter;

        // Content-Disposition: form-data; name=""
        if (singlePostParameter.startsWith( "Content-Disposition" )) {
            // TODO: parse Content-Disposition parse until next new line
            String[] data = remaining.split( "\\R", 2 );
            if (data.length != 2) {
                // throw parse ContentDispositionError;
            }

            String contentDispositionLine = data[0];

            // split contentDispositionLine and extract parameter names.

            remaining = data[1];
        }

        // TODO: if next line is not starting with ""+"\r\n" - throw parser error

        // just read until last "\r\n"
        // Data needs to be converted...
        //check if last boundary which has an additional "--" attached. 
    }
}
