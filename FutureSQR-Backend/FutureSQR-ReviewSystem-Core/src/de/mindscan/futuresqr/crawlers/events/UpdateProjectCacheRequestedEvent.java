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
package de.mindscan.futuresqr.crawlers.events;

import de.mindscan.futuresqr.core.events.FSqrEvent;

/**
 * This means that a request for updating the local project (checkout) was 
 * requested.
 * 
 * The crawler / update engine will subscribe to this event and issue an 
 * update task then execute the update.
 * 
 * UpdateProjectCacheRequestedEvent -> UpdateProjectCacheTask -> *
 */
public class UpdateProjectCacheRequestedEvent implements FSqrEvent {

    private String projectIdentifier;

    /**
     * 
     */
    public UpdateProjectCacheRequestedEvent( String projectIdentifier ) {
        this.projectIdentifier = projectIdentifier;
    }

    /**
     * @return the projectIdentifier
     */
    public String getProjectIdentifier() {
        return projectIdentifier;
    }

}
