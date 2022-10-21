
export class CurrentUiUser {
	
	public logonName = "";
	public displayName = "";
	public uuid = "";
	
	constructor() {
	}
	
	public isAnonymous():boolean {
		return this.logonName == "";
	}
	
	
}
