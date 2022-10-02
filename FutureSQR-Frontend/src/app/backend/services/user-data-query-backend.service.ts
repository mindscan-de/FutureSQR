import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserDataQueryBackendService {

	constructor() { }

	public getSimpleUserDataMap() {
		// this map should be requested 
		// check if we already have this database downloaded.
	}

	/**
	 * This will return a user object (consisting of uuid, displayname and avatarlocation, an whether this user is banned)
	 * we will need this kind of translation every time we request a page, where a user is
     * referenced by a uuid.
	 */
	public getSimpleUserObject(uuid : string) {
		// we use our cached copy od the userdatabase orwe have to request the simple user database once
		// if we found this user, we return the user
		// check if this username is on our blacklist -> return nothing
		// if this user is not on our blacklist, we rerequest the usermap
				
	}
	

}
