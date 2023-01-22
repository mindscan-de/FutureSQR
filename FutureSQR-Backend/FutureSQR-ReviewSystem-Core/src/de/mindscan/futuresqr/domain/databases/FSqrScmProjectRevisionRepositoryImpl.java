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

import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.FSqrScmHistory;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectType;
import de.mindscan.futuresqr.scmaccess.git.GitScmHistoryProvider;
import de.mindscan.futuresqr.scmaccess.types.ScmBasicRevisionInformation;
import de.mindscan.futuresqr.scmaccess.types.ScmHistory;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

/**
 * 
 */
public class FSqrScmProjectRevisionRepositoryImpl {

    private GitScmHistoryProvider gitHistoryProvider;
    private FSqrApplicationServices applicationServices;

    public FSqrScmProjectRevisionRepositoryImpl() {
        this.gitHistoryProvider = new GitScmHistoryProvider();
        this.applicationServices = new FSqrApplicationServicesUnitialized();
    }

    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;
    }

    public FSqrScmHistory getRecentRevisionHistory( FSqrScmProjectConfiguration scmConfiguration, String projectId ) {
        if (scmConfiguration.getScmProjectType() == FSqrScmProjectType.git) {

            ScmHistory nRecentHistory = gitHistoryProvider.getNRecentRevisions( translate( scmConfiguration ), 75 );
            return translate( nRecentHistory );
        }

        FSqrScmHistory result = new FSqrScmHistory();

        return result;
    }

    public FSqrScmHistory getRecentRevisionHistory( String projectId ) {
        return getRecentRevisionHistory( applicationServices.getConfigurationRepository().getProjectConfiguration( projectId ), projectId );
    }

    private FSqrScmHistory translate( ScmHistory nRecentHistory ) {
        FSqrScmHistory result = new FSqrScmHistory();

        nRecentHistory.revisions.stream().forEach( x -> result.addRevision( translate( x ) ) );

        return result;
    }

    private FSqrRevision translate( ScmBasicRevisionInformation x ) {
        FSqrRevision result = new FSqrRevision( x );

        // TODO: calculate the author id / lookup author uuid
        // result.setAuthorUuid( x.auth );

        // TODO: shorten message for message head (max until first newline) 
        // result.setCommitMessageHead( x.message );

        // TODO: calculate whether a review is known for this 
        // result.setHasAttachedReview( false );
        // result.setReviewId( reviewId );

        return result;
    }

    private ScmRepository translate( FSqrScmProjectConfiguration scmConfiguration ) {
        ScmRepository result = new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" );

        return result;
    }
}
