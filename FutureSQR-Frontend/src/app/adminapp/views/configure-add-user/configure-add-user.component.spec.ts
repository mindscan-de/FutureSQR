import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureAddUserComponent } from './configure-add-user.component';

describe('ConfigureAddUserComponent', () => {
  let component: ConfigureAddUserComponent;
  let fixture: ComponentFixture<ConfigureAddUserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigureAddUserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigureAddUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
