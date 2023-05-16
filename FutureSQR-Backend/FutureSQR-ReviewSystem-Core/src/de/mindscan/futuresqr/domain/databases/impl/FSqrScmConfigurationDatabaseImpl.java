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
        FSqrScmProjectConfiguration brightflux = new FSqrScmProjectConfiguration( "brightflux", "BrightFlux", UuidUtil.getRandomUUID().toString(), 1 );
        brightflux.setProjectReviewPrefix( "CR-BRFX" );
        brightflux.setProjectDescription( "LogFileViewer and LogFileAnalysis with yet unseen Features and written in Java" );
        insertProjectScmConfiguration( brightflux );

        // TODO: project is starred: false, localpath = null, 
        FSqrScmProjectConfiguration curiousmyth = new FSqrScmProjectConfiguration( "curiousmyth", "CuriousMyth", UuidUtil.getRandomUUID().toString(), 1 );
        curiousmyth.setProjectReviewPrefix( "CR-CRSM-" );
        curiousmyth.setProjectDescription( "Modelling facts, entities and information for knowledge representation" );
        insertProjectScmConfiguration( curiousmyth );

        // TODO: fluentgenesis-classifier
        FSqrScmProjectConfiguration fluentgenesis_classifier = new FSqrScmProjectConfiguration( "fluentgenesis-classifier", "FluentGenesis-Classifier",
                        UuidUtil.getRandomUUID().toString(), 1 );
        fluentgenesis_classifier.setProjectReviewPrefix( "CR-FLUGEN-CLS-" );
        fluentgenesis_classifier.setProjectDescription( "" );
        insertProjectScmConfiguration( fluentgenesis_classifier );

        // TODO: fluentgenesis-embedder
        // TODO: fluentgenesis-plugin
        // TODO: furiousiron-frontend
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
