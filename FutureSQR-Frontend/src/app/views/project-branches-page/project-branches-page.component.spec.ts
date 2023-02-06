import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectBranchesPageComponent } from './project-branches-page.component';

describe('ProjectBranchesPageComponent', () => {
  let component: ProjectBranchesPageComponent;
  let fixture: ComponentFixture<ProjectBranchesPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectBranchesPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectBranchesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
