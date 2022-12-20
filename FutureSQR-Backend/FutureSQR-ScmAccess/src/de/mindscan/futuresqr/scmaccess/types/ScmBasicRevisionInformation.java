/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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
package de.mindscan.futuresqr.scmaccess.types;

import java.util.List;

/**
 * For context: backend-model-project-recent-commit-revision.ts
 */
public class ScmBasicRevisionInformation {

    String shortRevisionId = "";
    String revisionId = "";

    String authorName = "";
    String authorId = "";
    String authorUuid = ""; // maybe this is actually part of the review system and should be handled by the review system

    String date = ""; // this should be a long - timestamp (UTC), to be more efficient - Java and dates are a pain ...
    String shortDate = "";
    String relDate = "";

    String message = "";

    List<String> parentIds;
    List<String> shortParentIds;

    // Should not be part of the basic SCM Information, but something the review system provides as extra data.
    boolean hasReview = false;
    String reviewID = "";

}
