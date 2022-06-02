import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AllProjectsComponent } from './all-projects.component';

describe('AllProjectsComponent', () => {
  let component: AllProjectsComponent;
  let fixture: ComponentFixture<AllProjectsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AllProjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AllProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
