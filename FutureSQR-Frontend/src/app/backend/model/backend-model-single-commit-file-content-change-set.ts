export class BackendModelSingleCommitFileContentChangeSet {
	// the file content changeset actually contains the particular line changes 
	// and the involved line numbers and so on.
	
	// that will be reworked and updated, when better parsed
	public line_info:string = "";
	public line_diff_data: string[] = [];
	
}
