import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleReviewPageComponent } from './single-review-page.component';

describe('SingleReviewPageComponent', () => {
  let component: SingleReviewPageComponent;
  let fixture: ComponentFixture<SingleReviewPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SingleReviewPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SingleReviewPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
