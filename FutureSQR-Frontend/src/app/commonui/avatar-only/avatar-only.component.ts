import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

import { UserLookupService }  from '../../uiservices/user-lookup.service';


@Component({
  selector: 'app-avatar-only',
  templateUrl: './avatar-only.component.html',
  styleUrls: ['./avatar-only.component.css']
})
export class AvatarOnlyComponent implements OnInit {
	
	public avatarLocation: string = "";
	public avatarDisplayName: string = "";
	
	@Input() url: string = "";
	@Input() uuid: string = "";
	@Input() size: number = 28;

	constructor(
		private userLookupService : UserLookupService
	) { 
		this.avatarLocation = this.userLookupService.unknown().avatarLocation;
	}

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		if(changes.uuid != undefined) {
			
			let uuid:string = changes.uuid.currentValue;
			let userInfo = this.userLookupService.lookup(uuid);
			
			this.avatarLocation = userInfo.avatarLocation;
			this.avatarDisplayName = userInfo.displayName;
		}
		else if (changes.url != undefined) {
			
			this.avatarLocation= changes.url.currentValue;
		}
	}

}
