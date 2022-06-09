import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleRevisionPageComponent } from './single-revision-page.component';

describe('SingleRevisionPageComponent', () => {
  let component: SingleRevisionPageComponent;
  let fixture: ComponentFixture<SingleRevisionPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SingleRevisionPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SingleRevisionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
