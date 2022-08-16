import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup,  Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/operators';

import { AccountService } from '../../_services/account.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
	
	public form: FormGroup;
	public loading = false;
	public submitted = false;

	constructor(
		private formBuilder : FormBuilder,
		private route: ActivatedRoute,
		private router: Router,
		private accountService: AccountService
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group(			{
				username: ['', Validators.required],
				password: ['', Validators.required]
		});
	}
	
	get f() { return this.form.controls; }
	
	
	onSubmit() : void {
		this.submitted = true;
		
		if (this.form.invalid) {
            return;
        }

		this.loading = true;
		// 
		this.accountService.login(this.f.username.value, this.f.password.value)
		.pipe(first())
		.subscribe({
			next: ()=> {
				const returnURL = this.route.snapshot.queryParams['returnUrl'] || '/';
				this.router.navigateByUrl(returnURL);				
			},
			error: error => {
				// TODO: have a kind of notification center in case of a bad login and then show this error.
				this.loading = false;
			}
		});
	}
	

}
