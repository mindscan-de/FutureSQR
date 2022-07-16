import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RevisionActionPanelComponent } from './revision-action-panel.component';

describe('RevisionActionPanelComponent', () => {
  let component: RevisionActionPanelComponent;
  let fixture: ComponentFixture<RevisionActionPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RevisionActionPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RevisionActionPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
