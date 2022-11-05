// TODO align format, with user / user rights with all backends
// python, springboot
// this type contains the backend user info (login, uuid, displayname, etc. pp) 
// and the current user permissions, such that the fontend can react to the
// permissions in the backend (e.g. show admin links).
export class CurrentBackendUser {
	public uuid: string;
	public loginname: string;
	public displayname: string;
	public avatarlocation: string;
	public email: string;
	
	// TODO provide some is valid methods?
	
	isValid() {
		// TODO combine with other metrics as well..
		return this.isUUIDValid();
	}
	
	isUUIDValid() {
		return this.uuid!=undefined && this.uuid!=null && this.uuid != "";
	}
}
