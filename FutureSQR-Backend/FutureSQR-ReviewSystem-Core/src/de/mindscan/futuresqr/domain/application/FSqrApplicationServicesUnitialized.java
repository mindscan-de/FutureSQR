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

import de.mindscan.futuresqr.domain.configuration.FSqrSystemInstanceConfiguration;
import de.mindscan.futuresqr.domain.repository.FSqrCodeReviewRepository;
import de.mindscan.futuresqr.domain.repository.FSqrDiscussionThreadRepository;
import de.mindscan.futuresqr.domain.repository.FSqrScmProjectConfigurationRepository;
import de.mindscan.futuresqr.domain.repository.FSqrScmProjectRevisionRepository;
import de.mindscan.futuresqr.domain.repository.FSqrScmRepositoryServices;
import de.mindscan.futuresqr.domain.repository.FSqrScmUserRepository;
import de.mindscan.futuresqr.domain.repository.FSqrUserToProjectRepository;

/**
 * 
 */
public class FSqrApplicationServicesUnitialized implements FSqrApplicationServices {

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmProjectConfigurationRepository getConfigurationRepository() {
        throw new RuntimeException( "Please initialize Application Services first before using this method. See Stack." );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmProjectRevisionRepository getRevisionRepository() {
        throw new RuntimeException( "Please initialize Application Services first before using this method. See Stack." );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmUserRepository getUserRepository() {
        throw new RuntimeException( "Please initialize Application Services first before using this method. See Stack." );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrCodeReviewRepository getReviewRepository() {
        throw new RuntimeException( "Please initialize Application Services first before using this method. See Stack." );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrSystemInstanceConfiguration getSystemConfiguration() {
        throw new RuntimeException( "Please initialize Application Services first before using this method. See Stack." );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrUserToProjectRepository getUserToProjectRepository() {
        throw new RuntimeException( "Please initialize Application Services first before using this method. See Stack." );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrDiscussionThreadRepository getDiscussionThreadRepository() {
        throw new RuntimeException( "Please initialize Application Services first before using this method. See Stack." );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public FSqrScmRepositoryServices getFSqrScmRepositoryServices() {
        throw new RuntimeException( "Please initialize Application Services first before using this method. See Stack." );
    }
}
