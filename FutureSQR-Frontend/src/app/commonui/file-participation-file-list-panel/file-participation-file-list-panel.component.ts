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
			this.uiFileInformations = changes.fileInformations.currentValue; 
		}
	}

}
