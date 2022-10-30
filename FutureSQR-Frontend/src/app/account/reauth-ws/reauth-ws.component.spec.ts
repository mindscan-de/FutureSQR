import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReauthWsComponent } from './reauth-ws.component';

describe('ReauthWsComponent', () => {
  let component: ReauthWsComponent;
  let fixture: ComponentFixture<ReauthWsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReauthWsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReauthWsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
