import { Component, OnInit, Input,  SimpleChanges } from '@angular/core';

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

	constructor() { }

	ngOnInit(): void {
	}

}
