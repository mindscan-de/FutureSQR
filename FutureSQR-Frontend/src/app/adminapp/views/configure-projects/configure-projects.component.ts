import { Component, OnInit } from '@angular/core';

// General App Services
import { AdminNavigationBarService }  from '../../services/admin-navigation-bar.service';

@Component({
  selector: 'app-configure-projects',
  templateUrl: './configure-projects.component.html',
  styleUrls: ['./configure-projects.component.css']
})
export class ConfigureProjectsComponent implements OnInit {

	constructor(
 		private adminNavigationBarService : AdminNavigationBarService	
	) { }

  ngOnInit(): void {
  }

}
