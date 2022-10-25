import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureAddProjectComponent } from './configure-add-project.component';

describe('ConfigureAddProjectComponent', () => {
  let component: ConfigureAddProjectComponent;
  let fixture: ComponentFixture<ConfigureAddProjectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigureAddProjectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigureAddProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
