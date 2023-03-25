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

    private static final String APPROVED_JSON = ".approved.json";
    private static final String RECEIVED_JSON = ".received.json";

    public static <T extends Object> void approve( List<T> received ) {
        RuntimeException runtimeException = new RuntimeException();
        StackTraceElement unitTestStackTraceElement = findUnitTestName( runtimeException.getStackTrace() );

        new JsonApprovals().approveJson( unitTestStackTraceElement, received );
    }

    private static StackTraceElement findUnitTestName( StackTraceElement[] stackTrace ) {
        // skip all elements in stack trace starting with this class name 
        // then select first one, this is the file name and package name for the approval.

        for (StackTraceElement stackTraceElement : stackTrace) {
            if (!JsonApprovals.class.getCanonicalName().equals( stackTraceElement.getClassName() )) {
                System.out.println( "JsonApprovals: " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() );
                return stackTraceElement;
            }
        }

        throw new ApprovalError( "StackTraceElement is null; Can't determine the Testname." );
    }

    public JsonApprovals() {
    }

    public void approveJson( StackTraceElement unitTestStackTraceElement, List<? extends Object> received ) {
        String approvedFileName = buildApprovedFileName( unitTestStackTraceElement );
        String receivedFileName = buildReceivedFileName( unitTestStackTraceElement );

        // TODO: check if approval file name exists. -> if not we now have a ApprovalFailure.
        if (!isApprovalPresent( approvedFileName )) {
            // TODO write received Json first

            // throw new ApprovalFailure( "Not yet approved. Please approve this test by renaming the '.received.json' to '.approved.json'." );
        }
    }

    private boolean isApprovalPresent( String approvedFileName ) {
        return false;
    }

    private String buildApprovedFileName( StackTraceElement stackFrame ) {
        return stackFrame.getClassName() + "." + stackFrame.getMethodName() + APPROVED_JSON;
    }

    private String buildReceivedFileName( StackTraceElement unitTestStackTraceElement ) {
        return unitTestStackTraceElement.getClassName() + "." + unitTestStackTraceElement.getMethodName() + RECEIVED_JSON;
    }

}
