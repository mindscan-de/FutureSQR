import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRemoveSingleRevisionItemComponent } from './add-remove-single-revision-item.component';

describe('AddRemoveSingleRevisionItemComponent', () => {
  let component: AddRemoveSingleRevisionItemComponent;
  let fixture: ComponentFixture<AddRemoveSingleRevisionItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddRemoveSingleRevisionItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddRemoveSingleRevisionItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
