import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StarredProjectsComponent } from './starred-projects.component';

describe('StarredProjectsComponent', () => {
  let component: StarredProjectsComponent;
  let fixture: ComponentFixture<StarredProjectsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StarredProjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StarredProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
