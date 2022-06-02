import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-revision',
  templateUrl: './revision.component.html',
  styleUrls: ['./revision.component.scss']
})
export class RevisionComponent implements OnInit {
	
  public activeProjectID: string = '';
  public activeRevisionID: string = '';

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
	this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
	this.activeRevisionID = this.route.snapshot.paramMap.get('revisionid');	
  }

}
