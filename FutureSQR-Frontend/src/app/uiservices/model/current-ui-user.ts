
export class CurrentUiUser {
	
	public logonName = "";
	public displayName = "";
	public uuid = "";
	public avatar = "";
	
	constructor() {
	}
	
	public isAnonymous():boolean {
		return this.logonName == undefined || this.logonName == "";
	}

}
