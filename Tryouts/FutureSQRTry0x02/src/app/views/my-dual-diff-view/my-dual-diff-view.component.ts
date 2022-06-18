import { Component, OnInit } from '@angular/core';

// ui model
import { UiDiffContentModel } from './uimodel/ui-diff-content-model';
 

@Component({
  selector: 'app-my-dual-diff-view',
  templateUrl: './my-dual-diff-view.component.html',
  styleUrls: ['./my-dual-diff-view.component.css']
})
export class MyDualDiffViewComponent implements OnInit {

	public content:string = "# Chapter 1\n"+"## Chapter 2\n"+"* X\n"+"  * Y\n"+"    * Z\n";  
	public readOnly:boolean = true;
	
	public leftContent : UiDiffContentModel = new UiDiffContentModel("",1); 
	public rightContent : UiDiffContentModel = new UiDiffContentModel("",100);
	
	
	// input should be a full file-diff
	// which then is m2m model transformed to a series of two diffs
	// left removals and constant
	// right adds and constant

  constructor() { }

  ngOnInit(): void {
  }

}
