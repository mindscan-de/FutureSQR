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
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * 
 */
public class InMemoryCacheStringToAtomicIntegerImpl {

    private Map<String, AtomicInteger> stringToAtomicIntegerCache;

    /**
     * 
     */
    public InMemoryCacheStringToAtomicIntegerImpl() {
        this.stringToAtomicIntegerCache = new HashMap<>();
    }

    public boolean isCached( String key ) {
        return stringToAtomicIntegerCache.containsKey( key );
    }

    public int getValue( String key, Function<String, Integer> loader ) {
        if (isCached( key )) {
            return stringToAtomicIntegerCache.get( key ).intValue();
        }

        if (loader != null) {
            Integer value = loader.apply( key );

            if (value != null) {
                int intValue = value.intValue();
                stringToAtomicIntegerCache.put( key, new AtomicInteger( intValue ) );
                return intValue;
            }
        }

        return 0;
    }

    public void increase( String key ) {
        if (isCached( key )) {
            stringToAtomicIntegerCache.get( key ).incrementAndGet();
        }
    }

    public void decrease( String key ) {
        if (isCached( key )) {
            stringToAtomicIntegerCache.get( key ).decrementAndGet();
        }
    }

}
