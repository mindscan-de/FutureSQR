import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})
export class ProjectComponent implements OnInit {
	
  public activeProjectName: string = "";

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
	this.activeProjectName = this.route.snapshot.paramMap.get('projectname');
  }

}
