import { Component, OnInit, Input,  SimpleChanges} from '@angular/core';



import { BackendModelReviewData } from '../../../backend/model/backend-model-review-data';

@Component({
  selector: 'app-basic-review-information',
  templateUrl: './basic-review-information.component.html',
  styleUrls: ['./basic-review-information.component.css']
})
export class BasicReviewInformationComponent implements OnInit {
	
	@Input() activeReviewData: BackendModelReviewData = new BackendModelReviewData();
	@Input() activeProjectID: string = "";
	@Input() activeReviewID:string = "";
		
	constructor() { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		
	}

}
