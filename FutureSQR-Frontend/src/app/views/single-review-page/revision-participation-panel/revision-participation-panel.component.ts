import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-revision-participation-panel',
  templateUrl: './revision-participation-panel.component.html',
  styleUrls: ['./revision-participation-panel.component.css']
})
export class RevisionParticipationPanelComponent implements OnInit {
	
	@Output() onRevisionStateChanged = new EventEmitter<string>();

	constructor() { }

	ngOnInit(): void {
	}

}
