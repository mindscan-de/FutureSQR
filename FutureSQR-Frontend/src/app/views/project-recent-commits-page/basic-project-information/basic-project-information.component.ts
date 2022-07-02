import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

// Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';


@Component({
  selector: 'app-basic-project-information',
  templateUrl: './basic-project-information.component.html',
  styleUrls: ['./basic-project-information.component.css']
})
export class BasicProjectInformationComponent implements OnInit {
	
	public isStarred: boolean = false;
	
	// TODO: we might provide better project information here.
	@Input() activeProjectId:string;

	constructor( private projectDataQueryBackend : ProjectDataQueryBackendService ) { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		let activeProjectId:string = changes.activeProjectId.currentValue;
		
		if(this.activeProjectId != activeProjectId) {
			this.activeProjectId = activeProjectId;
		}
	}

	onStarMe(activeProjectId:string): void {
		this.projectDataQueryBackend.starProject(activeProjectId).subscribe(
			data=>{
				this.isStarred = true;
			},
			error=>{}
		);
		
	}
	
	onUnstarMe(activeProjectId:string): void {
		this.projectDataQueryBackend.unstarProject(activeProjectId).subscribe(
			data =>{
				this.isStarred = false; 
			},
			error=>{},
		);
	}

}
