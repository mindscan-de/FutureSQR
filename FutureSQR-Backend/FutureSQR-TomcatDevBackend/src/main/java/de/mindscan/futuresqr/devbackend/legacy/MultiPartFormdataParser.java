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
        MultiPartFormdataParameters postParameterCollector = new MultiPartFormdataParameters();

        // parse header and isolate boundary
        String[] boundaryArray = requestBody.split( "\\R", 2 );
        if (boundaryArray.length < 2) {
            return postParameterCollector;
        }

        String boundary = boundaryArray[0];
        String[] requestParameterBlocks = requestBody.split( "(\\R)?" + boundary + "(\\R)?" );

        // advance boundary and then parse name and then parse value
        for (String singlePostParameter : requestParameterBlocks) {

            // this is the last element
            if (singlePostParameter.equals( "--" ) || singlePostParameter.startsWith( "--" + "\r\n" )) {
                break;
            }

            try {
                collectPostParameter( postParameterCollector, new StringBasedLexer( singlePostParameter ) );
            }
            catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        return postParameterCollector;
    }

    private void collectPostParameter( MultiPartFormdataParameters postParameters, Lexer lexer ) {
        String parameterName = null;

        // Parse Header...
        while (lexer.isTokenEndBeforeInputEnd()) {
            lexer.prepareNextToken();

            if (Terminals.isStartOfLineSeparator( lexer.charAtTokenStart() )) {
                lexer.incrementTokenEndWhile( Terminals::isStartOfLineSeparator, 4 );

                String newLineMode = lexer.getTokenString();
                lexer.advanceToNextToken();

                if ("\r\n\r\n".equals( newLineMode )) {
                    // either it is double newline "\r\n\r\n" -> switch to value read
                    // switch to value mode
                    break;
                }
                else if ("\r\n".equals( newLineMode )) {
                    // or is single "\r\n" then continue with header parsing.
                    continue;
                }
                else {
                    // something unknown happened. What if the data is empty?
                    // 
                    throw new RuntimeException( "Unexpected newline combination found." );
                }
            }
            else {
                lexer.incrementTokenEndWhileNot( Terminals::isSpaceOrLineSeparator );

                String currentToken = lexer.getTokenString();

                if ("Content-Disposition:".equals( currentToken )) {
                    // we consume this token
                    lexer.advanceToNextToken();

                    // read over the spaces
                    lexer.prepareNextToken();
                    lexer.incrementTokenEndWhile( Terminals::isSpace );
                    lexer.advanceToNextToken();

                    // read the content type:
                    lexer.prepareNextToken();
                    lexer.incrementTokenEndWhileNot( Terminals::isSpaceOrLineSeparator );
                    String contentType = lexer.getTokenString();

                    if (!"form-data;".equals( contentType )) {
                        // we found a different content type.
                        // could be file upload, but we don't support it so we skip this part of the multi-part form-data
                        System.out.println( "Unexpected ContentType found: '" + contentType + "'" );
                        return;
                    }

                    // we consume this form-data token
                    lexer.advanceToNextToken();

                    // read over the spaces
                    lexer.prepareNextToken();
                    lexer.incrementTokenEndWhile( Terminals::isSpace );
                    lexer.advanceToNextToken();

                    // now parse name="nameOf"
                    lexer.prepareNextToken();
                    lexer.incrementTokenEndWhileNot( Terminals::isDoubleQuote );
                    String expectedAssignment = lexer.getTokenString();
                    if (!"name=".equals( expectedAssignment )) {
                        System.out.println( "Expected 'name=\"...\"' but found: '" + expectedAssignment + "'" );
                        return;
                    }
                    lexer.advanceToNextToken();

                    // skip first double quotes.
                    lexer.skipSingleChar();

                    // read parameter name
                    lexer.prepareNextToken();
                    lexer.incrementTokenEndWhileNot( Terminals::isDoubleQuote );
                    parameterName = lexer.getTokenString();
                    lexer.advanceToNextToken();

                    // skip everything until end of line.
                    lexer.prepareNextToken();
                    lexer.incrementTokenEndWhileNot( Terminals::isStartOfLineSeparator );
                    lexer.advanceToNextToken();

                    // parse content disposition until newline
                }
                else {
                    System.out.println( "CurrentToken='" + currentToken + "'" );
                    lexer.advanceToNextToken();
                }
            }
        }

        // TODO: parse and convert value.
        String parameterValue = lexer.getRemainingString();

        // TODO: actually based on the encoding and based on the charset this data must be transformed...

        if (parameterName != null && parameterValue != null) {
            postParameters.addParameter( parameterName, parameterValue );
        }

    }
}
