export class NavbarBreadcrumbItem {
	
	// the linktext.
	public linkText:string = "";
	
	// can be string or something else, should actually be string array?
	public routerLink:any[] = [];
	public hasQueryData: boolean= false;
	public queryData = {};
	
	//  
	public routerLinkActive: boolean = false;
	
	constructor(linkText:string, routerLink:any[], routerLinkActive: boolean) {
		this.linkText = linkText;
		this.routerLink = routerLink;
		this.routerLinkActive = routerLinkActive
	}
	
	public appendQueryData(data):void  {
		this.queryData = data;
		this.hasQueryData = true;
	}
}