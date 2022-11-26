import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExperimentalContentChangeSetSideBySideDiffComponent } from './experimental-content-change-set-side-by-side-diff.component';

describe('ExperimantalContentChangeSetSideBySideDiffComponent', () => {
  let component: ExperimentalContentChangeSetSideBySideDiffComponent;
  let fixture: ComponentFixture<ExperimentalContentChangeSetSideBySideDiffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExperimentalContentChangeSetSideBySideDiffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExperimentalContentChangeSetSideBySideDiffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
