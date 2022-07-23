import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-file-participation-file-list-panel',
  templateUrl: './file-participation-file-list-panel.component.html',
  styleUrls: ['./file-participation-file-list-panel.component.css']
})
export class FileParticipationFileListPanelComponent implements OnInit {

	public uiFilePathActions: string[][] = [];

	@Input() filePathActions: string[][] = [];

	constructor() { }

	ngOnInit(): void {
		
	}

	ngOnChanges(changes: SimpleChanges) : void {
		
		if(changes.filePathActions !== undefined) {
			this.uiFilePathActions = changes.filePathActions.currentValue; 
		}
	}

}
