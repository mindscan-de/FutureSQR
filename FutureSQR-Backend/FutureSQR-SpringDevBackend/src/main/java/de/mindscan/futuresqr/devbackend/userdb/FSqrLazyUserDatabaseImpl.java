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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
public class FSqrLazyUserDatabaseImpl {

	private HashMap<String, FSqrLazyUserDBEntry> userDatabaseMap = new HashMap<>();

	private TypeReference<HashMap<String, FSqrLazyUserDBEntry>> userDatabaseMapType = new TypeReference<HashMap<String, FSqrLazyUserDBEntry>>() {
	};

	/**
	 * 
	 */
	public FSqrLazyUserDatabaseImpl() {
		this.loadUserDatabaseFromResource();
	}

	/**
	 * 
	 */
	private void loadUserDatabaseFromResource() {
		// actually we should use the class loader to access and deal with this resource
		Path userdbPath = Paths.get("src/main/resources/userdb/userdatabase.json");

		ObjectMapper objectMapper = new ObjectMapper();
		try (FileReader fileReader = new FileReader(userdbPath.toAbsolutePath().toString())) {
			this.userDatabaseMap = objectMapper.readValue(fileReader, userDatabaseMapType);
		} catch (IOException e) {
			System.err.println("could not find userdatabase...");
			e.printStackTrace();

			ClassLoader cl = this.getClass().getClassLoader();
			System.err.println(cl.getResource("userdb/userdatabase.json"));
			try (InputStream is = cl.getResourceAsStream("userdb/userdatabase.json");
					Reader isr = new InputStreamReader(is)) {
				this.userDatabaseMap = objectMapper.readValue(isr, userDatabaseMapType);
			} catch (Exception ex) {
				System.err.println("yould not access alternate userdatabase");
				ex.printStackTrace();
			}
		}

	}

	public boolean hasUser(String username) {
		for (FSqrLazyUserDBEntry fSqrUserDBEntry : userDatabaseMap.values()) {
			if (fSqrUserDBEntry.loginname.equals(username)) {
				return true;
			}
		}
		return false;
	}

	public FSqrLazyUserDBEntry getUserEntryByLogonName(String username) {
		for (FSqrLazyUserDBEntry fSqrUserDBEntry : userDatabaseMap.values()) {
			if (fSqrUserDBEntry.loginname.equals(username)) {
				return fSqrUserDBEntry;
			}
		}
		return null;
	}

	public FSqrLazyUserDBEntry getUserEntryByUUID(String uuid) {
		if (hasUserEntryByUUID(uuid)) {
			return userDatabaseMap.get(uuid);
		}
		return null;
	}

	private boolean hasUserEntryByUUID(String uuid) {
		return userDatabaseMap.containsKey(uuid);
	}

}
