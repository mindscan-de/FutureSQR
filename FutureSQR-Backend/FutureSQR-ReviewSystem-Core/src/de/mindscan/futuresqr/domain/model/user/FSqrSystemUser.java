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
package de.mindscan.futuresqr.domain.model.user;

/**
 * 
 */
public class FSqrSystemUser {
    private String avatarLocation = "";
    private String userUUID = "";
    private String userLoginName = "";
    private String userDisplayName = "";
    private String userEmail = "";

    private boolean isBanned = true;

    public FSqrSystemUser( String uuid, String loginName, String displayname, String email ) {
        this.userUUID = uuid;
        this.userLoginName = loginName;
        this.userDisplayName = displayname;
        this.userEmail = email;
    }

    public void setAvatarLocation( String avatarLocation ) {
        this.avatarLocation = avatarLocation;
    }

    public String getAvatarLocation() {
        return avatarLocation;
    }

    public void setBanned( boolean isBanned ) {
        this.isBanned = isBanned;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getUserLoginName() {
        return userLoginName;
    }
}
