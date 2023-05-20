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
package de.mindscan.futuresqr.domain.repository.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 
 */
public class InMemoryCacheAlternateScmAliasTableImpl {

    // search key: ( scmAliasName:String ) -> ( systemUserId:String )
    private Map<String, String> alternateScmAliases;

    // TODO Map<String, Collection<String>> userId to ScmAliases
    // search key: ( systemUserId:String ) -> ( scmAliasName:String )

    /**
     * 
     */
    public InMemoryCacheAlternateScmAliasTableImpl() {
        this.alternateScmAliases = new HashMap<>();
    }

    public String getUserIdForScmAlias( String scmAlias ) {
        return alternateScmAliases.getOrDefault( scmAlias, scmAlias );
    }

    public String getUserIdForScmAlias( String scmAlias, Function<String, String> loader ) {
        return alternateScmAliases.computeIfAbsent( scmAlias, loader );
    }

    public void addScmAlias( String scmAlias, String systemUserId ) {
        this.alternateScmAliases.put( scmAlias, systemUserId );

        // TODO Map<String, Collection<String>> userId to ScmAliases
    }

    public void addScmAliases( List<String> scmAliases, String systemUserId ) {
        for (String scmAlias : scmAliases) {
            addScmAlias( scmAlias, systemUserId );
        }
    }

}
