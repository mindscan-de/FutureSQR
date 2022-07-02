import { Component, OnInit, Input, SimpleChanges, ChangeDetectorRef } from '@angular/core';

// Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';

// Backed Model
import { BackendModelProjectSimpleInformation } from '../../../backend/model/backend-model-project-simple-information';


@Component({
  selector: 'app-basic-project-information',
  templateUrl: './basic-project-information.component.html',
  styleUrls: ['./basic-project-information.component.css']
})
export class BasicProjectInformationComponent implements OnInit {
	
	public isStarred: boolean = false;
	public uiProjectInfo: BackendModelProjectSimpleInformation = new BackendModelProjectSimpleInformation();
	
	// TODO: we might provide better project information here.
	@Input() activeProjectId:string;

	constructor( private projectDataQueryBackend : ProjectDataQueryBackendService, private cdr: ChangeDetectorRef  ) { }

	ngOnInit(): void {
		this.projectDataQueryBackend.getSimpleInformationByProject(this.activeProjectId).subscribe(
			data=> this.onSimpleProjectInformationLoaded(data),
			error=> {}
		);
	}
	
	onSimpleProjectInformationLoaded(projectInfo: BackendModelProjectSimpleInformation) : void {
		this.uiProjectInfo = projectInfo;
		this.isStarred = projectInfo.projectIsStarred;
		this.cdr.detectChanges();
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
				this.cdr.detectChanges();
			},
			error=>{}
		);
		
	}
	
	onUnstarMe(activeProjectId:string): void {
		this.projectDataQueryBackend.unstarProject(activeProjectId).subscribe(
			data =>{
				this.isStarred = false; 
				this.cdr.detectChanges();
			},
			error=>{},
		);
	}

}
