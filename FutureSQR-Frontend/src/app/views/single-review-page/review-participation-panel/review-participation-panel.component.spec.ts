import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewParticipationPanelComponent } from './review-participation-panel.component';

describe('ReviewParticipationPanelComponent', () => {
  let component: ReviewParticipationPanelComponent;
  let fixture: ComponentFixture<ReviewParticipationPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewParticipationPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewParticipationPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
