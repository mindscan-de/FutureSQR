import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureSystemComponent } from './configure-system.component';

describe('ConfigureSystemComponent', () => {
  let component: ConfigureSystemComponent;
  let fixture: ComponentFixture<ConfigureSystemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigureSystemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigureSystemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
