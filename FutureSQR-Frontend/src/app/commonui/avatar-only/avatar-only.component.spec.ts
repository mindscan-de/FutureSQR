import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AvatarOnlyComponent } from './avatar-only.component';

describe('AvatarOnlyComponent', () => {
  let component: AvatarOnlyComponent;
  let fixture: ComponentFixture<AvatarOnlyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AvatarOnlyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AvatarOnlyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
