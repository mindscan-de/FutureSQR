import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FileParticipationFileListPanelComponent } from './file-participation-file-list-panel.component';

describe('FileParticipationFileListPanelComponent', () => {
  let component: FileParticipationFileListPanelComponent;
  let fixture: ComponentFixture<FileParticipationFileListPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FileParticipationFileListPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FileParticipationFileListPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
