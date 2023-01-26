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

    public FSqrScmHistory getRecentRevisionHistory( String projectId ) {
        return getRecentRevisionHistory( toScmConfiguration( projectId ), projectId );
    }

    private FSqrScmProjectConfiguration toScmConfiguration( String projectId ) {
        return applicationServices.getConfigurationRepository().getProjectConfiguration( projectId );
    }

    private FSqrScmHistory getRecentRevisionHistory( FSqrScmProjectConfiguration scmConfiguration, String projectId ) {
        if (scmConfiguration.getScmProjectType() == FSqrScmProjectType.git) {

            ScmHistory nRecentHistory = gitHistoryProvider.getNRecentRevisions( toScmRepository( scmConfiguration ), 75 );
            return translate( nRecentHistory, projectId );
        }

        FSqrScmHistory result = new FSqrScmHistory();

        return result;
    }

    private FSqrScmHistory translate( ScmHistory nRecentHistory, String projectId ) {
        FSqrScmHistory result = new FSqrScmHistory();

        nRecentHistory.revisions.stream().forEach( x -> result.addRevision( translate( x, projectId ) ) );

        return result;
    }

    private FSqrRevision translate( ScmBasicRevisionInformation x, String projectId ) {
        FSqrRevision result = new FSqrRevision( x );

        // calculate the author id / lookup author uuid
        String authorUUID = applicationServices.getUserRepository().getUserUUID( x.authorId );
        result.setAuthorUuid( authorUUID );

        // actually not only set the autor uuid but also provide a set of ui authors.
        // goal is to remove the uiautor database on the frontend side, and provide all authors for the revision result.
        // result.addAuthor( authorUUID, applicationServices.getUserRepository().lookupUUID(authorUUID));

        // TODO: shorten message for message head (max until first newline) 
        // result.setCommitMessageHead( x.message );

        // calculate whether a review is known for this 
        if (applicationServices.getReviewRepository().hasReviewForProjectAndRevision( projectId, x.revisionId )) {
            result.setHasAttachedReview( true );
            result.setReviewId( applicationServices.getReviewRepository().getReviewIdForProjectAndRevision( projectId, x.revisionId ) );
        }

        return result;
    }

    // TODO: calculate the correct system path from scmConfiguration.
    private ScmRepository toScmRepository( FSqrScmProjectConfiguration scmConfiguration ) {
        String repoCachePath = applicationServices.getSystemConfiguration().getSystemRepoCachePath();

        if (scmConfiguration.getScmProjectType() == FSqrScmProjectType.git) {
            if (scmConfiguration.hasLocalRepoPath()) {
                // not yet nice but better than before.
                return new ScmRepository( repoCachePath + scmConfiguration.getScmGitAdminConfiguration().localPath );
            }
        }

        // TODO: this has to be fixed soon...
        ScmRepository result = new ScmRepository( repoCachePath + "FutureSQR" );

        return result;
    }

    public FSqrRevision getSimpleRevisionInformation( String projectId, String revisionId ) {
        FSqrScmProjectConfiguration scmConfiguration = toScmConfiguration( projectId );
        if (scmConfiguration.getScmProjectType() == FSqrScmProjectType.git) {
            ScmRepository scmRepository = toScmRepository( scmConfiguration );
            ScmHistory scmHistory = gitHistoryProvider.getSimpleRevisionInformation( scmRepository, revisionId );
            FSqrScmHistory result = translate( scmHistory, projectId );

            return result.getRevisions().get( 0 );
        }

        return new FSqrRevision();
    }

}
