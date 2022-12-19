import { BackendModelProjectRecentCommitRevision } from './backend-model-project-recent-commit-revision';

export class BackendModelSingeFileCommitHistory {
	public filePath:string = "";
	public revisions:BackendModelProjectRecentCommitRevision[] = [];
}
