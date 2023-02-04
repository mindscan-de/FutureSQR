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
package de.mindscan.futuresqr.devbackend.projectdb;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.mindscan.futuresqr.domain.application.FSqrApplication;
import de.mindscan.futuresqr.domain.databases.FSqrScmProjectConfigurationRepositoryImpl;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectGitAdminConfiguration;

/**
 * 
 */
public class FSqrLazyProjectDatabaseImpl {

    private Gson gson = new Gson();

    private FSqrScmProjectConfigurationRepositoryImpl configurationRepository = FSqrApplication.getInstance().getServices().getConfigurationRepository();

    /**
     * 
     */
    public FSqrLazyProjectDatabaseImpl() {
        this.loadProjectDatabaseFromResource();
    }

    private void loadProjectDatabaseFromResource() {

        HashMap<String, FSqrLazyProjectDBEntry> projectConfigurationMap = new HashMap<>();

        Type projectConfigurationMapType = new TypeToken<HashMap<String, FSqrLazyProjectDBEntry>>() {
        }.getType();

        // actually we should use the class loader to access and deal with this resource
        Path userdbPath = Paths.get( "src/main/resources/projectdb/projectdatabase.json" );

        try (FileReader fileReader = new FileReader( userdbPath.toAbsolutePath().toString() )) {
            projectConfigurationMap = gson.fromJson( fileReader, projectConfigurationMapType );
        }
        catch (IOException e) {
            System.err.println( "could not find project database..." );
            e.printStackTrace();

            ClassLoader cl = this.getClass().getClassLoader();
            System.err.println( cl.getResource( "projectdb/projectdatabase.json" ) );
            try (InputStream is = cl.getResourceAsStream( "userdb/userdatabase.json" ); Reader isr = new InputStreamReader( is )) {
                projectConfigurationMap = gson.fromJson( isr, projectConfigurationMapType );
            }
            catch (Exception ex) {
                System.err.println( "yould not access alternate project database" );
                ex.printStackTrace();
            }
        }

        this.initializeScmProjectConfigurationRepository( projectConfigurationMap.values() );
    }

    private void initializeScmProjectConfigurationRepository( Collection<FSqrLazyProjectDBEntry> collection ) {
        for (FSqrLazyProjectDBEntry projectEntry : collection) {

            String projectId = projectEntry.projectID;
            String displayName = projectEntry.projectDisplayName;
            String projectuuid = projectEntry.projectUuid;
            int autoindexstart = projectEntry.autoIndex;

            FSqrScmProjectConfiguration scmProjectConfig = new FSqrScmProjectConfiguration( projectId, displayName, projectuuid, autoindexstart );
            scmProjectConfig.setProjectReviewPrefix( projectEntry.reviewPrefix );
            scmProjectConfig.setProjectDescription( projectEntry.projectDescription );

            // TODO: read git configuration and prepare the GIT SCM configuration / later also SVN SCM configuration
            if (projectEntry.hasAdministrationData()) {
                FSqrLazyProjectAdministrationEntry adminentry = projectEntry.administration;
                if (adminentry.hasLocalPath()) {
                    if ("svn".equals( adminentry.scmBackend )) {
                        // 
                        // scmProjectConfig.addSvnConfiguration();
                    }
                    else {
                        FSqrScmProjectGitAdminConfiguration gitAdminConfig = new FSqrScmProjectGitAdminConfiguration();

                        gitAdminConfig.localPath = adminentry.localPath;
                        gitAdminConfig.defaultBranchName = projectEntry.projectBranchName;

                        scmProjectConfig.addGitConfiguration( gitAdminConfig );
                    }

                }
            }

            configurationRepository.addScmProjectConfiguration( scmProjectConfig );
        }
    }

    public FSqrScmProjectConfiguration getProjectConfiguration( String projectId ) {
        return configurationRepository.getProjectConfiguration( projectId );
    }

    public Collection<FSqrScmProjectConfiguration> getAllProjects() {
        return configurationRepository.getAllProjectConfigurations();
    }

    public boolean isProjectIdPresent( String projectId ) {
        return configurationRepository.hasProjectConfiguration( projectId );
    }

    public boolean hasProjectLocalPath( String projectId ) {
        if (!isProjectIdPresent( projectId )) {
            return false;
        }

        return true;
        // return getProjectConfiguration( projectId ).hasLocalRepoPath();
    }

}
