import { Injectable } from '@angular/core';

import { AuthNService } from '../authn/auth-n.service';

import { CurrentBackendUser } from '../authn/model/current-backend-user';
import { CurrentBackendUserCapabilities } from '../authn/model/current-backend-user-capabilities';

@Injectable({
  providedIn: 'root'
})
export class FeatureFlagService {

	constructor(
		private authNService : AuthNService
	) {
 		this.authNService.liveBackendUserData().subscribe({
			next: data => {
				this.setCurrentFeatureFlags(this.m2mUserTransform(data));
			},
		});
	}
	
	m2mUserTransform(backendUser:CurrentBackendUser): Map<string,boolean> {
		let featureFlagMap: Map<string, boolean> = new Map<string, boolean>();
		
		if(backendUser.capabilities != undefined) {
			// TODO: maybe do sone magic on the featureflags, 
			// like setting some in client which the server is not aware of?
			featureFlagMap = backendUser.capabilities.featureflags;
		}
		
		return featureFlagMap;
	}


	setCurrentFeatureFlags(featureflags:Map<string, boolean> ): void {
	}
	
	isFeatureFlagSet(featureFlagName: string): boolean {
		return false;
	}
}
