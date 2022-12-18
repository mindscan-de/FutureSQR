import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

import { UiReviewFileInformation } from '../../../commonui/uimodel/ui-review-file-information';

@Component({
  selector: 'app-other-files-in-revision-panel',
  templateUrl: './other-files-in-revision-panel.component.html',
  styleUrls: ['./other-files-in-revision-panel.component.css']
})
export class OtherFilesInRevisionPanelComponent implements OnInit {
	
	// needed for the links
	public uiActiveProjectID: string = "";
	// needed for the links
	public uiActiveRevisionID: string = "";
	
	
	@Input() activeProjectID: string = "";
	private activeProjectIDProvided = false;
	@Input() activeRevisionID: string = "";
	private activeRevisionIDProvided = false;
	@Input() fileInformations:UiReviewFileInformation[] = [];

	constructor() { }

	ngOnInit(): void {
		
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		if(changes.activeProjectID != undefined) {
			this.uiActiveProjectID = changes.activeProjectID.currentValue; 
			this.activeProjectIDProvided = true;
		}
		
		if(changes.activeRevisionID != undefined) {
			this.uiActiveRevisionID = changes.activeRevisionID.currentValue;
			this.activeProjectIDProvided = true;
		}
		
		if(changes.fileInformations != undefined) {
			// TODO: filter/gray out the deleted files, since they can not be inspected any more.
		}
	}

}
