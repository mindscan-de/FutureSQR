import { TestBed } from '@angular/core/testing';

import { UserDataQueryBackendService } from './user-data-query-backend.service';

describe('UserDataQueryBackendService', () => {
  let service: UserDataQueryBackendService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserDataQueryBackendService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
