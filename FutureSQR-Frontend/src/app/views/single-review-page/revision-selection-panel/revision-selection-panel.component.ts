import { Component, OnInit, Input, Output, SimpleChanges, EventEmitter } from '@angular/core';

import { BackendModelReviewData } from '../../../backend/model/backend-model-review-data';
import { BackendModelProjectRecentCommitRevision } from '../../../backend/model/backend-model-project-recent-commit-revision';


import { UiModelProjectRecentCommitRevision } from './uimodel/ui-model-project-recent-commit-revision';

@Component({
  selector: 'app-revision-selection-panel',
  templateUrl: './revision-selection-panel.component.html',
  styleUrls: ['./revision-selection-panel.component.css']
})
export class RevisionSelectionPanelComponent implements OnInit {

	public currentUiReviewRevisions: UiModelProjectRecentCommitRevision [] = [];
	public currentRevisionActivations: string = "";
	

	@Input() activeReviewData: BackendModelReviewData = new BackendModelReviewData();
	@Input() reviewRevisions: BackendModelProjectRecentCommitRevision[] = [];	
	@Output() onRevisionActivationChanged: EventEmitter<string> = new EventEmitter<string>();

	constructor() { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes: SimpleChanges) : void {
		if(changes.reviewRevisions !== undefined) {
			this.currentUiReviewRevisions = this.m2mTransform(changes.reviewRevisions.currentValue.reverse());
			this.updateActivations();
		}
	}
	
	onToggleSelection(revision:UiModelProjectRecentCommitRevision): void {
		revision.isRevisionSelected = !revision.isRevisionSelected;
		this.updateActivations();
	}
	
	onShowAllRevisions(): void {
		for(let i:number=0;i<this.currentUiReviewRevisions.length;i++) {
			this.currentUiReviewRevisions[i].isRevisionSelected = true;
		}
		this.updateActivations();
	}
	
	onHideAllRevisions(): void {
		for(let i:number=0;i<this.currentUiReviewRevisions.length;i++) {
			this.currentUiReviewRevisions[i].isRevisionSelected = false;
		}
		this.updateActivations();
	}
	
	updateActivations():void {
		let activation:string = "";

		for(let i:number=0;i<this.currentUiReviewRevisions.length;i++) {
			if(this.currentUiReviewRevisions[i].isRevisionSelected) {
				activation=activation.concat("a");
			}
			else {
				activation=activation.concat("b");
			}
		}
		
		this.currentRevisionActivations = activation;
		this.onRevisionActivationChanged.emit(activation);
	}
	
	// todo: convert to ui model

	m2mTransform(inputList: BackendModelProjectRecentCommitRevision[]) : UiModelProjectRecentCommitRevision[] {
		let resultList: UiModelProjectRecentCommitRevision[] = [];
		
		let previousUiItem:UiModelProjectRecentCommitRevision = null;
		for(var i:number=0; i<inputList.length;i++) {
			let currentItem = inputList[i];
			let newItem = new UiModelProjectRecentCommitRevision();
			
			newItem.authorid = currentItem.authorid;
			newItem.authorname = currentItem.authorname;
			newItem.date = currentItem.date;
			newItem.hasReview = currentItem.hasReview;
			newItem.message = currentItem.message;
			newItem.parents = currentItem.parents;
			newItem.parentsshort = currentItem.parentsshort;
			newItem.reldate = currentItem.reldate;
			newItem.reviewID = currentItem.reviewID;
			newItem.revisionid = currentItem.revisionid;
			newItem.shortdate = currentItem.shortdate;
			newItem.shortrev = currentItem.shortrev;
			newItem.isRevisionSelected = true;
			
			if(previousUiItem != null) {
				newItem.isRoot = false;
				
				newItem.isDirectChild = (previousUiItem.shortrev == currentItem.parentsshort);
			}
			else
			{
				newItem.isRoot = true;
			}
			
			previousUiItem=newItem;
			resultList.push(newItem);
		}
		
		 return resultList;
	}
}
