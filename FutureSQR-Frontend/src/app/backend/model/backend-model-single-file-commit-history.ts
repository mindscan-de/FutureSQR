import { BackendModelProjectRecentCommitRevision } from './backend-model-project-recent-commit-revision';

export class BackendModelSingleFileCommitHistory {
	public filePath:string = "";
	public revisions:BackendModelProjectRecentCommitRevision[] = [];
}
