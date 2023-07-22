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

import java.util.ArrayList;
import java.util.List;
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

    private String projectRemoteRepoURL;

    private FSqrScmProjectType scmProjectType;

    // that means that this project is not active any more, and is not monitored any more, with respect to the SCM
    private boolean isArchived = false;

    private int numberOfStars;

    // TODO: here? defaultbranch - indiscriminate whether GIT or SVN we will have some kind of branches
    // trunk for svn -> url
    // branchname for svn -> url
    private String projectDefaultBranch = "";

    // TODO: here? branches  - indiscriminate whether GIT or SVN we will have some kind of branches
    // a branch should be translated depending on SCM
    // TODO: get a branch configuration???
    private List<String> branches = new ArrayList<>();

    // so maybe this should be available here.

    // TODO: some details, like ownership (who created, when created, when modified)
    // i guess this is not yet important, and can alo be kept in a kind of journal or so....
    private FSqrScmProjectGitAdminConfiguration scmGitAdminConfiguration;

    // TODO: latest date updates for this project - or separate other dates. 

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

    public boolean isScmProjectType( FSqrScmProjectType projectType ) {
        return scmProjectType == projectType;
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

    public boolean hasLocalRepoPath() {
        if (scmProjectType == FSqrScmProjectType.none) {
            return false;
        }

        if (scmProjectType == FSqrScmProjectType.git) {
            if (this.scmGitAdminConfiguration != null) {
                return this.scmGitAdminConfiguration.hasLocalPath();
            }
            return false;
        }

        return false;
    }

    public void addGitConfiguration( FSqrScmProjectGitAdminConfiguration gitAdminConfig ) {
        this.scmProjectType = FSqrScmProjectType.git;
        this.scmGitAdminConfiguration = gitAdminConfig;
        this.projectDefaultBranch = this.scmGitAdminConfiguration.getDefaultBranchName();
    }

    public void addSvnConfiguration( FSqrScmProjectSvnAdminConfiguration svnAdminConfig ) {
        this.scmProjectType = FSqrScmProjectType.svn;
        // TODO: set scmSvnAdminConfiguration = svnAdminConfig;
    }

    public FSqrScmProjectGitAdminConfiguration getScmGitAdminConfiguration() {
        return scmGitAdminConfiguration;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public String getProjectDefaultBranch() {
        if (projectDefaultBranch.isEmpty()) {
            // recovery mode, because configurations saved in database.
            switch (scmProjectType) {
                case git:
                    return scmGitAdminConfiguration.getDefaultBranchName();

                default:
                    break;
            }
        }

        return projectDefaultBranch;
    }

    // hide this as a method from api - will figure out, whether this is needed 
    void setProjectDefaultBranch( String projectDefaultBranch ) {
        this.projectDefaultBranch = projectDefaultBranch;
    }

    public List<String> getBranches() {
        return new ArrayList<>( branches );
    }

    public void setProjectRemoteRepoURL( String projectRemoteRepoURL ) {
        this.projectRemoteRepoURL = projectRemoteRepoURL;
    }

    public String getProjectRemoteRepoURL() {
        return projectRemoteRepoURL;
    }

    public boolean hasProjectRemoteRepoURL() {
        return !(this.projectRemoteRepoURL == null || this.projectRemoteRepoURL.isEmpty());
    }

    public void setNumberOfStars( int numberOfStarsForProject ) {
        this.numberOfStars = numberOfStarsForProject;
    }

    public int getNumberOfStars() {
        return numberOfStars;
    }

}
