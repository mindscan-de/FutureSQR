import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { map, first } from 'rxjs/operators';

import { BackendModelSimpleUserDictionary } from '../model/backend-model-simple-user-dictionary';


@Injectable({
  providedIn: 'root'
})
export class UserDataQueryBackendService {
	
	private static readonly URL_GET_ALL_USERS_AS_DICT:string           = "/FutureSQR/rest/user/userdictionary";

	constructor(
		private httpClient : HttpClient
	) { }

	// creates a shadow copy of the simple userdatabase for performance reasons.
	public getSimpleUserDataMap(isReRequest: boolean) {
		if(isReRequest) {
			// well we need a blocking read...
		}
		
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
	
	// TODO: sometimes we need a user list, e.g. for ticket assignment, maybe we also want 
	//       a filtered user list, and the aplication provdes enough filter information.

	// we need this user data also for keeping track of the discussions done in the review
	
	// Provide the current logged in user.
	public getCurrentUserUUID():string {
		return "b4d1449b-d50e-4c9f-a4cb-dd2230278306";
	}
	
	
	public getSimpleUserDictionary():Observable<BackendModelSimpleUserDictionary> {
	    return this.httpClient
				.get<any>(UserDataQueryBackendService.URL_GET_ALL_USERS_AS_DICT, {})
				// todo: use map operator to create a shadow copy of the dictionary. 
				.pipe(first());
	}

}
