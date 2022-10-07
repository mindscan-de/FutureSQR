import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
 

@Component({
  selector: 'app-top-navigation-bar',
  templateUrl: './top-navigation-bar.component.html',
  styleUrls: ['./top-navigation-bar.component.css']
})
export class TopNavigationBarComponent implements OnInit {

	public title:String = "APPTITLE";

	@Input() appTitle:string;
	
	constructor() { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		let newTitle:string = changes.appTitle.currentValue;
		
		if(changes.appTitle.currentValue) {
			this.title = newTitle;
		}
	}	

}
