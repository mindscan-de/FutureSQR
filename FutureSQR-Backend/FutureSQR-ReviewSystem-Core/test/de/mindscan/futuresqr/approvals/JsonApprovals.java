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
package de.mindscan.futuresqr.approvals;

import java.util.List;

/**
 * 
 */
public class JsonApprovals {

    public static <T extends Object> void approve( List<T> fileContentChangeSet ) {
        RuntimeException runtimeException = new RuntimeException();
        StackTraceElement unitTestStackTraceElement = findUnitTestName( runtimeException.getStackTrace() );

        // TODO: findUnitTestParameters? - the parameters the test was started with.... but basically i don't care enough for now for it.
    }

    private static StackTraceElement findUnitTestName( StackTraceElement[] stackTrace ) {
        // skip all elements in stack trace starting with this class name 
        // then select first one, this is the file name and package name for the approval.

        for (StackTraceElement stackTraceElement : stackTrace) {
            if (JsonApprovals.class.getCanonicalName().equals( stackTraceElement.getClassName() )) {
                System.out.println( stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() );
                return stackTraceElement;
            }
        }

        throw new ApprovalError( "StackTraceElement is null; Can't determine the Testname." );
    }

}
