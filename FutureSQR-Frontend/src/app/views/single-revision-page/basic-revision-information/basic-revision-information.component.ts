import { Component, OnInit, Input,  SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-basic-revision-information',
  templateUrl: './basic-revision-information.component.html',
  styleUrls: ['./basic-revision-information.component.css']
})
export class BasicRevisionInformationComponent implements OnInit {
	
	@Input() activeProjectID: string = "";
	@Input() activeRevisionID: string = "";

	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		
	}

}
