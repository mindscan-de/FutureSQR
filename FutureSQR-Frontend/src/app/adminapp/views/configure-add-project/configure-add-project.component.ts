import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup,  Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-configure-add-project',
  templateUrl: './configure-add-project.component.html',
  styleUrls: ['./configure-add-project.component.css']
})
export class ConfigureAddProjectComponent implements OnInit {
	
	public addForm: FormGroup;
	public loading = false;	
	public submitted = false;
	

	constructor(
		private formBuilder : FormBuilder		
	) { }

	ngOnInit(): void {
		this.addForm = this.formBuilder.group({
			scmRepositoryURL: ['', Validators.required],
			scmProjectDisplayName: ['', Validators.required]
		});
	}
	
	public onSubmit(): void {
		console.log("onSumbit invoked...");
		
		this.submitted = true;
		
		if(this.addForm.invalid) {
			console.log("form is invalid");
			return;
		}
		
		let that: ConfigureAddProjectComponent = this;
		
		// @TODO: THIS is Wrong right now.
		this.loading = false;
		
		// @TODO: send this to the backend admin interface 
		
	}



	// accessor for the form.
	get f() { return this.addForm.controls; }

}
