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
			// TODO: after setting this, the displayname, the projectid, the prefix should be prefilled.
			scmRepositoryURL: ['', Validators.required],
			// 
			scmProjectDisplayName: ['', Validators.required],
			// TODO: this input should be checked every keystroke, whether this is a legal project name.
			// TODO: lowercase the input, also check the rules for the characters.
			scmProjectId: ['', Validators.required],
			// TODO: update this value, when the DisplayName is configured. ToUppercase
			scmProjectReviewPrefix: ['', Validators.required],
			// basically this should be optional. (html?)
			scmProjectDescription: ['', Validators.required]
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
