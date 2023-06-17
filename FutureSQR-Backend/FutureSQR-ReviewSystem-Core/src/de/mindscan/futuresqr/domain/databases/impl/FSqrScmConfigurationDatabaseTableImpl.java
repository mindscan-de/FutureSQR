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
package de.mindscan.futuresqr.domain.databases.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;

import de.mindscan.futuresqr.core.uuid.UuidUtil;
import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.FSqrScmConfigurationDatabaseTable;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectGitAdminConfiguration;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectSvnAdminConfiguration;

/**
 * 
 */
public class FSqrScmConfigurationDatabaseTableImpl implements FSqrScmConfigurationDatabaseTable {

    // Tablename
    private static final String SCM_CONFIGURATION_TABLENAME = "ScmConfigurations";

    // Column names
    private static final String COLUMN_PROJECT_ID = "projectId";
    private static final String COLUMN_SCM_CONFIG_DATA = "scmConfigData";

    // SQL-Statements
    private static final String DROP_TABLE_IF_EXISTS = //
                    "DROP TABLE IF EXISTS " + SCM_CONFIGURATION_TABLENAME + ";";

    private static final String CREATE_TABLE_SCM_CONFIGURATIONS = //
                    "CREATE TABLE " + SCM_CONFIGURATION_TABLENAME + " (" + COLUMN_PROJECT_ID + ", " + COLUMN_SCM_CONFIG_DATA + "); ";

    private static final String SELECT_ALL_SCM_CONFIGURATIONS_PS = //
                    "SELECT * FROM " + SCM_CONFIGURATION_TABLENAME + " ORDER BY " + COLUMN_PROJECT_ID + ";";

    private static final String SELECT_SCM_CONFIGURATION_PS = //
                    "SELECT * FROM " + SCM_CONFIGURATION_TABLENAME + " WHERE (" + COLUMN_PROJECT_ID + "=?1); ";

    private static final String INSERT_SCM_CONFIGURATION_PS = //
                    "INSERT INTO " + SCM_CONFIGURATION_TABLENAME + " (" + COLUMN_PROJECT_ID + ", " + COLUMN_SCM_CONFIG_DATA + ") VALUES (?1, ?2); ";

    private static final String UPDATE_SCM_CONFIGURATION_PS = //
                    "UPDATE " + SCM_CONFIGURATION_TABLENAME + " SET " + COLUMN_SCM_CONFIG_DATA + "=?2 WHERE (" + COLUMN_PROJECT_ID + "=?1);";

    // 

    private Gson gson = new Gson();

    private FSqrDatabaseConnection connection;

