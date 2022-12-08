import { Component, Input, OnInit, Output, SimpleChanges, EventEmitter } from '@angular/core';

// Backend Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';

// Internal Services
import { CurrentUserService } from '../../../uiservices/current-user.service';

// backend model
import { BackendModelSingleCommitFileActionsInfo } from '../../../backend/model/backend-model-single-commit-file-actions-info';
import { BackendModelProjectRecentCommitRevision } from '../../../backend/model/backend-model-project-recent-commit-revision';


// UI Model
import { UiReviewFileInformation } from '../../../commonui/uimodel/ui-review-file-information';



@Component({
  selector: 'app-add-remove-single-revision-item',
  templateUrl: './add-remove-single-revision-item.component.html',
  styleUrls: ['./add-remove-single-revision-item.component.css']
})
export class AddRemoveSingleRevisionItemComponent implements OnInit {
	
	@Input() revision: BackendModelProjectRecentCommitRevision = new BackendModelProjectRecentCommitRevision();
	@Input() btnText: string = "btnText";
	@Input() title: string = "";
	@Input() activeProjectID : string;
	
	@Output() btnClicked: EventEmitter<string> = new EventEmitter<string>();

	public uiFileInformations: UiReviewFileInformation[] = undefined;
	public showFileList: boolean = false;
	
	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private currentUserService : CurrentUserService
	) { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		if(changes.btnText!=undefined) {
			this.btnText = changes.btnText.currentValue;
		}
	}

	onButtonClicked( revisionId: string ): void  {
		// currentUiReviewData.reviewFkProjectId, currentUiReviewData.reviewId, revision.revisionid)
		
		// send the revison id to the hosting component.
		
		// TODO: emit event.
		// this should call the outer method.
		console.log("somthieng...")
		
		this.btnClicked.emit(revisionId);
	}
	
	onToggleFileList() : void {
		// if we already have the file information, we just toggle it.
		if(this.uiFileInformations != undefined) {
			this.showFileList = !this.showFileList;
			return
		}
		
		let that = this;

		// otherwise retrieve the list and then show the list.
		
		this.projectDataQueryBackend.getRecentProjectRevisionFilePathsData(this.activeProjectID,this.revision.revisionid).subscribe(
			data =>  this.onFileListActionsProvided(data),
			error => console.log(error)
		);
	}
	
	// TODO: put this logic into m2m transformation.
	onFileListActionsProvided(fileChanges: BackendModelSingleCommitFileActionsInfo) : void {
		let fileInformations : UiReviewFileInformation[] = [];
		
		let map = fileChanges.fileActionMap;
		for(let i: number = 0;i<map.length;i++) {
			let fileInfo: UiReviewFileInformation = new UiReviewFileInformation( map[i][1], map[i][0], true );
			fileInformations.push(fileInfo);
		}
		this.uiFileInformations = fileInformations;
		
		this.showFileList = !this.showFileList;
	}
}
