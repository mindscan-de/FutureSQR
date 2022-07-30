import { Component, OnInit, Input,  SimpleChanges, ChangeDetectorRef  } from '@angular/core';

import { BackendModelThreadsMessage } from '../../backend/model/backend-model-threads-message'; 

@Component({
  selector: 'app-single-review-thread-message',
  templateUrl: './single-review-thread-message.component.html',
  styleUrls: ['./single-review-thread-message.component.css']
})
export class SingleReviewThreadMessageComponent implements OnInit {
	
	@Input() activeProjectID: string = "";
	@Input() activeReviewID: string = "";
	@Input() activeMessage: BackendModelThreadsMessage = new BackendModelThreadsMessage();
	
	public isInAnswerMode:boolean = false;
	public isMessageEditMode:boolean = false;


	constructor(private cdr: ChangeDetectorRef) { }

	ngOnInit(): void {
	}

	toggleEditTextMode():void {
		this.isMessageEditMode = !this.isMessageEditMode;
		this.cdr.detectChanges();
	}
}
