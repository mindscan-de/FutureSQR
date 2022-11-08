import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecentReviewActionsLogComponent } from './recent-review-actions-log.component';

describe('RecentReviewActionsLogComponent', () => {
  let component: RecentReviewActionsLogComponent;
  let fixture: ComponentFixture<RecentReviewActionsLogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecentReviewActionsLogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecentReviewActionsLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
