import { Component, OnInit, Input, Output, SimpleChanges, ChangeDetectorRef, EventEmitter } from '@angular/core';

// Backend Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';

// Internal Services
import { CurrentUserService } from '../../../uiservices/current-user.service';

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
	@Output() revisionHistoryUpdated: EventEmitter<string> = new EventEmitter<string>();

	constructor( 
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private currentUserService : CurrentUserService,
		private cdr: ChangeDetectorRef  ) { }

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
		let currentUserUUID:string = this.currentUserService.getCurrentUserUUID();
		
		this.projectDataQueryBackend.starProject(activeProjectId, currentUserUUID).subscribe(
			data=>{
				this.isStarred = true;
				this.uiProjectInfo.projectStarCount += 1;
				this.cdr.detectChanges();
			},
			error=>{}
		);
		
	}
	
	onUnstarMe(activeProjectId:string): void {
		let currentUserUUID:string = this.currentUserService.getCurrentUserUUID();
		
		this.projectDataQueryBackend.unstarProject(activeProjectId, currentUserUUID).subscribe(
			data =>{
				this.isStarred = false; 
				this.uiProjectInfo.projectStarCount -= 1;
				this.cdr.detectChanges();
			},
			error=>{},
		);
	}
	
	onUpdateProjectCache(activeProjectId:string): void {
		let that = this;
		
		this.projectDataQueryBackend.updateProjectCache(activeProjectId).subscribe(
			data => {
				that.revisionHistoryUpdated.emit("updated");
			},
			error=>{}
		);
	}

}
