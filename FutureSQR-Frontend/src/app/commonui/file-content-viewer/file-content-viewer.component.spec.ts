import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FileContentViewerComponent } from './file-content-viewer.component';

describe('FileContentViewerComponent', () => {
  let component: FileContentViewerComponent;
  let fixture: ComponentFixture<FileContentViewerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FileContentViewerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FileContentViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
