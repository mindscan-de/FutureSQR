import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyDualDiffViewComponent } from './my-dual-diff-view.component';

describe('MyDualDiffViewComponent', () => {
  let component: MyDualDiffViewComponent;
  let fixture: ComponentFixture<MyDualDiffViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyDualDiffViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyDualDiffViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
