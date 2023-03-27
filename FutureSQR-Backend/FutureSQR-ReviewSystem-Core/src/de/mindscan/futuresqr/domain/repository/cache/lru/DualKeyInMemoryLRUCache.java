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
package de.mindscan.futuresqr.domain.repository.cache.lru;

import java.util.function.BiFunction;

/**
 * &lt;T&gt; the type of the master key to the cache
 * &lt;U&gt; the type of the sub key argument to the cache
 * &lt;R&gt; the type of the cached type of the cache
 */
public class DualKeyInMemoryLRUCache<T, U, R> {

    /**
     * 
     */
    public DualKeyInMemoryLRUCache() {
        this( 2048 );
    }

    /**
     * 
     */
    public DualKeyInMemoryLRUCache( int lruSize ) {
        // intentionally left blank
    }

    public boolean isCached( T masterKey, U subKey ) {
        return false;
    }

    public R get( T masterKey, U subKey ) {
        if (isCached( masterKey, subKey )) {

        }
        return null;
    }

    public void put( T masterKey, U subKey, R value ) {

    }

    public R computeIfAbsent( T masterKey, U subKey, BiFunction<T, U, R> computeFunction ) {
        return null;
    }

}
