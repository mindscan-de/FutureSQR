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

import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class MultiPartFormdataParameters {

    private Map<String, String> parameters = new HashMap<>();

    public MultiPartFormdataParameters() {

    }

    public void addParameter( String parameterName, String parameterValue ) {
        this.parameters.put( parameterName, parameterValue );
    }

    public String getString( String parameterName ) {
        return parameters.getOrDefault( parameterName, null );
    }

    public String getStringOrThrow( String parameterName ) {
        if (parameters.containsKey( parameterName )) {
            return parameters.get( parameterName );
        }

        throw new RuntimeException( "Mising '" + parameterName + "' in transferred FormData." );
    }

    // for optional parameters
    public String getStringOrDefault( String parameterName, String defaultValue ) {
        return parameters.getOrDefault( parameterName, defaultValue );
    }

    public boolean hasParameter( String parameterName ) {
        return this.parameters.containsKey( parameterName );
    }
}
