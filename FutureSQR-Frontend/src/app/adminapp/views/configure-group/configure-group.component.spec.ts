import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureGroupComponent } from './configure-group.component';

describe('ConfigureGroupComponent', () => {
  let component: ConfigureGroupComponent;
  let fixture: ComponentFixture<ConfigureGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigureGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigureGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
