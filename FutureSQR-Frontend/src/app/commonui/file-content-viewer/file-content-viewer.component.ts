import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

import { BackendModelRevisionFileContent } from '../../backend/model/backend-model-revision-file-content';

import { UiReviewFileInformation } from '../uimodel/ui-review-file-information';

@Component({
  selector: 'app-file-content-viewer',
  templateUrl: './file-content-viewer.component.html',
  styleUrls: ['./file-content-viewer.component.css']
})
export class FileContentViewerComponent implements OnInit {

	public viewPortMargin:number = 1;
	public readOnly:boolean = true;
	public cmMode: string = "text";
	
	public uiRevisionFileContent: BackendModelRevisionFileContent = new BackendModelRevisionFileContent();
	public uiActiveFileInformation: UiReviewFileInformation = new UiReviewFileInformation("","",true);
	
	public toCodeMirror = {
		'md':'markdown',
		'html':'htmlmixed',
		'htm':'htmlmixed',
		'MD':'markdown',
		'py':'python',
		'java':'clike',
		'ts':'javascript'
	};
	@Input() revisionFileContent: BackendModelRevisionFileContent = new BackendModelRevisionFileContent();

	constructor(
		
	) { }

	ngOnInit(): void {
	}
	

	ngOnChanges(changes: SimpleChanges): void {
		if(changes.revisionFileContent != undefined) {
			this.uiRevisionFileContent = changes.revisionFileContent.currentValue;
			this.uiActiveFileInformation = new UiReviewFileInformation(this.uiRevisionFileContent.filePath, "", true);
			this.cmMode = this.toCodeMirror[this.uiActiveFileInformation.fileExtension];
		}
	}

}
