import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleReviewThreadPanelComponent } from './single-review-thread-panel.component';

describe('SingleReviewThreadPanelComponent', () => {
  let component: SingleReviewThreadPanelComponent;
  let fixture: ComponentFixture<SingleReviewThreadPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SingleReviewThreadPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SingleReviewThreadPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
