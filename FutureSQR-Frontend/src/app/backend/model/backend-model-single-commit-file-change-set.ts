import { BackendModelSingleCommitFileContentChangeSet } from './backend-model-single-commit-file-content-change-set';

export class BackendModelSingleCommitFileChangeSet {
	// TODO: contains information about a particular file
	//       and what happened to this file:
	// modified, added, renamed, etc., which hash it has. and so on....
	
	// It contains a list of file_content_changeset
	public fileContentChangeSet: BackendModelSingleCommitFileContentChangeSet[] = [];
}
