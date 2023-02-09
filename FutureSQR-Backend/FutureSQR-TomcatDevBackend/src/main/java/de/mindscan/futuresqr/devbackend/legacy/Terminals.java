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

/**
 * 
 */
public class Terminals {

    // Quotes
    public final static char[] TERMINAL_QUOTES = new char[] { '\'', '"' };

    public static boolean isCharIn( char currentChar, char[] charSet ) {
        for (int i = 0; i < charSet.length; i++) {
            if (currentChar == charSet[i]) {
                return true;
            }
        }
        return false;
    }

    public static boolean isStartOfQuote( char currentChar ) {
        return isCharIn( currentChar, TERMINAL_QUOTES );
    }

    public static boolean isStartOfLineSeparator( char currentChar ) {
        return currentChar == '\n' || currentChar == '\r';
    }

    public static boolean isSpace( char currenChar ) {
        return currenChar == ' ';
    }

    public static boolean isSpaceOrLineSeparator( char currentChar ) {
        return currentChar == ' ' || isStartOfLineSeparator( currentChar );
    }

}
