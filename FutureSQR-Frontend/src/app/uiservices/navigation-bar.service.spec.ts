import { TestBed } from '@angular/core/testing';

import { NavigationBarService } from './navigation-bar.service';

describe('NavigationBarService', () => {
  let service: NavigationBarService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NavigationBarService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
