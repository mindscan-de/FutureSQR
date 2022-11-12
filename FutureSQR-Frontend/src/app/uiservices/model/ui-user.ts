export class UiUser {
	public uuid:string;
	public displayName:string;
	public avatarLocation:string;
	
	constructor(uuid:string, displayName:string, avatarLocation:string) {
		this.uuid = uuid || '';
		this.displayName = displayName || "Unknown";
		
		// TODO: provide an icon for anonymous user
		this.avatarLocation = avatarLocation || '';
	}
}
