import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoveRevisionFromReviewSelectionDialogComponent } from './remove-revision-from-review-selection-dialog.component';

describe('RemoveRevisionFromReviewSelectionDialogComponent', () => {
  let component: RemoveRevisionFromReviewSelectionDialogComponent;
  let fixture: ComponentFixture<RemoveRevisionFromReviewSelectionDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RemoveRevisionFromReviewSelectionDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RemoveRevisionFromReviewSelectionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
