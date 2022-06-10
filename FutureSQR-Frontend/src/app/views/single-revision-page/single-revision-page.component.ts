import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-single-revision-page',
  templateUrl: './single-revision-page.component.html',
  styleUrls: ['./single-revision-page.component.css']
})
export class SingleRevisionPageComponent implements OnInit {
	
	public activeProjectID: string = '';
	public activeRevisionID: string = '';

  constructor( private route: ActivatedRoute  ) { }

  ngOnInit(): void {
	this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
	this.activeRevisionID = this.route.snapshot.paramMap.get('revisionid');
	
	// TODO: query the revision change to previous revision from backend.  
  }

}