    /**
     * 
     */
    public FSqrScmConfigurationDatabaseTableImpl() {
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setDatbaseConnection( FSqrDatabaseConnection connection ) {
        this.connection = connection;
    }

    /**
     * 
     */
    protected void initHardcodedData() {
        // insertProjectScmConfiguration( scmConfig );

        // TODO: project is starred: false, localpath = null, 
        FSqrScmProjectConfiguration brightFlux = new FSqrScmProjectConfiguration( "brightflux", "BrightFlux", UuidUtil.getRandomUUID().toString(), 1 );
        brightFlux.setProjectReviewPrefix( "CR-BRFX" );
        brightFlux.setProjectDescription( "LogFileViewer and LogFileAnalysis with yet unseen Features and written in Java" );
        brightFlux.setProjectRemoteRepoURL( "https://github.com/mindscan-de/BrightFlux.git" );
        insertProjectScmConfiguration( brightFlux );

        // TODO: project is starred: false, localpath = null, 
        FSqrScmProjectConfiguration curiousMyth = new FSqrScmProjectConfiguration( "curiousmyth", "CuriousMyth", UuidUtil.getRandomUUID().toString(), 1 );
        curiousMyth.setProjectReviewPrefix( "CR-CRSM-" );
        curiousMyth.setProjectDescription( "Modelling facts, entities and information for knowledge representation" );
        curiousMyth.setProjectRemoteRepoURL( "https://github.com/mindscan-de/CuriousMyth.git" );
        insertProjectScmConfiguration( curiousMyth );

        // TODO: project is starred: false, localpath = null,
        FSqrScmProjectConfiguration fluentGenesisClassifier = new FSqrScmProjectConfiguration( "fluentgenesis-classifier", "FluentGenesis-Classifier",
                        UuidUtil.getRandomUUID().toString(), 1 );
        fluentGenesisClassifier.setProjectReviewPrefix( "CR-FLUGEN-CLS-" );
        fluentGenesisClassifier.setProjectDescription( "" );
        fluentGenesisClassifier.setProjectRemoteRepoURL( "https://github.com/mindscan-de/FluentGenesis-Classifier.git" );
        insertProjectScmConfiguration( fluentGenesisClassifier );

        // TODO: project is starred: false, localpath = null,
        FSqrScmProjectConfiguration fluentGenesisEmbedder = new FSqrScmProjectConfiguration( "fluentgenesis-embedder", "FluentGenesis-Embedder",
                        UuidUtil.getRandomUUID().toString(), 1 );
        fluentGenesisEmbedder.setProjectReviewPrefix( "CR-FLUGEN-EMB-" );
        fluentGenesisEmbedder.setProjectDescription( "Source Code Language Unserstanding - Calculating the Embedding vectors and such." );
        fluentGenesisEmbedder.setProjectRemoteRepoURL( "https://github.com/mindscan-de/FluentGenesis-Embedder.git" );
        insertProjectScmConfiguration( fluentGenesisEmbedder );

        // TODO: project is starred: false, localpath = null,
        FSqrScmProjectConfiguration fluentGenesisPlugin = new FSqrScmProjectConfiguration( "fluentgenesis-plugin", "FluentGenesis-Plugin",
                        UuidUtil.getRandomUUID().toString(), 1 );
        fluentGenesisPlugin.setProjectReviewPrefix( "CR-FLUGEN-PLG-" );
        fluentGenesisPlugin.setProjectDescription( "Source Code Language Understanding - Eclipse plugin for Source Code Generation." );
        fluentGenesisPlugin.setProjectRemoteRepoURL( "https://github.com/mindscan-de/FluentGenesis-Plugin.git" );
        insertProjectScmConfiguration( fluentGenesisPlugin );

        // TODO: project is starred true
        FSqrScmProjectConfiguration furiousIronFrontend = new FSqrScmProjectConfiguration( "furiousiron-frontend", "FuriousIron-Frontend",
                        UuidUtil.getRandomUUID().toString(), 1 );
        furiousIronFrontend.setProjectReviewPrefix( "CR-FI-FRNT-" );
        furiousIronFrontend.setProjectDescription( "My personal source code engine project. Frontend. (Angular. TS)" );
        furiousIronFrontend.setProjectRemoteRepoURL( "https://github.com/mindscan-de/FuriousIron-Frontend.git" );
        FSqrScmProjectGitAdminConfiguration fifconfig = new FSqrScmProjectGitAdminConfiguration();
        fifconfig.localPath = "FuriousIron-Frontend";
        fifconfig.defaultBranchName = "master";
        furiousIronFrontend.addGitConfiguration( fifconfig );
        insertProjectScmConfiguration( furiousIronFrontend );

        // TODO: project is starred true
        FSqrScmProjectConfiguration furiousIronHFB = new FSqrScmProjectConfiguration( "furiousiron-hfb", "FuriousIron-HFB", UuidUtil.getRandomUUID().toString(),
                        50 );
        furiousIronHFB.setProjectReviewPrefix( "CR-FI-HFB-" );
        furiousIronHFB.setProjectDescription( "Hash-Free Bloom-Filter (Proof of concept implementation)" );
        furiousIronHFB.setProjectRemoteRepoURL( "https://github.com/mindscan-de/FuriousIron-HFB.git" );
        FSqrScmProjectGitAdminConfiguration fihconfig = new FSqrScmProjectGitAdminConfiguration();
        fihconfig.localPath = "FuriousIron-HFB";
        fihconfig.defaultBranchName = "main";
        furiousIronHFB.addGitConfiguration( fihconfig );
        insertProjectScmConfiguration( furiousIronHFB );

        // TODO: project is starred: false, localpath = null,
        FSqrScmProjectConfiguration furiousIronIndexer = new FSqrScmProjectConfiguration( "furiousiron-indexer", "FuriousIron-Indexer",
                        UuidUtil.getRandomUUID().toString(), 1 );
        furiousIronIndexer.setProjectReviewPrefix( "CR-FI-NDX-" );
        furiousIronIndexer.setProjectDescription( "My personal source code search engine project. Indexer. (Java. Windows. No Database. Filesystem only)" );
        furiousIronIndexer.setProjectRemoteRepoURL( "https://github.com/mindscan-de/FuriousIron-Indexer.git" );
        insertProjectScmConfiguration( furiousIronIndexer );

        // TODO: project is starred: false, localpath = null,
        FSqrScmProjectConfiguration furiousIronSearchBackend = new FSqrScmProjectConfiguration( "furiousiron-searchbackend", "FuriousIron-SearchBackend",
                        UuidUtil.getRandomUUID().toString(), 1 );
        furiousIronSearchBackend.setProjectReviewPrefix( "CR-FI-SRND-" );
        furiousIronSearchBackend.setProjectDescription(
                        "My personal source code search engine project. Backend. (Java. Tomcat. Windows. No Database. Filesystem only)" );
        furiousIronSearchBackend.setProjectRemoteRepoURL( "https://github.com/mindscan-de/FuriousIron-SearchBackend.git" );
        insertProjectScmConfiguration( furiousIronSearchBackend );

        // TODO: project is starred: true,
        FSqrScmProjectConfiguration futureSqr = new FSqrScmProjectConfiguration( "futuresqr", "FutureSQR", UuidUtil.getRandomUUID().toString(), 100 );
        futureSqr.setProjectReviewPrefix( "CR-FSQR-" );
        futureSqr.setProjectDescription( "Future Source Quality Review -- Code Review Tool for Trunk-Based-Development" );
        futureSqr.setProjectRemoteRepoURL( "https://github.com/mindscan-de/FutureSQR.git" );
        FSqrScmProjectGitAdminConfiguration fsqrconfig = new FSqrScmProjectGitAdminConfiguration();
        fsqrconfig.localPath = "FutureSQR";
        fsqrconfig.defaultBranchName = "main";
        futureSqr.addGitConfiguration( fsqrconfig );
        insertProjectScmConfiguration( futureSqr );

        // TODO: project is starred: true,
        FSqrScmProjectConfiguration futureSqrSvnTrunk = new FSqrScmProjectConfiguration( "futuresqr-svn-trunk", "FutureSQR (SVN)",
                        UuidUtil.getRandomUUID().toString(), 100 );
        futureSqrSvnTrunk.setProjectReviewPrefix( "CR-FSQR-SVN-" );
        futureSqrSvnTrunk.setProjectDescription(
                        "Future Source Quality Review -- Code Review Tool for Trunk-Based-Development -- https://github.com/mindscan-de/FutureSQR.git/trunk" );
        futureSqrSvnTrunk.setProjectRemoteRepoURL( "https://github.com/mindscan-de/FutureSQR.git/trunk" );
        FSqrScmProjectSvnAdminConfiguration fsqrsvnconfig = new FSqrScmProjectSvnAdminConfiguration();
        // TODO: fsqrsvnconfig.  "localPath": "FutureSQR_SVN_TrunkOnly"
        // TODO: fsqrsvnconfig.  "branchname": "main"
        futureSqrSvnTrunk.addSvnConfiguration( fsqrsvnconfig );
        insertProjectScmConfiguration( futureSqrSvnTrunk );

        // TODO: project is starred: false, localpath = null,
        FSqrScmProjectConfiguration orangeMoonBackend = new FSqrScmProjectConfiguration( "orangemoon-backend", "OrangeMoon-Backend",
                        UuidUtil.getRandomUUID().toString(), 1 );
        orangeMoonBackend.setProjectReviewPrefix( "CR-ORM-BND-" );
        orangeMoonBackend.setProjectDescription( "Japanese Dictionary Web-App - Backend (based on fastapi and jamdict)" );
        orangeMoonBackend.setProjectRemoteRepoURL( "https://github.com/mindscan-de/OrangeMoon-Backend.git" );
        insertProjectScmConfiguration( orangeMoonBackend );

        // TODO: project is starred: false, localpath = null,
        FSqrScmProjectConfiguration orangeMoonFrontend = new FSqrScmProjectConfiguration( "orangemoon-frontend", "OrangeMoon-Frontend",
                        UuidUtil.getRandomUUID().toString(), 1 );
        orangeMoonFrontend.setProjectReviewPrefix( "CR-ORM-FND-" );
        orangeMoonFrontend.setProjectDescription( "Japanese Dictionary Web-App - Frontend (based on nodejs and angular)" );
        orangeMoonFrontend.setProjectRemoteRepoURL( "https://github.com/mindscan-de/OrangeMoon-Frontend.git" );
        insertProjectScmConfiguration( orangeMoonFrontend );

        FSqrScmProjectConfiguration stableDiffusionWiki = new FSqrScmProjectConfiguration( "stable-diffusion-webui-wiki", "Stable Diffusion WebUI Wiki",
                        UuidUtil.getRandomUUID().toString(), 1 );
        stableDiffusionWiki.setProjectDescription( "Wiki of the stable diffusion WebUI." );
        stableDiffusionWiki.setProjectReviewPrefix( "CR-SDW-" );
        stableDiffusionWiki.setProjectRemoteRepoURL( "https://github.com/AUTOMATIC1111/stable-diffusion-webui.wiki.git" );
        FSqrScmProjectGitAdminConfiguration sdwuiwikiconfig = new FSqrScmProjectGitAdminConfiguration();
        sdwuiwikiconfig.localPath = "stable-diffusion-webui.wiki";
        sdwuiwikiconfig.defaultBranchName = "master";
        stableDiffusionWiki.addGitConfiguration( sdwuiwikiconfig );
        insertProjectScmConfiguration( stableDiffusionWiki );

        FSqrScmProjectConfiguration stableDiffusion = new FSqrScmProjectConfiguration( "stable-diffusion-webui", "Stable Diffusion WebUI",
                        UuidUtil.getRandomUUID().toString(), 1 );
        stableDiffusion.setProjectDescription( "Stable Diffusion web UI" );
        stableDiffusion.setProjectReviewPrefix( "CR_SD-" );
        stableDiffusion.setProjectRemoteRepoURL( "https://github.com/AUTOMATIC1111/stable-diffusion-webui.git" );
        FSqrScmProjectGitAdminConfiguration sdwuiconfig = new FSqrScmProjectGitAdminConfiguration();
        sdwuiconfig.localPath = "stable-diffusion-webui";
        sdwuiwikiconfig.defaultBranchName = "master";
        stableDiffusion.addGitConfiguration( sdwuiconfig );
        insertProjectScmConfiguration( stableDiffusion );

    }

    @Override
    public void insertProjectScmConfiguration( FSqrScmProjectConfiguration scmConfig ) {
        try {
            String serializedScmConfiguration = gson.toJson( scmConfig );

            PreparedStatement insert = this.connection.createPreparedStatement( INSERT_SCM_CONFIGURATION_PS );

            insert.setString( 1, scmConfig.getProjectId() );
            insert.setString( 2, serializedScmConfiguration );

            // 
            insert.addBatch();
            insert.executeBatch();

            this.connection.finishTransaction();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProjectScmConfiguration( FSqrScmProjectConfiguration scmConfig ) {
        if (scmConfig == null) {
            return;
        }

        try {
            String serializedScmConfiguration = gson.toJson( scmConfig );

            PreparedStatement update = this.connection.createPreparedStatement( UPDATE_SCM_CONFIGURATION_PS );

            // WHERE
            update.setString( 1, scmConfig.getProjectId() );

            // SET
            update.setString( 2, serializedScmConfiguration );

            // EXECUTE
            update.addBatch();
            update.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmProjectConfiguration selectScmConfigurationByProjectId( String projectId ) {
        FSqrScmProjectConfiguration result = null;

        try {
            PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_SCM_CONFIGURATION_PS );

            selectPS.setString( 1, projectId );

            ResultSet resultSet = selectPS.executeQuery();
            if (resultSet.next()) {
                result = createScmConfiguration( resultSet );
            }
            resultSet.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    private FSqrScmProjectConfiguration createScmConfiguration( ResultSet resultSet ) throws Exception {
        String configDataString = resultSet.getString( COLUMN_SCM_CONFIG_DATA );

        return gson.fromJson( configDataString, FSqrScmProjectConfiguration.class );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Collection<FSqrScmProjectConfiguration> selectAllScmConfigurations() {
        ArrayList<FSqrScmProjectConfiguration> resultList = new ArrayList<>();

        try {
            PreparedStatement selectPS = this.connection.createPreparedStatement( SELECT_ALL_SCM_CONFIGURATIONS_PS );

            ResultSet resultSet = selectPS.executeQuery();
            while (resultSet.next()) {
                resultList.add( createScmConfiguration( resultSet ) );
            }
            resultSet.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return resultList;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void createTable() {
        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate( DROP_TABLE_IF_EXISTS );
            statement.executeUpdate( CREATE_TABLE_SCM_CONFIGURATIONS );

            // initialize the application database with some hard coded data 
            initHardcodedData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        // intentionally left blank for now.
    }

}
