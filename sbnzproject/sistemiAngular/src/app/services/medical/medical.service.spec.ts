import { TestBed, inject } from '@angular/core/testing';

import { MedicalService } from './medical.service';

describe('MedicalService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MedicalService]
    });
  });

  it('should be created', inject([MedicalService], (service: MedicalService) => {
    expect(service).toBeTruthy();
  }));
});
