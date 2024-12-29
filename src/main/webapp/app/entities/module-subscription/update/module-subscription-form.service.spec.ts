import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../module-subscription.test-samples';

import { ModuleSubscriptionFormService } from './module-subscription-form.service';

describe('ModuleSubscription Form Service', () => {
  let service: ModuleSubscriptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ModuleSubscriptionFormService);
  });

  describe('Service methods', () => {
    describe('createModuleSubscriptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createModuleSubscriptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            subscriptionDate: expect.any(Object),
            active: expect.any(Object),
            module: expect.any(Object),
            client: expect.any(Object),
          })
        );
      });

      it('passing IModuleSubscription should create a new form with FormGroup', () => {
        const formGroup = service.createModuleSubscriptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            subscriptionDate: expect.any(Object),
            active: expect.any(Object),
            module: expect.any(Object),
            client: expect.any(Object),
          })
        );
      });
    });

    describe('getModuleSubscription', () => {
      it('should return NewModuleSubscription for default ModuleSubscription initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createModuleSubscriptionFormGroup(sampleWithNewData);

        const moduleSubscription = service.getModuleSubscription(formGroup) as any;

        expect(moduleSubscription).toMatchObject(sampleWithNewData);
      });

      it('should return NewModuleSubscription for empty ModuleSubscription initial value', () => {
        const formGroup = service.createModuleSubscriptionFormGroup();

        const moduleSubscription = service.getModuleSubscription(formGroup) as any;

        expect(moduleSubscription).toMatchObject({});
      });

      it('should return IModuleSubscription', () => {
        const formGroup = service.createModuleSubscriptionFormGroup(sampleWithRequiredData);

        const moduleSubscription = service.getModuleSubscription(formGroup) as any;

        expect(moduleSubscription).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IModuleSubscription should not enable id FormControl', () => {
        const formGroup = service.createModuleSubscriptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewModuleSubscription should disable id FormControl', () => {
        const formGroup = service.createModuleSubscriptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
