import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AllProjectsPageComponent } from './all-projects-page.component';

describe('AllProjectsPageComponent', () => {
  let component: AllProjectsPageComponent;
  let fixture: ComponentFixture<AllProjectsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AllProjectsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AllProjectsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
