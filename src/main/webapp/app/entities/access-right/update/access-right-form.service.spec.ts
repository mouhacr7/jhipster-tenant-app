import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../access-right.test-samples';

import { AccessRightFormService } from './access-right-form.service';

describe('AccessRight Form Service', () => {
  let service: AccessRightFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccessRightFormService);
  });

  describe('Service methods', () => {
    describe('createAccessRightFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccessRightFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            canRead: expect.any(Object),
            canWrite: expect.any(Object),
            canDelete: expect.any(Object),
            module: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IAccessRight should create a new form with FormGroup', () => {
        const formGroup = service.createAccessRightFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            canRead: expect.any(Object),
            canWrite: expect.any(Object),
            canDelete: expect.any(Object),
            module: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getAccessRight', () => {
      it('should return NewAccessRight for default AccessRight initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAccessRightFormGroup(sampleWithNewData);

        const accessRight = service.getAccessRight(formGroup) as any;

        expect(accessRight).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccessRight for empty AccessRight initial value', () => {
        const formGroup = service.createAccessRightFormGroup();

        const accessRight = service.getAccessRight(formGroup) as any;

        expect(accessRight).toMatchObject({});
      });

      it('should return IAccessRight', () => {
        const formGroup = service.createAccessRightFormGroup(sampleWithRequiredData);

        const accessRight = service.getAccessRight(formGroup) as any;

        expect(accessRight).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccessRight should not enable id FormControl', () => {
        const formGroup = service.createAccessRightFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccessRight should disable id FormControl', () => {
        const formGroup = service.createAccessRightFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
