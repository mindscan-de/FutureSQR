import { TestBed } from '@angular/core/testing';

import { AuthNService } from './auth-n.service';

describe('AuthNService', () => {
  let service: AuthNService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthNService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
