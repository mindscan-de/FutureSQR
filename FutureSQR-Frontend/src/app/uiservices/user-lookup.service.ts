import { Injectable } from '@angular/core';

import { UiUser } from './model/ui-user';

@Injectable({
  providedIn: 'root'
})
export class UserLookupService {
	// TODO: lookup service should actually not handle these UiUser infos - but anyways this is hardcoded right now.
	private static javaBackend : boolean = true;
	
	private unknownUser : UiUser = new UiUser('00000000-4000-0000-0000-000000000000','Unknown',	
			(UserLookupService.javaBackend ? '/FutureSQR' : '') +
			'/assets/avatars/00000000-4000-0000-0000-000000000000.256px.jpg');
	private lookupMap : Map<string, UiUser> = new Map<string, UiUser>();


	constructor() {
		// Hardcode the users... for some time.
		// this stuff is temporary, to just make things work
		this.lookupMap.set('8ce74ee9-48ff-3dde-b678-58a632887e31', 
			new UiUser('8ce74ee9-48ff-3dde-b678-58a632887e31', 'Maxim Gansert',
			(UserLookupService.javaBackend ? '/FutureSQR' : '') +   
			'/assets/avatars/8ce74ee9-48ff-3dde-b678-58a632887e31.256px.jpg'));
		this.lookupMap.set('f5fc8449-3049-3498-9f6b-ce828515bba2', 
			new UiUser('f5fc8449-3049-3498-9f6b-ce828515bba2', 'Elsa Someone',
			(UserLookupService.javaBackend ? '/FutureSQR' : '') + 
			'/assets/avatars/f5fc8449-3049-3498-9f6b-ce828515bba2.256px.jpg'));
		this.lookupMap.set('35c94b55-559f-30e4-a2f4-ee16d31fc276',
			new UiUser('35c94b55-559f-30e4-a2f4-ee16d31fc276', 'Robert Breunung',
			(UserLookupService.javaBackend ? '/FutureSQR' : '') + 
			'/assets/avatars/35c94b55-559f-30e4-a2f4-ee16d31fc276.256px.jpg'));
	 }


	lookup(useruuid:string): UiUser {
		if(this.lookupMap.has(useruuid)) {
			return this.lookupMap.get(useruuid);
		}
		
		return new UiUser(useruuid, this.unknownUser.displayName, (UserLookupService.javaBackend ? '/FutureSQR' : '') + this.unknownUser.avatarLocation);
	}
	
	unknown(): UiUser {
		return  this.unknownUser;
	}
	
}
