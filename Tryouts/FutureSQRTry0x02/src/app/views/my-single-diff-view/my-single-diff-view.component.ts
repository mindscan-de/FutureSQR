import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-my-single-diff-view',
  templateUrl: './my-single-diff-view.component.html',
  styleUrls: ['./my-single-diff-view.component.css']
})
export class MySingleDiffViewComponent implements OnInit {
	
	public content:string = "# Chapter 1\n"+"## Chapter 2\n"+"* X\n"+"  * Y\n"+"    * Z\n";  
	public readOnly:boolean = true;
	

  constructor() { }

  ngOnInit(): void {
  }

}
