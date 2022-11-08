import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonalUserFeedComponent } from './personal-user-feed.component';

describe('PersonalUserFeedComponent', () => {
  let component: PersonalUserFeedComponent;
  let fixture: ComponentFixture<PersonalUserFeedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PersonalUserFeedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PersonalUserFeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
