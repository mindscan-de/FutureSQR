import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-basic-project-information',
  templateUrl: './basic-project-information.component.html',
  styleUrls: ['./basic-project-information.component.css']
})
export class BasicProjectInformationComponent implements OnInit {
	
	public isStarred: boolean = false;
	
	// TODO: we might provide better project information here.
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

	onStarMe(activeProjectId:string): void {
		this.isStarred = true;
	}
	
	onUnstarMe(activeProjectId:string): void {
		this.isStarred = false; 
	}

}
