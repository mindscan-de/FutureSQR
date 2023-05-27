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
package de.mindscan.futuresqr.scmaccess;

import de.mindscan.futuresqr.scmaccess.git.GitScmContentProvider;
import de.mindscan.futuresqr.scmaccess.git.GitScmHistoryProvider;
import de.mindscan.futuresqr.scmaccess.git.GitScmRepositoryServicesProvider;
import de.mindscan.futuresqr.scmaccess.impl.EmptyScmContentProvider;
import de.mindscan.futuresqr.scmaccess.impl.EmptyScmHistoryProvider;
import de.mindscan.futuresqr.scmaccess.impl.EmptyScmRepositoryServicesProvider;

/**
 * 
 */
public class ScmAccessFactory {

    public static ScmContentProvider getEmptyContentProvider() {
        return new EmptyScmContentProvider();
    }

    public static ScmContentProvider getGitContentProvider( ScmConfigurationProvider configProvider ) {
        return new GitScmContentProvider( configProvider );
    }

    public static ScmHistoryProvider getEmptyHistoryProvider() {
        return new EmptyScmHistoryProvider();
    }

    public static ScmHistoryProvider getGitHistoryProvider( ScmConfigurationProvider configProvider ) {
        return new GitScmHistoryProvider( configProvider );
    }

    public static ScmRepositoryServicesProvider getEmptyRepositoryServicesProvider() {
        return new EmptyScmRepositoryServicesProvider();
    }

    public static ScmRepositoryServicesProvider getGitRepositoryServicesProvider( ScmConfigurationProvider configProvider ) {
        return new GitScmRepositoryServicesProvider( configProvider );
    }

}
