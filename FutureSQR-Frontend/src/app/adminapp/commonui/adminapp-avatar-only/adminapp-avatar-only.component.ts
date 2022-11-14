import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

import { UserLookupService } from '../../../uiservices/user-lookup.service';


@Component({
  selector: 'app-adminapp-avatar-only',
  templateUrl: './adminapp-avatar-only.component.html',
  styleUrls: ['./adminapp-avatar-only.component.css']
})
export class AdminappAvatarOnlyComponent implements OnInit {
	
	public avatarLocation: string = "";
	public avatarDisplayName: string = "";

	@Input() uuid: string = "";
	@Input() size: number = 28; 

	constructor(
		private userLookupService : UserLookupService,	
	) { 
		this.avatarLocation = this.userLookupService.unknown().avatarLocation;		
	}

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		console.log("ngOnchanges");
		console.log(changes);
		if(changes.uuid != undefined) {
			
			let uuid:string = changes.uuid.currentValue;
			let userInfo = this.userLookupService.lookup(uuid);
			
			this.avatarLocation = userInfo.avatarLocation;
			this.avatarDisplayName = userInfo.displayName;
		}
		
	}

}
