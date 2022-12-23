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

import de.mindscan.futuresqr.scmaccess.git.processor.ScmFileContentOutputProcessor;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContent;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

/**
 * TOOD: maybe we want to track errors in execution? and abort other steps?
 */
public class GitCLICommandOutput {

    private byte[] outputBuffer = new byte[0];
    private ScmRepository repository;
    private GitCommand command;

    /**
     * 
     */
    public GitCLICommandOutput( ScmRepository repositoy, GitCommand command ) {
        this.repository = repositoy;
        this.command = command;
    }

    /**
     * @param processOutput
     */
    public void setProcessOutput( byte[] processOutput ) {
        this.outputBuffer = processOutput;
    }

    /**
     * @return the outputBuffer
     */
    public byte[] getProcessOutput() {
        return outputBuffer;
    }

    public ScmFileContent collect( ScmFileContentOutputProcessor processor ) {
        return processor.parse( this );
    }

    /**
     * @param repository
     */
    public void setRepository( ScmRepository repository ) {
        this.repository = repository;
    }

    /**
     * @return the repository
     */
    public ScmRepository getRepository() {
        return this.repository;
    }

    /**
     * @param command
     */
    public void setCommand( GitCommand command ) {
        this.command = command;
    }

    /**
     * @return the command
     */
    public GitCommand getCommand() {
        return command;
    }

}
