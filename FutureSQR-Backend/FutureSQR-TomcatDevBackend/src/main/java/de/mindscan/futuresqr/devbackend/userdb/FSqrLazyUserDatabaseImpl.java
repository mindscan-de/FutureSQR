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
package de.mindscan.futuresqr.devbackend.userdb;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 */
public class FSqrLazyUserDatabaseImpl {

    private Gson gson = new Gson();

    private HashMap<String, FSqrUserDBEntry> userDatabaseMap = new HashMap<>();

    private Type userDatabaseMapType = new TypeToken<HashMap<String, FSqrUserDBEntry>>() {
    }.getType();

    /**
     * 
     */
    public FSqrLazyUserDatabaseImpl() {
        this.loadUserDatabaseFromResource();
    }

    public boolean hasUser( String username ) {
        Collection<FSqrUserDBEntry> values = userDatabaseMap.values();

        for (FSqrUserDBEntry fSqrUserDBEntry : values) {
            if (fSqrUserDBEntry.loginname.equals( username )) {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     */
    private void loadUserDatabaseFromResource() {
        // actually we should use the class loader to access and deal with this resource
        Path userdbPath = Paths.get( "src/main/resources/userdb/userdatabase.json" );

        try (FileReader fileReader = new FileReader( userdbPath.toAbsolutePath().toString() )) {
            this.userDatabaseMap = gson.fromJson( fileReader, userDatabaseMapType );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
