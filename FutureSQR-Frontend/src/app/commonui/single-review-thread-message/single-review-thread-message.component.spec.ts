import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleReviewThreadMessageComponent } from './single-review-thread-message.component';

describe('SingleReviewThreadMessageComponent', () => {
  let component: SingleReviewThreadMessageComponent;
  let fixture: ComponentFixture<SingleReviewThreadMessageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SingleReviewThreadMessageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SingleReviewThreadMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
