import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup,  Validators, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Admin App Services
import { AdminNavigationBarService }  from '../../services/admin-navigation-bar.service';
import { AdminNavbarBreadcrumbItem } from '../../services/model/admin-navbar-breadcrumb-item';

// Backend Service
import { AdminDataQueryBackendService } from '../../backend/services/admin-data-query-backend.service';
import { AdminBackendScmProjectConfiguration } from '../../backend/model/admin-backend-scm-project-configuration';

@Component({
  selector: 'app-configure-project',
  templateUrl: './configure-project.component.html',
  styleUrls: ['./configure-project.component.css']
})
export class ConfigureProjectComponent implements OnInit {
	
	public changeForm: FormGroup;
	public activeProjectID : string = '';
	public activeProjectConfiguration : AdminBackendScmProjectConfiguration = new AdminBackendScmProjectConfiguration();
	
	public loading = false;	
	public submitted = false;
	 

	constructor(
		private adminNavigationBarService : AdminNavigationBarService,
		private adminDataQueryBackend : AdminDataQueryBackendService,
		private formBuilder : FormBuilder,
		private route: ActivatedRoute, 
		private router: Router
	) { 
		// TODO: begin FormBuilder and Formvalidation
		
		
		// TODO: FIXME: suscribe to the URL changes - on reaload the clone button is calculated correctly, but on navigation it doesn't' work correctly
		// also the navigation bar will not be updated, when we go direct from project to admin...
	}

	ngOnInit(): void {
		this.changeForm = this.formBuilder.group({
			
		});
		
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		
		this.adminDataQueryBackend.getProjectConfiguration(this.activeProjectID).subscribe(
			data => { 
				this.onConfigurationProvided(data);
			},
			error => {}
		);
		
		this.updateNavigationBar();
	}
	
	private onConfigurationProvided( configration : AdminBackendScmProjectConfiguration ) : void {
		this.activeProjectConfiguration = configration;
	}

	updateNavigationBar() : void {
		let x:AdminNavbarBreadcrumbItem[] = []
		
		x.push(new AdminNavbarBreadcrumbItem('projects', ['projects'], false));
		x.push(new AdminNavbarBreadcrumbItem(this.activeProjectID, ['project',this.activeProjectID], true));
		 
		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
	}
	
	public onSubmit(): void {
		
	}
	
	public onCloneProjectToDisk(): void {
		// @TODO: start the clone process, it should be queued into a job queue and the option should be 
		//        blocked until this job is completed. 
	}


	// accessor for the form.
	get f() { return this.changeForm.controls; }

}
