export class NavbarBreadcrumbItem {
	public linkText:string = "linkText";
	// can be string or something else, should actually be string array?
	public routerLink:any[] = [];
	
	constructor(linkText, routerLink) {
		this.linkText = linkText;
		this.routerLink = routerLink;
	}
}
