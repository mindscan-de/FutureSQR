import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentChangeSetSingleDiffComponent } from './content-change-set-single-diff.component';

describe('ContentChangeSetSingleDiffComponent', () => {
  let component: ContentChangeSetSingleDiffComponent;
  let fixture: ComponentFixture<ContentChangeSetSingleDiffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentChangeSetSingleDiffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentChangeSetSingleDiffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
