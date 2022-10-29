import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup,  Validators } from '@angular/forms';

import { Router, ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/operators';


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
	
	constructor(
		private formBuilder : FormBuilder,
		private route: ActivatedRoute
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
	}
}
