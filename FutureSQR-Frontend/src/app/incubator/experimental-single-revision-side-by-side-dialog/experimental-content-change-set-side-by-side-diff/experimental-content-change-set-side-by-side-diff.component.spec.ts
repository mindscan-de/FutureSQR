import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExperimantalContentChangeSetSideBySideDiffComponent } from './experimantal-content-change-set-side-by-side-diff.component';

describe('ExperimantalContentChangeSetSideBySideDiffComponent', () => {
  let component: ExperimantalContentChangeSetSideBySideDiffComponent;
  let fixture: ComponentFixture<ExperimantalContentChangeSetSideBySideDiffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExperimantalContentChangeSetSideBySideDiffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExperimantalContentChangeSetSideBySideDiffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
