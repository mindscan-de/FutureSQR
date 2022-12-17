import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

import { BackendModelRevisionFileContent } from '../../backend/model/backend-model-revision-file-content';

@Component({
  selector: 'app-file-content-viewer',
  templateUrl: './file-content-viewer.component.html',
  styleUrls: ['./file-content-viewer.component.css']
})
export class FileContentViewerComponent implements OnInit {

	@Input() revisionFileContent: BackendModelRevisionFileContent = new BackendModelRevisionFileContent();

	constructor(
		
	) { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		if(changes.revisionFileContent != undefined) {
		}
	}

}
