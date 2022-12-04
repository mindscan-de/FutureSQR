import { UiContentChangeSetModel } from './ui-content-change-set-model';

export class UiFileChangeSetModel {
	
	public uiContentChangeSet: UiContentChangeSetModel[] = [];
	
	// ATTN: This will be replaced by fromFilename, toFileName, InfoWhether we have moves etc.
	public tempLazyDiffLineInfo: string = "";
	// ATTN: This will be replaced and converted into a file mode, if needed.
	public tempLayzIndexLineInfo: string = "";
	
	public setUiContentChangeSet( contentChangeSet: UiContentChangeSetModel[]) : void {
		this.uiContentChangeSet = contentChangeSet;
	}
	
	public setDiffLine(diffLineInfo:string): void {
		this.tempLazyDiffLineInfo = diffLineInfo;
	}
	
	public setIndexLine(indexLineInfo:string): void {
		this.tempLayzIndexLineInfo = indexLineInfo;
	}
}
