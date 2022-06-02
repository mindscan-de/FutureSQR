import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})
export class ProjectComponent implements OnInit {
	
  public activeProjectID: string = "";

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
	this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
  }

}
