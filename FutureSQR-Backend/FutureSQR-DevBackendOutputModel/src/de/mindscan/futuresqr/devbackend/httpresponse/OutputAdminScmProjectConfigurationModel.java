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
package de.mindscan.futuresqr.devbackend.httpresponse;

import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;

/**
 * 
 */
public class OutputAdminScmProjectConfigurationModel {

    // TODO: rework this type, to accomodate for 'svn' and 'git' branches and urls either for branches etc.
    //     : this is not yet useful enough / will work this out later, when the database stuff is implemented
    //     : and when we need a svn and git crawler.

    public String projectDisplayName = "";
    public String projectDescription = "";
    public String projectId = "";
    public String projectReviewPrefix = "";
    public String projectUuid = "";
    public String projectRemoteRepositoryURL = "";
    public String scmProjectType = "";
    public boolean hasProjectRemoteReporitoryURL = false;

    // TODO: git configuration - but will still be useless...
    // TODO: svn configuration - but will still be useless...

    public OutputAdminScmProjectConfigurationModel( FSqrScmProjectConfiguration projectConfiguration ) {
        this.projectDescription = projectConfiguration.getProjectDescription();
        this.projectDisplayName = projectConfiguration.getProjectDisplayName();
        this.projectId = projectConfiguration.getProjectId();
        this.projectReviewPrefix = projectConfiguration.getProjectReviewPrefix();
        this.projectUuid = projectConfiguration.getProjectUuid();
        this.scmProjectType = projectConfiguration.getScmProjectType().toString();
        this.hasProjectRemoteReporitoryURL = projectConfiguration.hasProjectRemoteRepoURL();
        if (hasProjectRemoteReporitoryURL) {
            this.projectRemoteRepositoryURL = projectConfiguration.getProjectRemoteRepoURL();
        }
        else {
            this.projectRemoteRepositoryURL = "";
        }
    }

}
