import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

import { UserLookupService }  from '../../uiservices/user-lookup.service';
import { UiUser } from '../../uiservices/model/ui-user';


@Component({
  selector: 'app-avatar-and-name',
  templateUrl: './avatar-and-name.component.html',
  styleUrls: ['./avatar-and-name.component.css']
})
export class AvatarAndNameComponent implements OnInit {
	
	public userInfo: UiUser;
	
	@Input() uuid: string = "";
	@Input() size: number = 28;

	constructor(
		private userLookupService : UserLookupService
	) { 
		this.userInfo = this.userLookupService.unknown();
	}

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		if(changes.uuid != undefined) {
			let uuid:string = changes.uuid.currentValue;
			this.userInfo = this.userLookupService.lookup(uuid)
		}
	}
}
