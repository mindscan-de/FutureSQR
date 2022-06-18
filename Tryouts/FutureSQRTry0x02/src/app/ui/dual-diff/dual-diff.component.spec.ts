import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DualDiffComponent } from './dual-diff.component';

describe('DualDiffComponent', () => {
  let component: DualDiffComponent;
  let fixture: ComponentFixture<DualDiffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DualDiffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DualDiffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
