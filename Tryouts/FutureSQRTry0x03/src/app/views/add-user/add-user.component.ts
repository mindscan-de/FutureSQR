import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup,  Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { AccountService } from '../../_services/account.service';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {
	
	public addForm: FormGroup;
	public loading = false;	
	public submitted = false;

	constructor(
		private formBuilder : FormBuilder,
		private router: Router,		
		private accountService: AccountService
	) { }

	ngOnInit(): void {
		this.addForm = this.formBuilder.group( {
			username: ['', Validators.required],
			// password field should depend on backed auth method and only then be set as required
			// also add validator, that both passwords match each other
			password: ['', Validators.required],
			password2: ['', Validators.required],
			
			displayname: ['', Validators.required],
			contactemail: ['', Validators.required]
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
		
		let that:AddUserComponent = this; 

		this.loading = false;
				
		this.accountService.postAddUser(
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
					this.router.navigate(['/users']);
				},
				error : error => { 
					that.loading = false;
					}
				}
			)

		
	}

/*	onCreateUserClicked(): void {
		// TODO: create a NBModal Dialog.
		// the dialog should provide the invocation of the backend
		// on success this modal dialog closes itself
		// or the modal dialog closes itself on cancel
		// in case of successful close, we want to trigger a reload of 
		// the userlist or to patch the userlist and point to the entry (maybe we add user first to the list)
		
		// or instead of a modal dialog we can have a router endpoint
		// which can be navigated, and a token may be obtained. / maybe better
	}
*/
}
