import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecentlyContributedProjectsComponent } from './recently-contributed-projects.component';

describe('RecentlyContributedProjectsComponent', () => {
  let component: RecentlyContributedProjectsComponent;
  let fixture: ComponentFixture<RecentlyContributedProjectsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecentlyContributedProjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecentlyContributedProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
