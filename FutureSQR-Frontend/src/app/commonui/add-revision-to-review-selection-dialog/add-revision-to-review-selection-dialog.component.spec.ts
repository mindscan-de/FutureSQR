import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRevisionToReviewSelectionDialogComponent } from './add-revision-to-review-selection-dialog.component';

describe('AddRevisionToReviewSelectionDialogComponent', () => {
  let component: AddRevisionToReviewSelectionDialogComponent;
  let fixture: ComponentFixture<AddRevisionToReviewSelectionDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddRevisionToReviewSelectionDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddRevisionToReviewSelectionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
