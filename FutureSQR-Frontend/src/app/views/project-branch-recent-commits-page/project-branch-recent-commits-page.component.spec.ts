import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectBranchRecentCommitsPageComponent } from './project-branch-recent-commits-page.component';

describe('ProjectBranchRecentCommitsPageComponent', () => {
  let component: ProjectBranchRecentCommitsPageComponent;
  let fixture: ComponentFixture<ProjectBranchRecentCommitsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectBranchRecentCommitsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectBranchRecentCommitsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
