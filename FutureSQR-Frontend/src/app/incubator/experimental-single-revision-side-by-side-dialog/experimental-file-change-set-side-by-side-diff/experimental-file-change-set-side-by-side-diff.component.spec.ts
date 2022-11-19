import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExperimentalFileChangeSetSideBySideDiffComponent } from './experimental-file-change-set-side-by-side-diff.component';

describe('ExperimentalFileChangeSetSideBySideDiffComponent', () => {
  let component: ExperimentalFileChangeSetSideBySideDiffComponent;
  let fixture: ComponentFixture<ExperimentalFileChangeSetSideBySideDiffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExperimentalFileChangeSetSideBySideDiffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExperimentalFileChangeSetSideBySideDiffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
