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
package de.mindscan.futuresqr.domain.databases;

import java.util.ArrayList;
import java.util.List;

import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.application.FSqrApplicationServicesUnitialized;
import de.mindscan.futuresqr.domain.model.FSqrCodeReview;
import de.mindscan.futuresqr.domain.model.FSqrCodeReviewFactory;
import de.mindscan.futuresqr.domain.model.FSqrRevision;

/**
 * 
 */

public class FSqrCodeReviewRepositoryImpl {

    private FSqrApplicationServices applicationServices;

    /**
     * 
     */
    public FSqrCodeReviewRepositoryImpl() {
        this.applicationServices = new FSqrApplicationServicesUnitialized();
    }

    public void setApplicationServices( FSqrApplicationServices services ) {
        this.applicationServices = services;
    }

    // insertReview( projectid, review )
    // selectReview( projectid, reviewid )

    public boolean hasReviewForProjectAndRevision( String projectid, String revisionid ) {
        return false;
    }

    public String getReviewIdForProjectAndRevision( String projectid, String revisionid ) {
        return "";
    }

    // selectOpenReviews( projectid )
    public List<FSqrCodeReview> selectOpenReviews( String projectId ) {
        // TODO: implement this
        return new ArrayList<>();
    }

    // selectClosedReviews( projectid )
    public List<FSqrCodeReview> selectRecentlyClosedReviews( String projectId ) {
        // TODO: implement this
        return new ArrayList<>();
    }

    // add revision to review
    // remove revision from review

    public FSqrCodeReview createReviewFromRevision( String projectid, FSqrRevision revision ) {
        FSqrScmProjectConfigurationRepositoryImpl configurationRepository = applicationServices.getConfigurationRepository();

        FSqrCodeReview codeReview = FSqrCodeReviewFactory.createReviewFromRevision( projectid, revision, configurationRepository );

        return codeReview;
    }
}
