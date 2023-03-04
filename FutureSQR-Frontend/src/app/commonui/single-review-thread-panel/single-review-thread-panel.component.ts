import { Component, OnInit, Input, Output, SimpleChanges, EventEmitter } from '@angular/core';

import { BackendModelThreadsFullThread } from '../../backend/model/backend-model-threads-full-thread';


@Component({
  selector: 'app-single-review-thread-panel',
  templateUrl: './single-review-thread-panel.component.html',
  styleUrls: ['./single-review-thread-panel.component.css']
})
export class SingleReviewThreadPanelComponent implements OnInit {
	
	@Input() activeProjectID: string = "";
	@Input() activeReviewID: string = "";
	@Input() activeThread: BackendModelThreadsFullThread = new BackendModelThreadsFullThread();
	@Output() threadUpdated: EventEmitter<string> = new EventEmitter<string>();
	

	constructor() { }

	ngOnInit(): void {
	}

	onThreadUpdated(data:string): void {
		// TODO, actually we only want to update the active thread, none other.
		
		// forward the update to the main page.
		this.threadUpdated.emit(data);
	}
	
}
