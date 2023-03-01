import { BackendModelSingleCommitFileContentChangeSet } from './backend-model-single-commit-file-content-change-set';

export class BackendModelSingleCommitFileChangeSet {
	public fromPath:string = "";
	public toPath:string = "";
	public lazy_index_line:string = "";
	
	// It contains a list of file_content_changeset
	public fileContentChangeSet: BackendModelSingleCommitFileContentChangeSet[] = [];
}
