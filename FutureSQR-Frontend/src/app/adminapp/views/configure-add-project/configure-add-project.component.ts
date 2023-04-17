import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup,  Validators, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';

// Admin App Services
import { AdminNavigationBarService }  from '../../services/admin-navigation-bar.service';
import { AdminNavbarBreadcrumbItem } from '../../services/model/admin-navbar-breadcrumb-item';

// Backend Services
import { AdminDataQueryBackendService } from '../../backend/services/admin-data-query-backend.service';
import { AdminBackendScmProjectConfiguration } from '../../backend/model/admin-backend-scm-project-configuration';

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
		private adminNavigationBarService : AdminNavigationBarService,
		private formBuilder : FormBuilder,
		private router: Router,		
		private adminDataQueryBackend : AdminDataQueryBackendService
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
		
		this.updateNavigationBar();		
	}
	
	updateNavigationBar() : void {
		let x:AdminNavbarBreadcrumbItem[] = []
		
		x.push(new AdminNavbarBreadcrumbItem('projects', ['projects'], false));
		
		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
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
		this.adminDataQueryBackend.postAddProject(
			this.f.scmRepositoryURL.value,
			this.f.scmProjectDisplayName.value,
			this.f.scmProjectId.value,
			this.f.scmProjectReviewPrefix.value,
			this.f.scmProjectDescription.value
		).subscribe( {
			next : (data) => {
				that.loading = false;
				const scmNewConfiguration:AdminBackendScmProjectConfiguration = data
				
				// TODO: check the answer for what we look for, eg. project id and such.
				
				// TODO: in case of success we want to navigate to the newly created project, such that we can setup more...
				that.router.navigate(["/","admin","project",scmNewConfiguration.projectId]);
			},
			error : (error) => {
				that.loading = false;
			}
		});
	}



	// accessor for the form.
	get f() { return this.addForm.controls; }
	
	myValidatorForSomething(myRequirement): ValidatorFn {
		return (control: AbstractControl): ValidationErrors | null => {
			// TODO must be boolean
			const newValue = control.value;
			
			// TODO if problem return dictionary of { myPropertyName: {value: control.value}} of null instead: 
			return null;
		};
	}

}
