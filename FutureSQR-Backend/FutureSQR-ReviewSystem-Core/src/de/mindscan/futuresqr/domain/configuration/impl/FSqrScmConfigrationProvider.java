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
package de.mindscan.futuresqr.domain.configuration.impl;

import de.mindscan.futuresqr.domain.configuration.FSqrSystemInstanceConfiguration;
import de.mindscan.futuresqr.scmaccess.ScmConfigurationProvider;

/**
 * 
 */
public class FSqrScmConfigrationProvider implements ScmConfigurationProvider {

    private String gitExecutablePath;
    private String svnExecutablePath;

    /**
     * 
     */
    public FSqrScmConfigrationProvider( FSqrSystemInstanceConfiguration systemInstance ) {
        this.gitExecutablePath = systemInstance.getGitExecutablePath();
        this.svnExecutablePath = systemInstance.getSvnExecutablePath();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getGitExecutablePath() {
        return gitExecutablePath;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getSvnExecutablePath() {
        return svnExecutablePath;
    }

}
