import { UiContentChangeSetModel } from './ui-content-change-set-model';

export class UiFileChangeSetModel {
	
	public uiContentChangeSet: UiContentChangeSetModel[] = [];
	
	
	public setUiContentChangeSet( contentChangeSet: UiContentChangeSetModel[]) : void {
		this.uiContentChangeSet = contentChangeSet;
	}
}
