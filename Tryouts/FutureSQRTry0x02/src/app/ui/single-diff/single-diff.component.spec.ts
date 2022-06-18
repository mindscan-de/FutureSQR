import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleDiffComponent } from './single-diff.component';

describe('SingleDiffComponent', () => {
  let component: SingleDiffComponent;
  let fixture: ComponentFixture<SingleDiffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SingleDiffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SingleDiffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
