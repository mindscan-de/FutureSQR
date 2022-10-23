import { TestBed } from '@angular/core/testing';

import { AdminNavigationBarService } from './admin-navigation-bar.service';

describe('AdminNavigationBarService', () => {
  let service: AdminNavigationBarService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminNavigationBarService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
