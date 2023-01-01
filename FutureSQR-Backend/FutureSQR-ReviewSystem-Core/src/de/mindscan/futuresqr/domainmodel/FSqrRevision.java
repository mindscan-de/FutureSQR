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
package de.mindscan.futuresqr.domainmodel;

import java.util.List;

/**
 * 
 */
public class FSqrRevision {

    // ATTENTION: 
    // some of these information can be recovered from the SCM, but some are only known by and part of the code review system
    // so some of these information can be dynamically indexed and be temporarily saved. But what should be done, if the
    // information is slow to retrieve like via SVN?
    // maybe just save/use a reference to the ScmBasicRevisionInformation, to distinguish these?
    // so not every information should be stored in a database entry 

    // revision identifier according to the configured scm for a project
    private String revisionId = "";

    // TODO: actually a revision should have a project id, such it can be referenced and selected during list operations
    // for a database object in a row this is a good filter criteria.

    // short revision identifier according to the configured scm for a project
    // this is for compact representation in the ui, to not provide >32 hex long strings.
    private String shortRevisionId = "";

    private String authorName = "";
    private String authorId = "";
    // calculated through user database and from SCM and authorName and Authorid
    private String authorUuid = "";

    private long revisionTimestamp = 0L;
    private String revisionDate = "";
    private String revisionRelativeDate = ""; // dynamic?

    // full commit message, or just stripped of head?
    private String commitMessageFull = "";

    // only first line of commitMessage
    private String commitMessageHead = "";

    private List<String> parentIds;
    private List<String> shortParentIds;

    private boolean hasAttachedReview = false; // dynamic?, check for empty string?
    private String reviewId = "";

    // urls to the CI-System?, to see how it built and what state?
    // or ci-references? Are those references from the ci table to the revision?
    // or should they be part of the ui joining this information, because here it is only creating overhead? 
    // actually the buildstate is nice, but not yet in focus.
}
