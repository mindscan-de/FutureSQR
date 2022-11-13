import { Injectable } from '@angular/core';

import { UiUser } from './model/ui-user';

@Injectable({
  providedIn: 'root'
})
export class UserLookupService {
	
	private unknownUser : UiUser = new UiUser('00000000-4000-0000-0000-000000000000','Unknown','/assets/avatars/00000000-4000-0000-0000-000000000000.256px.jpg');
	private lookupMap : Map<string, UiUser> = new Map<string, UiUser>();

	private commiterMap : Map<string, string> = new Map<string, string>();

	constructor() {
		// Hardcode the users... for some time.
		// this stuff is temporary, to just make things work
		this.lookupMap.set('8ce74ee9-48ff-3dde-b678-58a632887e31', 
			new UiUser('8ce74ee9-48ff-3dde-b678-58a632887e31', 'Maxim Gansert', '/assets/avatars/8ce74ee9-48ff-3dde-b678-58a632887e31.256px.jpg'));
		this.lookupMap.set('f5fc8449-3049-3498-9f6b-ce828515bba2', 
			new UiUser('f5fc8449-3049-3498-9f6b-ce828515bba2', 'Elsa Someone', '/assets/avatars/f5fc8449-3049-3498-9f6b-ce828515bba2.256px.jpg'));
		
		// this must be implemented in the backend, since only the backend knows the different commitnames for different repositories.
		// this stuff is temporary, to just make things work
		this.commiterMap.set('mindscan-de','8ce74ee9-48ff-3dde-b678-58a632887e31');
		this.commiterMap.set('<unknown>', '00000000-4000-0000-0000-000000000000');
	 }


	lookup(useruuid:string): UiUser {
		if(this.lookupMap.has(useruuid)) {
			return this.lookupMap.get(useruuid);
		}
		
		return new UiUser(useruuid, this.unknownUser.displayName, this.unknownUser.avatarLocation);
	}
	
	unknown(): UiUser {
		return  this.unknownUser;
	}
	
	// This is basically something the backend must do and provide a uuid for the author/commit info, instead of plain login name.
	// this is only to decorate the commits with the mapped user icon.
	lookupComitterToUuid(commitername:string): string {
		if(this.commiterMap.has(commitername)) {
			return this.commiterMap.get(commitername);
		}
		
		return this.commiterMap.get("<unknown>");
	}
}
