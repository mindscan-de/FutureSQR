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
		// if user is in surrogate list, we return this surrogate user
		// if not we request an updated userdatabase
		// if user is not in this map, we found an unknown user and wasted resources, 
		//     so we simple create a dummy/surrogate entry for this user and return the surrogate
		// if we found this user we return this user.
		// we keep a list of all surrogate users, such that after a rerequested userdatabase those surrogate users
		
		// TODO: maybe we want to create a surrogate user in the backend (todo according to which event) 
	}
	
	

}
