import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['./redirect.component.css']
})
export class RedirectComponent implements OnInit {

	constructor(
		private route: ActivatedRoute,
		private router: Router,

	) { }

  ngOnInit(): void {
	
	const redirectURL = this.route.snapshot.queryParams['redirectUrl'] || '/';
	// avoid loops
	if (redirectURL != "/account/redirect") {
		this.router.navigateByUrl(redirectURL);
	}
	else {
		this.router.navigateByUrl("/");
	}
  }

}
