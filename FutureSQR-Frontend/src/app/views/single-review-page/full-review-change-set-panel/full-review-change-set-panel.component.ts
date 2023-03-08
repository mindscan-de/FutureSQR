import { Component, OnInit, Input } from '@angular/core';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

// UI Model
import { UiFileChangeSetModel } from '../../../commonui/uimodel/ui-file-change-set-model';

// UI-Dialog
import { SingleRevisionSideBySideDialogComponent } from '../../../commonui/single-revision-side-by-side-dialog/single-revision-side-by-side-dialog.component';


@Component({
  selector: 'app-full-review-change-set-panel',
  templateUrl: './full-review-change-set-panel.component.html',
  styleUrls: ['./full-review-change-set-panel.component.css']
})
export class FullReviewChangeSetPanelComponent implements OnInit {
	
	@Input() activeReviewChangeSet: UiFileChangeSetModel[] =[]

	constructor(
		private modalService: NgbModal		
	) { }

	ngOnInit(): void {
	}
	
	// open side by side dialog
	openSideBySideDialog( filechangeSet:UiFileChangeSetModel ):void {
		const modalref = this.modalService.open(  SingleRevisionSideBySideDialogComponent,  {centered: true, ariaLabelledBy: 'modal-basic-title', size:<any>'fs'}    )
		
		modalref.componentInstance.setAllChangeSets(this.activeReviewChangeSet);
		modalref.componentInstance.setSelectedFileChangeSet(filechangeSet);
		
		modalref.result.then((result) => {
			result.subscribe(
				data => {} ,
				error => {}
			)
		}, (reason) => {
		  // this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
	 	});
	}
	

}
