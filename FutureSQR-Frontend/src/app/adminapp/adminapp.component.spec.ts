import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminappComponent } from './adminapp.component';

describe('AdminappComponent', () => {
  let component: AdminappComponent;
  let fixture: ComponentFixture<AdminappComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminappComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminappComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
