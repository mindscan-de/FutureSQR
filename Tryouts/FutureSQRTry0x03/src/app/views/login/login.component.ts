import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup,  Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
	
	public form: FormGroup;

	constructor(
		private formBuilder : FormBuilder
		
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group(
			{
				username: ['', Validators.required],
				password: ['', Validators.required]
			}
			
		);
	}
	
	
	onSubmit() : void {
		
	}
	

}
