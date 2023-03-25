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

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

/**
 * 
 */
public class JsonApprovals {

    private static final String APPROVED_JSON = ".approved.json";
    private static final String RECEIVED_JSON = ".received.json";
    private static final String TEST_RESOURCES_APPROVALS_DIRECTORY = "test-resources/approvals";

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
                String methodName = stackTraceElement.getMethodName();
                System.out.println( "JsonApprovals: " + stackTraceElement.getClassName() + "." + methodName );
                return stackTraceElement;
            }
        }

        throw new ApprovalError( "StackTraceElement is null; Can't determine the Testname." );
    }

    public JsonApprovals() {
    }

    public void approveJson( StackTraceElement unitTestStackTraceElement, List<? extends Object> received ) {
        Path approvedFileName = buildApprovedFileName( unitTestStackTraceElement );
        Path receivedFileName = buildReceivedFileName( unitTestStackTraceElement );

        if (!isApprovalPresent( approvedFileName )) {
            saveReceived( received, receivedFileName );
            throw new ApprovalFailure( "Not yet approved. Please approve this test by renaming the '.received.json' to '.approved.json'." );
        }

        // TODO: now check if received and approved json match.
    }

    private void saveReceived( List<? extends Object> received, Path receivedFileName ) {

        Gson gson = new GsonBuilder() //
                        .serializeNulls() //
                        .setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter( buildResolvedResourcesFileName( receivedFileName ).toString() )) {
            try (JsonWriter jsonWriter = new JsonWriter( writer );) {
                jsonWriter.setIndent( "  " );
                gson.toJson( received, received.getClass(), jsonWriter );
                jsonWriter.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isApprovalPresent( Path approvedFileName ) {
        return Files.isRegularFile( buildResolvedResourcesFileName( approvedFileName ) );
    }

    private Path buildResolvedResourcesFileName( Path fileName ) {
        return Paths.get( TEST_RESOURCES_APPROVALS_DIRECTORY ).resolve( fileName );
    }

    private Path buildApprovedFileName( StackTraceElement stackFrame ) {
        String className = stackFrame.getClassName();
        String simpleClassName = className.substring( className.lastIndexOf( "." ) + ".".length() );

        return Paths.get( simpleClassName, stackFrame.getMethodName() + APPROVED_JSON );
    }

    private Path buildReceivedFileName( StackTraceElement stackFrame ) {
        String className = stackFrame.getClassName();
        String simpleClassName = className.substring( className.lastIndexOf( "." ) + ".".length() );
        return Paths.get( simpleClassName, stackFrame.getMethodName() + RECEIVED_JSON );
    }

}
