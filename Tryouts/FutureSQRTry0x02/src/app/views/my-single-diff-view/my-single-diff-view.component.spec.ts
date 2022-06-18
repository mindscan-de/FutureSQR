import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MySingleDiffViewComponent } from './my-single-diff-view.component';

describe('MySingleDiffViewComponent', () => {
  let component: MySingleDiffViewComponent;
  let fixture: ComponentFixture<MySingleDiffViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MySingleDiffViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MySingleDiffViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
