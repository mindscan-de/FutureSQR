import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentChangeSetSideBySideDiffComponent } from './content-change-set-side-by-side-diff.component';

describe('ContentChangeSetSideBySideDiffComponent', () => {
  let component: ContentChangeSetSideBySideDiffComponent;
  let fixture: ComponentFixture<ContentChangeSetSideBySideDiffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentChangeSetSideBySideDiffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentChangeSetSideBySideDiffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
