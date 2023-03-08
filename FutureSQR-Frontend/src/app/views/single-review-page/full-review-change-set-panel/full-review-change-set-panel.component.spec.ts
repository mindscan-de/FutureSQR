import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FullReviewChangeSetPanelComponent } from './full-review-change-set-panel.component';

describe('FullReviewChangeSetPanelComponent', () => {
  let component: FullReviewChangeSetPanelComponent;
  let fixture: ComponentFixture<FullReviewChangeSetPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FullReviewChangeSetPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FullReviewChangeSetPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
