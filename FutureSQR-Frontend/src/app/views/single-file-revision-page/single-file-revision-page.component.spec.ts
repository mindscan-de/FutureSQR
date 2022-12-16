import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleFileRevisionPageComponent } from './single-file-revision-page.component';

describe('SingleFileRevisionPageComponent', () => {
  let component: SingleFileRevisionPageComponent;
  let fixture: ComponentFixture<SingleFileRevisionPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SingleFileRevisionPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SingleFileRevisionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
