import { UiContentChangeSetModel } from './ui-content-change-set-model';

export class UiFileChangeSetModel {
	
	public uiContentChangeSet: UiContentChangeSetModel[] = [];
	
	public scmFromFilePath: string = "";
	public scmFromParentPath: string = "";
	public scmFromFileName: string = "";
	
	public scmToFilePath: string = "";
	public scmToParentPath: string = "";
	public scmToFileName: string = "";
	
	
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
	
	public setScmFromPath(scmPath:string): void {
		this.scmFromFilePath = scmPath;
		let lastindex:number = scmPath.lastIndexOf("/");
		if(lastindex==-1) {
			this.scmFromFileName = scmPath;
		} 
		else {
			this.scmFromFileName = scmPath.substring(lastindex);
			this.scmFromParentPath = scmPath.substring(0,lastindex);
		}
	}
	
	public setScmToPath(scmPath:string): void {
		this.scmToFilePath = scmPath;
		let lastindex:number = scmPath.lastIndexOf("/");
		if(lastindex==-1) {
			this.scmToFileName = scmPath;
		} 
		else {
			this.scmToFileName = scmPath.substring(lastindex);
			this.scmToParentPath = scmPath.substring(0,lastindex);
		}
	}
	
}
