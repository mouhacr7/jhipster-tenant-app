import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../license.test-samples';

import { LicenseFormService } from './license-form.service';

describe('License Form Service', () => {
  let service: LicenseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LicenseFormService);
  });

  describe('Service methods', () => {
    describe('createLicenseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLicenseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            active: expect.any(Object),
            module: expect.any(Object),
          })
        );
      });

      it('passing ILicense should create a new form with FormGroup', () => {
        const formGroup = service.createLicenseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            active: expect.any(Object),
            module: expect.any(Object),
          })
        );
      });
    });

    describe('getLicense', () => {
      it('should return NewLicense for default License initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLicenseFormGroup(sampleWithNewData);

        const license = service.getLicense(formGroup) as any;

        expect(license).toMatchObject(sampleWithNewData);
      });

      it('should return NewLicense for empty License initial value', () => {
        const formGroup = service.createLicenseFormGroup();

        const license = service.getLicense(formGroup) as any;

        expect(license).toMatchObject({});
      });

      it('should return ILicense', () => {
        const formGroup = service.createLicenseFormGroup(sampleWithRequiredData);

        const license = service.getLicense(formGroup) as any;

        expect(license).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILicense should not enable id FormControl', () => {
        const formGroup = service.createLicenseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLicense should disable id FormControl', () => {
        const formGroup = service.createLicenseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
