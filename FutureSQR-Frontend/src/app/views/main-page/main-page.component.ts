import { Component, OnInit } from '@angular/core';

import { NavigationBarService } from '../../uiservices/navigation-bar.service';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

	constructor(
		private navbarService: NavigationBarService
	) { }

	ngOnInit(): void {
		this.navbarService.clearBreadcrumbNavigation();
	}

}
