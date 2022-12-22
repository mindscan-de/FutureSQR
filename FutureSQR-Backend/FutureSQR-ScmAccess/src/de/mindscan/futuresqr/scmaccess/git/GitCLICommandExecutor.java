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
package de.mindscan.futuresqr.scmaccess.git;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

/**
 * Actually this needs to be executed from a queue and should use promises.
 * Also we should think about thread switching and such.... otherwise the
 * webserver will stall for each command. But this can be done later.
 * this might be problematic if 100 users actually want to access the
 * repositories for content.
 */
public class GitCLICommandExecutor {

    // start with hard coded git executable to make things work.
    public static final String GIT_EXECUTABLE_PATH = "C:\\Program Files\\Git\\cmd\\git.exe";

    public void execute( ScmRepository repository, GitCommand command ) {
        List<String> gitCliCommand = buildCliCommand( repository, command );

        try {
            // use WaitFor()?
            Process process = new ProcessBuilder( gitCliCommand ).start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader( is );
            BufferedReader br = new BufferedReader( isr );

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println( line );
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<String> buildCliCommand( ScmRepository repository, GitCommand command ) {
        List<String> gitCliCommand = new ArrayList<String>();
        gitCliCommand.add( GIT_EXECUTABLE_PATH );

        // actually depends on repository and repository type
        gitCliCommand.add( "-C" );
        gitCliCommand.add( "D:\\Temp\\future-square-cache\\FutureSQR" );

        gitCliCommand.addAll( command.getArguments() );

        return gitCliCommand;
    }
}
