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

import java.util.HashMap;
import java.util.Map;

import de.mindscan.futuresqr.core.uuid.UuidUtil;
import de.mindscan.futuresqr.domain.databases.FSqrScmConfigurationDatabase;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectGitAdminConfiguration;

/**
 * 
 */
public class FSqrScmConfigurationDatabaseImpl implements FSqrScmConfigurationDatabase {

    private Map<String, FSqrScmProjectConfiguration> tmpScmConfigDatabase = new HashMap<>();

    /**
     * 
     */
    public FSqrScmConfigurationDatabaseImpl() {
        // TODO: remove me, when we have a database and a database session object
        initHardcodedData();
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
        insertProjectScmConfiguration( brightFlux );

        // TODO: project is starred: false, localpath = null, 
        FSqrScmProjectConfiguration curiousMyth = new FSqrScmProjectConfiguration( "curiousmyth", "CuriousMyth", UuidUtil.getRandomUUID().toString(), 1 );
        curiousMyth.setProjectReviewPrefix( "CR-CRSM-" );
        curiousMyth.setProjectDescription( "Modelling facts, entities and information for knowledge representation" );
        insertProjectScmConfiguration( curiousMyth );

        // TODO: project is starred: false, localpath = null,
        FSqrScmProjectConfiguration fluentGenesisClassifier = new FSqrScmProjectConfiguration( "fluentgenesis-classifier", "FluentGenesis-Classifier",
                        UuidUtil.getRandomUUID().toString(), 1 );
        fluentGenesisClassifier.setProjectReviewPrefix( "CR-FLUGEN-CLS-" );
        fluentGenesisClassifier.setProjectDescription( "" );
        insertProjectScmConfiguration( fluentGenesisClassifier );

        // TODO: project is starred: false, localpath = null,
        FSqrScmProjectConfiguration fluentGenesisEmbedder = new FSqrScmProjectConfiguration( "fluentgenesis-embedder", "FluentGenesis-Embedder",
                        UuidUtil.getRandomUUID().toString(), 1 );
        fluentGenesisEmbedder.setProjectReviewPrefix( "CR-FLUGEN-EMB-" );
        fluentGenesisEmbedder.setProjectDescription( "Source Code Language Unserstanding - Calculating the Embedding vectors and such." );
        insertProjectScmConfiguration( fluentGenesisEmbedder );

        // TODO: project is starred: false, localpath = null,
        FSqrScmProjectConfiguration fluentGenesisPlugin = new FSqrScmProjectConfiguration( "fluentgenesis-plugin", "FluentGenesis-Plugin",
                        UuidUtil.getRandomUUID().toString(), 1 );
        fluentGenesisPlugin.setProjectReviewPrefix( "CR-FLUGEN-PLG-" );
        fluentGenesisPlugin.setProjectDescription( "Source Code Language Understanding - Eclipse plugin for Source Code Generation." );
        insertProjectScmConfiguration( fluentGenesisPlugin );

        // TODO: project is starred true, SCM configuration...
        FSqrScmProjectConfiguration furiousIronFrontend = new FSqrScmProjectConfiguration( "furiousiron-frontend", "FuriousIron-Frontend",
                        UuidUtil.getRandomUUID().toString(), 1 );
        furiousIronFrontend.setProjectReviewPrefix( "CR-FI-FRNT-" );
        furiousIronFrontend.setProjectDescription( "My personal source code engine project. Frontend. (Angular. TS)" );
        FSqrScmProjectGitAdminConfiguration fifconfig = new FSqrScmProjectGitAdminConfiguration();
        fifconfig.localPath = "FuriousIron-Frontend";
        fifconfig.defaultBranchName = "master";
        furiousIronFrontend.addGitConfiguration( fifconfig );
        insertProjectScmConfiguration( furiousIronFrontend );

        // TODO: furiousiron-hfb
        // TODO: furiousiron-indexer
        // TODO: furiousiron-searchbackend
        // TODO: futuresqr
        // TODO: futuresqr-svn-trunk
        // TODO: orangemoon-backend
        // TODO: orangemoon-frontend

    }

    public void insertProjectScmConfiguration( FSqrScmProjectConfiguration scmConfig ) {
        tmpScmConfigDatabase.put( scmConfig.getProjectId(), scmConfig );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmProjectConfiguration selectScmConfigurationByProjectId( String projectId ) {
        return tmpScmConfigDatabase.get( projectId );
    }

}
