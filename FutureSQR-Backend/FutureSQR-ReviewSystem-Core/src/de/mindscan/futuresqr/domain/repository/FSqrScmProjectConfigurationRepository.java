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
package de.mindscan.futuresqr.domain.repository;

import java.util.Collection;

import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;

/**
 * 
 * 
 * TODO: think about useful extensions to the configuration repository. Will be extended as soon as there is enough stuff what can be edited.
 *    
 * TODO: archive project, it will disable the project and will no longer track the updates.
 * TODO: also we want to update the scm configuration git/svn/local cache, we want to configure this 
 */
public interface FSqrScmProjectConfigurationRepository {

    // retrieve/get configuration

    boolean hasProjectConfiguration( String projectId );

    FSqrScmProjectConfiguration getProjectConfiguration( String projectId );

    Collection<FSqrScmProjectConfiguration> getAllProjectConfigurations();

    // insert project configuration

    void addScmProjectConfiguration( FSqrScmProjectConfiguration projectConfiguration );

    // get review identifier, is this required here?

    String getNewProjectReviewIdentifier( String projectId );

}
