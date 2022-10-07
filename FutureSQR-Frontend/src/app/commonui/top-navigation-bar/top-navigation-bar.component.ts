import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
 
import { NavigationBarService } from '../../services/navigation-bar.service';



@Component({
  selector: 'app-top-navigation-bar',
  templateUrl: './top-navigation-bar.component.html',
  styleUrls: ['./top-navigation-bar.component.css']
})
export class TopNavigationBarComponent implements OnInit {

	public title:String = "APPTITLE";

	@Input() appTitle:string;
	
	constructor (
		private navigationBarService : NavigationBarService
	) {}

	ngOnInit(): void {
		// TODO: we want to subscribe to changes intended for the breadcrumb navigation
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		let newTitle:string = changes.appTitle.currentValue;
		
		if(changes.appTitle.currentValue) {
			this.title = newTitle;
		}
	}

	
}
