import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExperimentalSingleRevisionSideBySideDialogComponent } from './experimental-single-revision-side-by-side-dialog.component';

describe('ExperimentalSingleRevisionSideBySideDialogComponent', () => {
  let component: ExperimentalSingleRevisionSideBySideDialogComponent;
  let fixture: ComponentFixture<ExperimentalSingleRevisionSideBySideDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExperimentalSingleRevisionSideBySideDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExperimentalSingleRevisionSideBySideDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
