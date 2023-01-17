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
package de.mindscan.futuresqr.domain.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This thing/entity contains information about the configuration of a scm project. This thin 
 * has a name and identifiers. Etc.
 * 
 * like data which is relevant for the SCM as well as what repotype, what's the displaynames
 * and so on...
 */
public class FSqrScmProjectConfiguration {
    public static final String DEFAULT_CODEREVIEW_PREFIX = "CR-";

    private AtomicInteger autoIndex = new AtomicInteger( 1 );

    // lowercase alphanumeric string including minus chars, 
    // must be unique for the installed fsqr instance.
    private String projectId;

    private String projectDisplayName;
    private String projectDescription = "";

    private final String projectUuid;
    private String projectReviewPrefix = DEFAULT_CODEREVIEW_PREFIX;

    private FSqrScmProjectType scmProjectType;

    // TODO: some details, like ownership (who created, when created, when modified)
    // i guess this is not yet important, and can alo be kept in a kind of journal or so....

    public FSqrScmProjectConfiguration( String projectId, String projectDisplayName, String projectUuid, int autoIndexStart ) {
        this.autoIndex = new AtomicInteger( autoIndexStart );
        this.projectId = projectId;
        this.projectDisplayName = projectDisplayName;
        // this id is unique for the whole lifetime of the review system - server lifetime.
        this.projectUuid = projectUuid;

        // TODO: if we provide an scm admin / scm detail configuration, then we replace this. 
        this.scmProjectType = FSqrScmProjectType.none;
    }

    public int createNewReviewIndex() {
        int newReviewIndex = autoIndex.getAndIncrement();

        // TODO: some listener should be informed, that a new autoIndex is set.

        return newReviewIndex;
    }

    public String createNewReviewIdentifierWithPrefix() {
        int newReviewIndex = this.createNewReviewIndex();

        return projectReviewPrefix + Integer.toString( newReviewIndex );
    }

    public FSqrScmProjectType getScmProjectType() {
        return scmProjectType;
    }

    public void setProjectReviewPrefix( String projectReviewPrefix ) {
        this.projectReviewPrefix = projectReviewPrefix;
    }

    public String getProjectReviewPrefix() {
        return projectReviewPrefix;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectDescription( String projectDescription ) {
        this.projectDescription = projectDescription;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getProjectDisplayName() {
        return projectDisplayName;
    }

    public String getProjectUuid() {
        return projectUuid;
    }
}
