import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureGroupsComponent } from './configure-groups.component';

describe('ConfigureGroupsComponent', () => {
  let component: ConfigureGroupsComponent;
  let fixture: ComponentFixture<ConfigureGroupsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigureGroupsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigureGroupsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
