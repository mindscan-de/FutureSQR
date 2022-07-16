import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicRevisionInformationComponent } from './basic-revision-information.component';

describe('BasicRevisionInformationComponent', () => {
  let component: BasicRevisionInformationComponent;
  let fixture: ComponentFixture<BasicRevisionInformationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BasicRevisionInformationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BasicRevisionInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
