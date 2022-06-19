import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-my-dual-diff-view',
  templateUrl: './my-dual-diff-view.component.html',
  styleUrls: ['./my-dual-diff-view.component.css']
})
export class MyDualDiffViewComponent implements OnInit {

	public content:string = "# Chapter 1\n"+"## Chapter 2\n"+"* X\n"+"  * Y\n"+"    * Z\n";  
	public readOnly:boolean = true;
	
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
  }

	
}
