import { Component, Input, OnInit, Output, SimpleChanges, EventEmitter } from '@angular/core';


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
	
	@Output() btnClicked: EventEmitter<string> = new EventEmitter<string>();

	public uiFileInformations: UiReviewFileInformation[] = undefined;
	public showFileList: boolean = false;
	
	constructor() { }

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
		if(true) {
			this.showFileList = !this.showFileList;
			return
		}
	}
	
	onFileListActionsProvided() : void {
		this.showFileList = !this.showFileList;
	}
}
