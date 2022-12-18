import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OtherFileRevisionsPanelComponent } from './other-file-revisions-panel.component';

describe('OtherFileRevisionsPanelComponent', () => {
  let component: OtherFileRevisionsPanelComponent;
  let fixture: ComponentFixture<OtherFileRevisionsPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OtherFileRevisionsPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OtherFileRevisionsPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
