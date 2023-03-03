import { BackendModelSingleCommitFileContentChangeSet } from './backend-model-single-commit-file-content-change-set';

export class BackendModelSingleCommitFileChangeSet {
	public fromPath:string = "";
	public toPath:string = "";
	
	public fileAction:string = "";
	public fileMode:string = "";
	public fileParentRevId:string = "";
	public fileCurrentRevId:string = "";
	
	// It contains a list of file_content_changeset
	public fileContentChangeSet: BackendModelSingleCommitFileContentChangeSet[] = [];
}
