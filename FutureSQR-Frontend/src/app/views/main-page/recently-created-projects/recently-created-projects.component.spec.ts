import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecentlyCreatedProjectsComponent } from './recently-created-projects.component';

describe('RecentlyCreatedProjectsComponent', () => {
  let component: RecentlyCreatedProjectsComponent;
  let fixture: ComponentFixture<RecentlyCreatedProjectsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecentlyCreatedProjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecentlyCreatedProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
