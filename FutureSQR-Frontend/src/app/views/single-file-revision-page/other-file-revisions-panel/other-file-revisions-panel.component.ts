import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-other-file-revisions-panel',
  templateUrl: './other-file-revisions-panel.component.html',
  styleUrls: ['./other-file-revisions-panel.component.css']
})
export class OtherFileRevisionsPanelComponent implements OnInit {

	@Input() activeProjectID: string = '';
	@Input() activeRevisionID: string = '';
	@Input() activeFilePath: string = '';

	constructor() { }
	
	

	ngOnInit(): void {
	}

}
