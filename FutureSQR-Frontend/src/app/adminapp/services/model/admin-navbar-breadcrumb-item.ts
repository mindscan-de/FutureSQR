export class AdminNavbarBreadcrumbItem {
	
	// the linktext.
	public linkText:string = "";

	// can be string or something else, should actually be string array?
	public routerLink:any[] = [];
	
	//  
	public routerLinkActive: boolean = false;
	
	constructor(linkText:string, routerLink:any[], routerLinkActive: boolean) {
		this.linkText = linkText;
		this.routerLink = routerLink;
		this.routerLinkActive = routerLinkActive
	}

	
}
