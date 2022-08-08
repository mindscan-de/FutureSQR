import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenReviewItemComponent } from './open-review-item.component';

describe('OpenReviewItemComponent', () => {
  let component: OpenReviewItemComponent;
  let fixture: ComponentFixture<OpenReviewItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpenReviewItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenReviewItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
