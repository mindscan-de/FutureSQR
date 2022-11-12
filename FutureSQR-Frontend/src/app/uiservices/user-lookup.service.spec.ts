import { TestBed } from '@angular/core/testing';

import { UserLookupService } from './user-lookup.service';

describe('UserLookupService', () => {
  let service: UserLookupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserLookupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
