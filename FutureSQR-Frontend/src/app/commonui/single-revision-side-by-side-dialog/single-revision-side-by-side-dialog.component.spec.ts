import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleRevisionSideBySideDialogComponent } from './single-revision-side-by-side-dialog.component';

describe('SingleRevisionSideBySideDialogComponent', () => {
  let component: SingleRevisionSideBySideDialogComponent;
  let fixture: ComponentFixture<SingleRevisionSideBySideDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SingleRevisionSideBySideDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SingleRevisionSideBySideDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
