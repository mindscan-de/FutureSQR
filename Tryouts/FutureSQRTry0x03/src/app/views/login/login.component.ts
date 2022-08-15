import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup,  Validators } from '@angular/forms';

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
		private formBuilder : FormBuilder
		
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
	}
	

}
