import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }


/*	onCreateUserClicked(): void {
		// TODO: create a NBModal Dialog.
		// the dialog should provide the invocation of the backend
		// on success this modal dialog closes itself
		// or the modal dialog closes itself on cancel
		// in case of successful close, we want to trigger a reload of 
		// the userlist or to patch the userlist and point to the entry (maybe we add user first to the list)
		
		// or instead of a modal dialog we can have a router endpoint
		// which can be navigated, and a token may be obtained. / maybe better
	}
*/
}
