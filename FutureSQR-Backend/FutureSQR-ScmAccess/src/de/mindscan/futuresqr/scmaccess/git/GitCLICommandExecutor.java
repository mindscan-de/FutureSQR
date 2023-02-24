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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.mindscan.futuresqr.scmaccess.HardCodedConstants;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

/**
 * Actually this needs to be executed from a queue and should use promises.
 * Also we should think about thread switching and such.... otherwise the
 * webserver will stall for each command. But this can be done later.
 * this might be problematic if 100 users actually want to access the
 * repositories for content.
 */
public class GitCLICommandExecutor {

    private final String executablePath;

    public GitCLICommandExecutor() {
        this( HardCodedConstants.GIT_EXECUTABLE_PATH );
    }

    public GitCLICommandExecutor( String executablePath ) {
        this.executablePath = executablePath;
    }

    public GitCLICommandOutput execute( ScmRepository repository, GitCommand command ) {
        List<String> gitCliCommand = buildCommandLine( repository, command );
        GitCLICommandOutput gitCommandOutput = new GitCLICommandOutput( repository, command );

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            // use WaitFor()?
            Process process = new ProcessBuilder( gitCliCommand ).start();
            InputStream is = process.getInputStream();

            byte buffer[] = new byte[1024];
            int readLength;

            // TODO: limit the output if necessary, but for now good enough e.g. 4 MB or so?
            while ((readLength = is.read( buffer )) != -1) {
                os.write( buffer, 0, readLength );
            }

            gitCommandOutput.setProcessOutput( os.toByteArray() );

            is.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return gitCommandOutput;
    }

    private List<String> buildCommandLine( ScmRepository repository, GitCommand command ) {
        List<String> gitCliCommand = new ArrayList<String>();
        gitCliCommand.add( this.executablePath );

        // actually depends on repository and repository type
        if (repository.hasLocalRepositoryPath()) {
            gitCliCommand.add( "-C" );
            gitCliCommand.add( repository.getLocalRepositoryPath() );
        }

        gitCliCommand.addAll( command.getArguments() );

        return gitCliCommand;
    }
}
