import { UiContentChangeSetModel } from './ui-content-change-set-model';

export class UiFileChangeSetModel {
	public uiContentChangeSet: UiContentChangeSetModel[] = [];
	
	public scmFromFilePath: string = "";
	public scmFromParentPath: string = "";
	public scmFromFileName: string = "";
	
	public scmFileAction: string = "";
	public scmToFilePath: string = "";
	public scmToParentPath: string = "";
	public scmToFileName: string = "";
	
	public scmFileMode: string = "";
	public scmFileParentRevisionId = "";
	public scmFileCurrentRevisionId = "";
	
	
	public setUiContentChangeSet( contentChangeSet: UiContentChangeSetModel[]) : void {
		this.uiContentChangeSet = contentChangeSet;
	}

    setFileMode(fileMode: string) {
        this.scmFileMode = fileMode;
    }

    setFileParentRevision(fileParentRevId: string) {
        this.scmFileParentRevisionId = fileParentRevId;
    }
	
    setFileCurrentRevision(fileCurrentRevId: string) {
        this.scmFileCurrentRevisionId = fileCurrentRevId;
    }

    setFileAction(fileAction: string) {
        this.scmFileAction = fileAction;
    }


	public setScmFromPath(scmPath:string): void {
		this.scmFromFilePath = scmPath;
		let lastindex:number = scmPath.lastIndexOf("/");
		if(lastindex==-1) {
			this.scmFromFileName = scmPath;
		} 
		else {
			this.scmFromFileName = scmPath.substring(lastindex+1);
			this.scmFromParentPath = scmPath.substring(0,lastindex+1);
		}
	}
	
	public setScmToPath(scmPath:string): void {
		this.scmToFilePath = scmPath;
		let lastindex:number = scmPath.lastIndexOf("/");
		if(lastindex==-1) {
			this.scmToFileName = scmPath;
		} 
		else {
			this.scmToFileName = scmPath.substring(lastindex+1);
			this.scmToParentPath = scmPath.substring(0,lastindex+1);
		}
	}
	
}
