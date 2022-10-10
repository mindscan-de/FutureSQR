import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddParticipantToReviewSelectionDialogComponent } from './add-participant-to-review-selection-dialog.component';

describe('AddParticipantToReviewSelectionDialogComponent', () => {
  let component: AddParticipantToReviewSelectionDialogComponent;
  let fixture: ComponentFixture<AddParticipantToReviewSelectionDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddParticipantToReviewSelectionDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddParticipantToReviewSelectionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
