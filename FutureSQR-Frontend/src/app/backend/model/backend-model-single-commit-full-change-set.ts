import {BackendModelSingleCommitFileChangeSet} from './backend-model-single-commit-file-change-set';

export class BackendModelSingleCommitFullChangeSet {
	// contains information about a single commit
	// * commiter, date, message etc.
	
	public revisionId: string = "";
	
	// contains an array with every changed file and their particular changes
	public fileChangeSet: BackendModelSingleCommitFileChangeSet[] = [];
}
