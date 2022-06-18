import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-my-dual-diff-view',
  templateUrl: './my-dual-diff-view.component.html',
  styleUrls: ['./my-dual-diff-view.component.css']
})
export class MyDualDiffViewComponent implements OnInit {

	public content:string = "# Chapter 1\n"+"## Chapter 2\n"+"* X\n"+"  * Y\n"+"    * Z\n";  
	public readOnly:boolean = true;
	

  constructor() { }

  ngOnInit(): void {
  }

}
