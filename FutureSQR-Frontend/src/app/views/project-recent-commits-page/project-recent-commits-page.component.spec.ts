import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectRecentCommitsPageComponent } from './project-recent-commits-page.component';

describe('ProjectRecentCommitsPageComponent', () => {
  let component: ProjectRecentCommitsPageComponent;
  let fixture: ComponentFixture<ProjectRecentCommitsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectRecentCommitsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectRecentCommitsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
