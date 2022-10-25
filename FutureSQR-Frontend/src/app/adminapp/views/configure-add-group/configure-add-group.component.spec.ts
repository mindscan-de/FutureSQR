import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureAddGroupComponent } from './configure-add-group.component';

describe('ConfigureAddGroupComponent', () => {
  let component: ConfigureAddGroupComponent;
  let fixture: ComponentFixture<ConfigureAddGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigureAddGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigureAddGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
