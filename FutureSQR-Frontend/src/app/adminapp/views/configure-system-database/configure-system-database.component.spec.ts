import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureSystemDatabaseComponent } from './configure-system-database.component';

describe('ConfigureSystemDatabaseComponent', () => {
  let component: ConfigureSystemDatabaseComponent;
  let fixture: ComponentFixture<ConfigureSystemDatabaseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigureSystemDatabaseComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigureSystemDatabaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
