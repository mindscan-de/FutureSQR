import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup,  Validators } from '@angular/forms';

import { Router, ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/operators';


import { AuthNService } from '../../authn/auth-n.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
	
	/**
		Okay long story short, the user either landed on this page either by choice, or because the
		user could not be authenticated transparently. Therefore we must aquire a token for sending
		the form to login.
		
		And then login using the AuthN Service. This caneither have two outcomes, the user/password
		is wrong or not, if wrong, we must still remain on this page, but do we have to aquire a new
		token?
		
		If the user can be successfully authenticated, all kind of stuff must happen....
		
		* We must set the current user in the ui services. But this should be part of the success 
		  logic of the auth-n-service. 
		* Also the redirect after a successful login to the return url must be performed. can either 
		  be done here when the authn service has a success part and a fail part.
		* Some more clarifications needed here?
	 */
	
	public form: FormGroup;
	public loading = false;
	public submitted = false;
	
	private returnURL: string;
	
	// TODO: we must clear the content of the breadcrumb navigation bar...
	
	constructor(
		private formBuilder : FormBuilder,
		private route: ActivatedRoute,
		private router: Router,
		private authnService: AuthNService
	) { }

	ngOnInit(): void {
		this.returnURL = this.route.snapshot.queryParams['returnUrl'] || '/';
		
		this.form = this.formBuilder.group(			{
				username: ['', Validators.required],
				password: ['', Validators.required]
		});
		
	}
	
	get f() { return this.form.controls; }
	
	onSubmit() : void {
		// mark that a form was submitted
		this.submitted = true;
		
		if (this.form.invalid) {
			// but keep messages in form
            return;
        }
		
		// indicate that we are processing this request and this will disable the button and run the spinner
		this.loading = true;
		
		// TODO add a subscription/future/callback for the good and the bad case
		this.authnService.login( this.f.username.value, this.f.password.value, 
			{	// if login is successful, we want to redirect the user to the desired page
				next: ()=>{this.onSuccessfulAuthentication(); },
				
				// if failed we want the user to be able to provide new password
				failed: ()=>{this.onFailedAuthentication();},
	
				// if error, we want the user to be able to still act somehow, message would be nice.
				error: ()=>{this.onFailedAuthentication();}}
			);
		
	}
	
	onSuccessfulAuthentication(): void  {
		this.loading = false;
		this.router.navigateByUrl(this.returnURL);
	}
	
	onFailedAuthentication(): void {
		this.loading = false;
	}
}
