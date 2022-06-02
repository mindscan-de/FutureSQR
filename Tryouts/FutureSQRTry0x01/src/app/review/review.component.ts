import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.scss']
})
export class ReviewComponent implements OnInit {
	
  public activeProjectID: string = '';
  public activeReviewID: string = '';

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
	// read the initial URL path based path parameters
	this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
	this.activeReviewID = this.route.snapshot.paramMap.get('reviewid');
  }

}
