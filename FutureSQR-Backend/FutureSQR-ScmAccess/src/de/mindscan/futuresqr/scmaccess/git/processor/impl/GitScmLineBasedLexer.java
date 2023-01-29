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
package de.mindscan.futuresqr.scmaccess.git.processor.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Basic scanner, which will be refactored a to more sophisticated over time.
 * 
 * Currently backed by a String array. This will change to streams and finally to Byte Streams, but as for now, this
 * is good enough.  
 */
public class GitScmLineBasedLexer {

    private int currentLine;
    private List<String> input;

    public GitScmLineBasedLexer() {
    }

    public GitScmLineBasedLexer( Collection<String> input ) {
        this.input = new ArrayList<String>( input );
        this.currentLine = 0;
    }

    public GitScmLineBasedLexer( String[] input ) {
        this.input = Arrays.asList( input );
        this.currentLine = 0;
    }

    public void resetLexer() {

    }

    public String getCurrentLine() {
        return input.get( currentLine );
    }

    public void advanceToNextLine() {
        this.currentLine++;
    }

    public boolean hasNextLine() {
        return currentLine < input.size();
    }
}
