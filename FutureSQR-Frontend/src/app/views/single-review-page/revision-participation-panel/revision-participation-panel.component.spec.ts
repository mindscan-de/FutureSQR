import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RevisionParticipationPanelComponent } from './revision-participation-panel.component';

describe('RevisionParticipationPanelComponent', () => {
  let component: RevisionParticipationPanelComponent;
  let fixture: ComponentFixture<RevisionParticipationPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RevisionParticipationPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RevisionParticipationPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
