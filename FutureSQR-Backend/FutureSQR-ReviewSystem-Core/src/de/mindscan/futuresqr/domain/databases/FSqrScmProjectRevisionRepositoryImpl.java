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
package de.mindscan.futuresqr.domain.databases;

import java.util.ArrayList;
import java.util.List;

import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectType;
import de.mindscan.futuresqr.scmaccess.git.GitScmHistoryProvider;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

/**
 * 
 */
public class FSqrScmProjectRevisionRepositoryImpl {

    private GitScmHistoryProvider gitHistoryProvider = new GitScmHistoryProvider();

    public List<FSqrRevision> getRecentRevisions( FSqrScmProjectConfiguration scmConfiguration, String projectId ) {
        if (scmConfiguration.getScmProjectType() == FSqrScmProjectType.git) {

            gitHistoryProvider.getNRecentRevisions( translate( scmConfiguration ), 75 );
        }

        List<FSqrRevision> result = new ArrayList<>();

        return result;
    }

    private ScmRepository translate( FSqrScmProjectConfiguration scmConfiguration ) {
        // TODO: convert or read from translation map. 
        return null;
    }
}
