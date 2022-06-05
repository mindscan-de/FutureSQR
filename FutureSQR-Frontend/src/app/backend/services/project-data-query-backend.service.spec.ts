import { TestBed } from '@angular/core/testing';

import { ProjectDataQueryBackendService } from './project-data-query-backend.service';

describe('ProjectDataQueryBackendService', () => {
  let service: ProjectDataQueryBackendService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProjectDataQueryBackendService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
