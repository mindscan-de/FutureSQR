import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AvatarAndNameComponent } from './avatar-and-name.component';

describe('AvatarAndNameComponent', () => {
  let component: AvatarAndNameComponent;
  let fixture: ComponentFixture<AvatarAndNameComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AvatarAndNameComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AvatarAndNameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
