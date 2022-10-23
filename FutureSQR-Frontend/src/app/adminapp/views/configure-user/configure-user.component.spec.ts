import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureUserComponent } from './configure-user.component';

describe('ConfigureUserComponent', () => {
  let component: ConfigureUserComponent;
  let fixture: ComponentFixture<ConfigureUserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigureUserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigureUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
