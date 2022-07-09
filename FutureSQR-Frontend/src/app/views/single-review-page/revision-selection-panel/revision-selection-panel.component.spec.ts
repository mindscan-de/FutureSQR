import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RevisionSelectionPanelComponent } from './revision-selection-panel.component';

describe('RevisionSelectionPanelComponent', () => {
  let component: RevisionSelectionPanelComponent;
  let fixture: ComponentFixture<RevisionSelectionPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RevisionSelectionPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RevisionSelectionPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
