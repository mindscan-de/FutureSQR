import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminappAvatarOnlyComponent } from './adminapp-avatar-only.component';

describe('AdminappAvatarOnlyComponent', () => {
  let component: AdminappAvatarOnlyComponent;
  let fixture: ComponentFixture<AdminappAvatarOnlyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminappAvatarOnlyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminappAvatarOnlyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
