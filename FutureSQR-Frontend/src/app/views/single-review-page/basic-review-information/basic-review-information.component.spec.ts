import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicReviewInformationComponent } from './basic-review-information.component';

describe('BasicReviewInformationComponent', () => {
  let component: BasicReviewInformationComponent;
  let fixture: ComponentFixture<BasicReviewInformationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BasicReviewInformationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BasicReviewInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
