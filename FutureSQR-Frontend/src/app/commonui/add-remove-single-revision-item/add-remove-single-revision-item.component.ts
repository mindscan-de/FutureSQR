import { Component, Input, OnInit, Output, SimpleChanges, EventEmitter } from '@angular/core';

// Backend Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// m2m
import { TransformCommitRevision } from '../../m2m/transform-commit-revision';

// backend model
import { BackendModelProjectRecentCommitRevision } from '../../backend/model/backend-model-project-recent-commit-revision';

// UI Model
import { UiReviewFileInformation } from '../../commonui/uimodel/ui-review-file-information';



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
		private projectDataQueryBackend : ProjectDataQueryBackendService
	) { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		if(changes.btnText!=undefined) {
			this.btnText = changes.btnText.currentValue;
		}
	}

	onButtonClicked( revisionId: string ): void  {
		// send the revison id to the hosting component.
		this.btnClicked.emit(revisionId);
	}
	
	onToggleFileList() : void {
		// if we already have the file information, we just toggle it.
		if(this.uiFileInformations != undefined) {
			this.showFileList = !this.showFileList;
			return;
		}
		
		// otherwise retrieve the list and then show the list.
		this.projectDataQueryBackend.getRecentProjectRevisionFilePathsData(this.activeProjectID,this.revision.revisionid).subscribe(
			data =>  this.onFileListActionsProvided(TransformCommitRevision.convertToUiReviewFileinformationArray(data)),
			error => console.log(error)
		);
	}
	
	onFileListActionsProvided(fileInformations:UiReviewFileInformation[]) : void {
		this.uiFileInformations = fileInformations;
		
		this.showFileList = !this.showFileList;
	}
}
