import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FileChangeSetSideBySideDiffComponent } from './file-change-set-side-by-side-diff.component';

describe('FileChangeSetSideBySideDiffComponent', () => {
  let component: FileChangeSetSideBySideDiffComponent;
  let fixture: ComponentFixture<FileChangeSetSideBySideDiffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FileChangeSetSideBySideDiffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FileChangeSetSideBySideDiffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
