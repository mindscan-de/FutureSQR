import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClosedReviewItemComponent } from './closed-review-item.component';

describe('ClosedReviewItemComponent', () => {
  let component: ClosedReviewItemComponent;
  let fixture: ComponentFixture<ClosedReviewItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClosedReviewItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClosedReviewItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
