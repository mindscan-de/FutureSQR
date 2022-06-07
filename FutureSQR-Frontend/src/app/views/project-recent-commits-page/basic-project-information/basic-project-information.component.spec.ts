import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicProjectInformationComponent } from './basic-project-information.component';

describe('BasicProjectInformationComponent', () => {
  let component: BasicProjectInformationComponent;
  let fixture: ComponentFixture<BasicProjectInformationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BasicProjectInformationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BasicProjectInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
