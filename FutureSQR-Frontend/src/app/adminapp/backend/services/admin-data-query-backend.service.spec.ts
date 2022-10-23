import { TestBed } from '@angular/core/testing';

import { AdminDataQueryBackendService } from './admin-data-query-backend.service';

describe('AdminDataQueryBackendService', () => {
  let service: AdminDataQueryBackendService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminDataQueryBackendService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
