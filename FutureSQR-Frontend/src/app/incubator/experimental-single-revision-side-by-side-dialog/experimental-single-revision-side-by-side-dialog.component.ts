import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// Should be a ui model instead of a backend model...
import { BackendModelSingleCommitFileChangeSet } from '../../backend/model/backend-model-single-commit-file-change-set';


@Component({
  selector: 'app-experimental-single-revision-side-by-side-dialog',
  templateUrl: './experimental-single-revision-side-by-side-dialog.component.html',
  styleUrls: ['./experimental-single-revision-side-by-side-dialog.component.css']
})
export class ExperimentalSingleRevisionSideBySideDialogComponent implements OnInit {

	constructor(
		public activeModal: NgbActiveModal
	) { }

	ngOnInit(): void {
 	}

	onClose() : void  {
		this.activeModal.close(null)
	}

}
