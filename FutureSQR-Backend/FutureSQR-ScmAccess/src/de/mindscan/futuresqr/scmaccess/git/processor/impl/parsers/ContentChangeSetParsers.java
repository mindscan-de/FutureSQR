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
package de.mindscan.futuresqr.scmaccess.git.processor.impl.parsers;

import de.mindscan.futuresqr.scmaccess.types.ScmFileContentChangeSet;

/**
 * 
 */
public class ContentChangeSetParsers {

    private static final String LINE_INFO_MARKER = "@@";

    public static void parseLineInfoIntoContentChangeSet( String lineInfo, ScmFileContentChangeSet contentChangeset ) {
        // setup some default values, in case we run into parsing errors.
        contentChangeset.diffLeftLineCountStart = 1;
        contentChangeset.diffLeftLineCountDelta = 0;
        contentChangeset.diffRightLineCountStart = 1;
        contentChangeset.diffRightLineCountDelta = 0;

        String[] lineInfoSplitted = lineInfo.split( LINE_INFO_MARKER );

        // TODO: in case that the lineInfoSplitted contains a third item, we want to save that as a locator / context info.

        if (lineInfoSplitted == null || lineInfoSplitted.length < 2) {
            return;
        }

        String[] lineNumberDataLR = lineInfoSplitted[1].trim().split( " " );

        if (lineNumberDataLR.length != 2) {
            return;
        }

        String[] diffLeftLineData = lineNumberDataLR[0].trim().split( "[+,\\-]" );
        if (diffLeftLineData.length >= 2) {
            if (diffLeftLineData.length >= 2) {
                try {
                    contentChangeset.diffLeftLineCountStart = Integer.parseInt( diffLeftLineData[1] );
                }
                catch (Exception e) {
                }
            }
            if (diffLeftLineData.length >= 3) {
                try {
                    contentChangeset.diffLeftLineCountDelta = Integer.parseInt( diffLeftLineData[2] );
                }
                catch (Exception e) {
                }
            }
        }

        String[] diffRightLineData = lineNumberDataLR[1].trim().split( "[+,\\-]" );
        if (diffRightLineData.length >= 2) {
            if (diffRightLineData.length >= 2) {
                try {
                    contentChangeset.diffRightLineCountStart = Integer.parseInt( diffRightLineData[1] );
                }
                catch (Exception e) {
                }
            }

            if (diffRightLineData.length >= 3) {
                try {
                    contentChangeset.diffRightLineCountDelta = Integer.parseInt( diffRightLineData[2] );
                }
                catch (Exception e) {
                }
            }
        }

    }

}
