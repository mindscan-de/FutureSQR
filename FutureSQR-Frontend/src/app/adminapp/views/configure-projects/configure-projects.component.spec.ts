import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureProjectsComponent } from './configure-projects.component';

describe('ConfigureProjectsComponent', () => {
  let component: ConfigureProjectsComponent;
  let fixture: ComponentFixture<ConfigureProjectsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigureProjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigureProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
