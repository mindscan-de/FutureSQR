import { TestBed } from '@angular/core/testing';

import { AuthNGuardService } from './auth-nguard.service';

describe('AuthNGuardService', () => {
  let service: AuthNGuardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthNGuardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
