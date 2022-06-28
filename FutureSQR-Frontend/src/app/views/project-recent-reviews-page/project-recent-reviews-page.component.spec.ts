import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectRecentReviewsPageComponent } from './project-recent-reviews-page.component';

describe('ProjectRecentReviewsPageComponent', () => {
  let component: ProjectRecentReviewsPageComponent;
  let fixture: ComponentFixture<ProjectRecentReviewsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectRecentReviewsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectRecentReviewsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
