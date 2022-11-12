import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewResultComponent } from './review-result.component';

describe('ReviewResultComponent', () => {
  let component: ReviewResultComponent;
  let fixture: ComponentFixture<ReviewResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewResultComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
