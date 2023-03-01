import { BackendModelSingleCommitFileContentChangeSet } from './backend-model-single-commit-file-content-change-set';

export class BackendModelSingleCommitFileChangeSet {
	public fromPath:string = "";
	public toPath:string = "";
	
	// TODO: get rid of the index line.
	public lazy_index_line:string = "";
	
	// TODO: fill me next.
	public fileMode:string = "";
	public fileParentRevId:string = "";
	public fileCurrentRevId:string = "";
	
	// It contains a list of file_content_changeset
	public fileContentChangeSet: BackendModelSingleCommitFileContentChangeSet[] = [];
}
