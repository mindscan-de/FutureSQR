import { Injectable } from '@angular/core';

import { UiUser } from './model/ui-user';

@Injectable({
  providedIn: 'root'
})
export class UserLookupService {
	
	private unknownUser : UiUser = new UiUser('8ce74ee9-48ff-3dde-b678-58a632887e31','Unknown','/assets/avatars/00000000-4000-0000-0000-000000000000.256px.jpg');
	private lookupMap : Map<string, UiUser> = new Map<string, UiUser>();

	constructor() {
		// Hardcode the users... for some time.
		this.lookupMap.set('8ce74ee9-48ff-3dde-b678-58a632887e31', 
			new UiUser('8ce74ee9-48ff-3dde-b678-58a632887e31', 'Maxim Gansert', '/assets/avatars/8ce74ee9-48ff-3dde-b678-58a632887e31.256px.jpg'));
		this.lookupMap.set('f5fc8449-3049-3498-9f6b-ce828515bba2', 
			new UiUser('f5fc8449-3049-3498-9f6b-ce828515bba2', 'Elsa Someone', '/assets/avatars/f5fc8449-3049-3498-9f6b-ce828515bba2.256px.jpg'));
	 }


	lookup(useruuid:string): UiUser {
		if(this.lookupMap.has(useruuid)) {
			return this.lookupMap.get(useruuid);
		}
		
		return new UiUser(useruuid, this.unknownUser.displayName, this.unknownUser.avatarLocation);
	}
}
