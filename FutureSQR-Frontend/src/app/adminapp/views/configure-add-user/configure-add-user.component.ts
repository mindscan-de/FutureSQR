import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup,  Validators } from '@angular/forms';
import { Router } from '@angular/router';


import { AdminDataQueryBackendService } from '../../backend/services/admin-data-query-backend.service';

@Component({
  selector: 'app-configure-add-user',
  templateUrl: './configure-add-user.component.html',
  styleUrls: ['./configure-add-user.component.css']
})
export class ConfigureAddUserComponent implements OnInit {

	public addForm: FormGroup;
	public loading = false;	
	public submitted = false;

	constructor(
		private formBuilder : FormBuilder,
		private router: Router,		
		private accountService: AdminDataQueryBackendService
	) { }


	ngOnInit(): void {
		this.addForm = this.formBuilder.group( {
			username: ['', Validators.required],
			// password field should depend on backed auth method and only then be set as required
			// also add validator, that both passwords match each other
			password: ['', Validators.required],
			password2: ['', Validators.required],
			
			displayname: ['', Validators.required],
			contactemail: ['', Validators.compose([Validators.required, Validators.email])]
			});
	}
	
	// accessor for the form.
	get f() { return this.addForm.controls; }
	
	onSubmit() : void {
		console.log("onSubmit invoked...")
		
		this.submitted = true;
		
		if(this.addForm.invalid) {
			console.log("form is invalid")
			return;
		}
		
		let that:ConfigureAddUserComponent = this; 

		this.loading = false;
				
/*		this.accountService.postAddUser(
			this.f.username.value, 
			this.f.displayname.value,
			this.f.contactemail.value,
			this.f.password
			).subscribe( {
				next : (data) => {
					// TODO navigate to userpage - not part of the backend
					// to tell frontend which state the frontend should take
					console.log("User created");
					console.log(data);
					
					// will trigger a navigation and a page reload
					// where we want to navigate ? to the new user or to the userlist?
					// actually we want to further configure a created user (like groups, accounts for different SCMs)
					this.router.navigate(['/','admin','user','uuid']);
				},
				error : error => { 
					that.loading = false;
					}
				}
			)
*/
		
	}
	
	

}
