import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MostActiveProjectsComponent } from './most-active-projects.component';

describe('MostActiveProjectsComponent', () => {
  let component: MostActiveProjectsComponent;
  let fixture: ComponentFixture<MostActiveProjectsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MostActiveProjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MostActiveProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
