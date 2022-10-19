import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoveParticipantFromReviewSelectionDialogComponent } from './remove-participant-from-review-selection-dialog.component';

describe('RemoveParticipantFromReviewSelectionDialogComponent', () => {
  let component: RemoveParticipantFromReviewSelectionDialogComponent;
  let fixture: ComponentFixture<RemoveParticipantFromReviewSelectionDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RemoveParticipantFromReviewSelectionDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RemoveParticipantFromReviewSelectionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
