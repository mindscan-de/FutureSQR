import { TestBed } from '@angular/core/testing';

import { AuthZService } from './auth-z.service';

describe('AuthZService', () => {
  let service: AuthZService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthZService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
