import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-basic-project-information',
  templateUrl: './basic-project-information.component.html',
  styleUrls: ['./basic-project-information.component.css']
})
export class BasicProjectInformationComponent implements OnInit {
	
	@Input() activeProjectId:string;

	constructor() { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		let activeProjectId:string = changes.activeProjectId.currentValue;
		
		if(this.activeProjectId != activeProjectId) {
			this.activeProjectId = activeProjectId;
		}
	}

}
