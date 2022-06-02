import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StaredProjectsComponent } from './stared-projects.component';

describe('StaredProjectsComponent', () => {
  let component: StaredProjectsComponent;
  let fixture: ComponentFixture<StaredProjectsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StaredProjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaredProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
