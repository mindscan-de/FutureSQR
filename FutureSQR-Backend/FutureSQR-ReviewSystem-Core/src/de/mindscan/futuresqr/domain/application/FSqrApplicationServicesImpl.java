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
package de.mindscan.futuresqr.domain.application;

import de.mindscan.futuresqr.domain.databases.FSqrScmProjectConfigurationRepositoryImpl;
import de.mindscan.futuresqr.domain.databases.FSqrScmProjectRevisionRepositoryImpl;

/**
 * 
 */
public class FSqrApplicationServicesImpl implements FSqrApplicationServices {

    private FSqrScmProjectConfigurationRepositoryImpl configurationRepository;
    private FSqrScmProjectRevisionRepositoryImpl revisionRepository;

    /**
     * 
     */
    public FSqrApplicationServicesImpl() {
        this.configurationRepository = new FSqrScmProjectConfigurationRepositoryImpl();
        this.revisionRepository = new FSqrScmProjectRevisionRepositoryImpl();

        // we need to boot the instance
        // and then we actually need a way to provide some data from externalproviders, and then
        // finalize the boot of this application somehow.
        initializeServiceInstances( this );

        // we don't want to deal with the internals of this Review system from the outside e.g. the Web servers
    }

    void initializeServiceInstances( FSqrApplicationServices services ) {
        this.revisionRepository.setApplicationServices( services );
    }

    // TODO we want to provide some dataprovider ability, the application can ask, e.g. on restart or on demand, 
    //      when the data is not yet in memory (e.g. not cached)
    // TODO when we provide a data provider, and a data provider is not set, it will invoke the initial/essential data

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmProjectConfigurationRepositoryImpl getConfigurationRepository() {
        return this.configurationRepository;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmProjectRevisionRepositoryImpl getRevisionRepository() {
        return this.revisionRepository;
    }
}