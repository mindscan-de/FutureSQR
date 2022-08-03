import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectRecentRevisionComponent } from './project-recent-revision.component';

describe('ProjectRecentRevisionComponent', () => {
  let component: ProjectRecentRevisionComponent;
  let fixture: ComponentFixture<ProjectRecentRevisionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectRecentRevisionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectRecentRevisionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
