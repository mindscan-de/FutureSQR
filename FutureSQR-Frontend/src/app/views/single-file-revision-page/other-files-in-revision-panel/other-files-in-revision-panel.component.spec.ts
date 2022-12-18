import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OtherFilesInRevisionPanelComponent } from './other-files-in-revision-panel.component';

describe('OtherFilesInRevisionPanelComponent', () => {
  let component: OtherFilesInRevisionPanelComponent;
  let fixture: ComponentFixture<OtherFilesInRevisionPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OtherFilesInRevisionPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OtherFilesInRevisionPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
