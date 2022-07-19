import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewDiscussionPanelComponent } from './review-discussion-panel.component';

describe('ReviewDiscussionPanelComponent', () => {
  let component: ReviewDiscussionPanelComponent;
  let fixture: ComponentFixture<ReviewDiscussionPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewDiscussionPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewDiscussionPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
