import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FileChangeSetSingleDiffComponent } from './file-change-set-single-diff.component';

describe('FileChangeSetSingleDiffComponent', () => {
  let component: FileChangeSetSingleDiffComponent;
  let fixture: ComponentFixture<FileChangeSetSingleDiffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FileChangeSetSingleDiffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FileChangeSetSingleDiffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
