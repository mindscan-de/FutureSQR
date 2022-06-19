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
	public rightContent : UiDiffContentModel = new UiDiffContentModel("",1);
	
	public lineDiffData : string[] =  [
            "   \"requires\": true,",
            "   \"dependencies\": {",
            "     \"@angular-devkit/architect\": {",
            "-      \"version\": \"0.1001.1\",",
            "-      \"resolved\": \"https://registry.npmjs.org/@angular-devkit/architect/-/architect-0.1001.1.tgz\",",
            "-      \"integrity\": \"sha512-2jRO7L/k9gNxHVJxPoUMVvtf/KzsSXNT7akbAbb8CkBJxjx3NC3Y9NssPD9E78kyiXogO6IvkwyalBGrWvOPBQ==\",",
            "+      \"version\": \"0.1002.0\",",
            "+      \"resolved\": \"https://registry.npmjs.org/@angular-devkit/architect/-/architect-0.1002.0.tgz\",",
            "+      \"integrity\": \"sha512-twM8V03ujBIGVpgV1PBlSDodUdxtUb7WakutfWafAvEHUsgwzfvQz2VtKWvjNZ9AiYjnCuwkQaclqVv0VHNo9w==\",",
            "       \"dev\": true,",
            "       \"requires\": {",
            "-        \"@angular-devkit/core\": \"10.1.1\",",
            "+        \"@angular-devkit/core\": \"10.2.0\",",
            "         \"rxjs\": \"6.6.2\"",
            "       },",
            "       \"dependencies\": {"
          ]
	
	// input should be a full file-diff
	// which then is m2m model transformed to a series of two diffs
	// left removals and constant
	// right adds and constant

  constructor() { }

  ngOnInit(): void {
	this.leftContent = this.filterLeftDiff(this.lineDiffData);
	this.rightContent = this.filterRightDiff(this.lineDiffData)
  }

	filterLeftDiff(linediff: string[]) : UiDiffContentModel {
		let leftdiff = linediff.filter(line => !line.startsWith("+")).join("\n");
		
		let result:UiDiffContentModel = new UiDiffContentModel(leftdiff,12);
		
		return result; 
	}
	
	filterRightDiff(linediff: string[]) : UiDiffContentModel {
		let rightdiff = linediff.filter(line => !line.startsWith("-")).join("\n");
		
		let result:UiDiffContentModel = new UiDiffContentModel(rightdiff,12);
		
		return result; 
	}
	
}
