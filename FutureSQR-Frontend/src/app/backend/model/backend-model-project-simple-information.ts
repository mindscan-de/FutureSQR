export class BackendModelProjectSimpleInformation {
	// a project should have a UUID - e.g for administration purposes and internal DB purposes.
	public projectUUID: string ="";
	
	public reviewPrefix: string = "";
	// basically this is the projectShortName of the project, it should be fixed once it is set
	// becasue references can exists for 10 or more years.
	public projectID: string = "";
	// prepare transition to project shortname.
	public projectShortName: string = "";
	
	public projectDisplayName: string = "";
	public projectDescription: string = "";
	// Obviously this is a property calculated from current user.
	public projectIsStarred: boolean = false;
	public projectStarCount: number = 0; 
}
