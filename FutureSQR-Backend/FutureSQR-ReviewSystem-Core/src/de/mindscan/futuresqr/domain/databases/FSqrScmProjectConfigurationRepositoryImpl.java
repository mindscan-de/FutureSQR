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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;

/**
 * This provides the in-memory repository for the Source Code Management Project Configurations.
 * 
 * For performance reasons, we should load these once, because they are used the whole tiem, and 
 * use the initially provided project configurations.
 * 
 * This is where we store all the Project configurations and their type of SCM associated 
 * with this particular scm project. It provides an index of all projects and such.
 * 
 * This object should not be serializable.
 */
public class FSqrScmProjectConfigurationRepositoryImpl {

    private Map<String, FSqrScmProjectConfiguration> scmProjectConfigurationsByProjectId = new HashMap<>();

    /**
     * 
     */
    public FSqrScmProjectConfigurationRepositoryImpl() {
        // intentionally left blank
    }

    // TODO use an alternate constructor with a projectConfigurationInitialProvider, which 
    //      provides the configurations, when this class is initialized

    public Collection<FSqrScmProjectConfiguration> getAllProjectConfigurations() {
        return scmProjectConfigurationsByProjectId.values();
    }

    public FSqrScmProjectConfiguration getProjectConfiguration( String projectId ) {
        return null;
    }

    public FSqrScmProjectConfiguration getProjectConfiguration( UUID projectUUID ) {
        return null;
    }

    public boolean hasProjectConfiguration( String projectId ) {
        return scmProjectConfigurationsByProjectId.containsKey( projectId );
    }

    public void addScmProjectConfiguration( FSqrScmProjectConfiguration projectConfiguration ) {
        String projectId = projectConfiguration.getProjectId();

        if (projectId == null || projectId.trim().isEmpty()) {
            return;
        }

        scmProjectConfigurationsByProjectId.put( projectId, projectConfiguration );
    }

}
