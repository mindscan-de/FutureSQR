import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpXsrfTokenExtractor } from '@angular/common/http';
import { first } from 'rxjs/operators';


import {AuthNService } from '../../authn/auth-n.service';

@Component({
  selector: 'app-reauth-ws',
  templateUrl: './reauth-ws.component.html',
  styleUrls: ['./reauth-ws.component.css']
})
export class ReauthWsComponent implements OnInit {

	private returnUrl: string

	token: string | null = null
	tokenDefinition: CsrfToken | null = null

	constructor(
		extraktor: HttpXsrfTokenExtractor, 
		private http: HttpClient, 
		private router: Router, route: ActivatedRoute,
		private authNService: AuthNService
		
	) {
		this.returnUrl = route.snapshot.queryParams['returnUrl'] || '/';
		this.token = extraktor.getToken()
	}

	ngOnInit(): void {
		// first part is to be moved to authn service later, 
		// only the router remains here.
		// this.authnService.preauthenticate(returnURL / callback?)
		this.http.get<CsrfToken>("/FutureSQR/rest/login/csrf").pipe(first())
			.subscribe({
				next: n => { this.tokenDefinition = n },
				// timeout is only for debugging purpose and may be removed later
				complete: () => { setTimeout(() => {
					// set authn service to preauthenticated.
					this.authNService.updateCurrentBrowserAuthLifecycleToPreauthenticated();
					// DO we need to store this token somewhere else? 
					// if yes -> authnservice will have to store this in case of spare use / other solution if permanently required
					this.router.navigateByUrl(this.returnUrl);
				}, 3000) }
			});
	}
}

// TODO extract this to separate file - typescript compiler will compile that into a single file.
interface CsrfToken {
  headerName: String
  parameterName: String
  token: String
}
