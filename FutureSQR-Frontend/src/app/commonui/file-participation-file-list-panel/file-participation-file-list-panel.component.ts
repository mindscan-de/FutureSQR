import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

import { UiReviewFileInformation } from '../uimodel/ui-review-file-information';

@Component({
  selector: 'app-file-participation-file-list-panel',
  templateUrl: './file-participation-file-list-panel.component.html',
  styleUrls: ['./file-participation-file-list-panel.component.css']
})
export class FileParticipationFileListPanelComponent implements OnInit {

	public uiFileInformations: UiReviewFileInformation[] = [];

	@Input() fileInformations: UiReviewFileInformation[] = [];

	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges) : void {
		if(changes.fileInformations !== undefined) {
			// thing is we get this as an array of files sorted by path
			let allFiles : UiReviewFileInformation[] = changes.fileInformations.currentValue
			
			this.m2mTransform(allFiles);
			this.uiFileInformations = allFiles; 
		}
	}

	m2mTransform( allfiles:UiReviewFileInformation[]) : void {
		// TODO: We use parent file path to group the files.
		// TODO: what about the empty path?
		// just an algorithmic question...
	}
}
