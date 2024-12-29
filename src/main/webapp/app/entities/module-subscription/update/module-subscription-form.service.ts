import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IModuleSubscription, NewModuleSubscription } from '../module-subscription.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IModuleSubscription for edit and NewModuleSubscriptionFormGroupInput for create.
 */
type ModuleSubscriptionFormGroupInput = IModuleSubscription | PartialWithRequiredKeyOf<NewModuleSubscription>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IModuleSubscription | NewModuleSubscription> = Omit<T, 'subscriptionDate'> & {
  subscriptionDate?: string | null;
};

type ModuleSubscriptionFormRawValue = FormValueOf<IModuleSubscription>;

type NewModuleSubscriptionFormRawValue = FormValueOf<NewModuleSubscription>;

type ModuleSubscriptionFormDefaults = Pick<NewModuleSubscription, 'id' | 'subscriptionDate' | 'active'>;

type ModuleSubscriptionFormGroupContent = {
  id: FormControl<ModuleSubscriptionFormRawValue['id'] | NewModuleSubscription['id']>;
  subscriptionDate: FormControl<ModuleSubscriptionFormRawValue['subscriptionDate']>;
  active: FormControl<ModuleSubscriptionFormRawValue['active']>;
  module: FormControl<ModuleSubscriptionFormRawValue['module']>;
  client: FormControl<ModuleSubscriptionFormRawValue['client']>;
};

export type ModuleSubscriptionFormGroup = FormGroup<ModuleSubscriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ModuleSubscriptionFormService {
  createModuleSubscriptionFormGroup(moduleSubscription: ModuleSubscriptionFormGroupInput = { id: null }): ModuleSubscriptionFormGroup {
    const moduleSubscriptionRawValue = this.convertModuleSubscriptionToModuleSubscriptionRawValue({
      ...this.getFormDefaults(),
      ...moduleSubscription,
    });
    return new FormGroup<ModuleSubscriptionFormGroupContent>({
      id: new FormControl(
        { value: moduleSubscriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      subscriptionDate: new FormControl(moduleSubscriptionRawValue.subscriptionDate, {
        validators: [Validators.required],
      }),
      active: new FormControl(moduleSubscriptionRawValue.active, {
        validators: [Validators.required],
      }),
      module: new FormControl(moduleSubscriptionRawValue.module),
      client: new FormControl(moduleSubscriptionRawValue.client),
    });
  }

  getModuleSubscription(form: ModuleSubscriptionFormGroup): IModuleSubscription | NewModuleSubscription {
    return this.convertModuleSubscriptionRawValueToModuleSubscription(
      form.getRawValue() as ModuleSubscriptionFormRawValue | NewModuleSubscriptionFormRawValue
    );
  }

  resetForm(form: ModuleSubscriptionFormGroup, moduleSubscription: ModuleSubscriptionFormGroupInput): void {
    const moduleSubscriptionRawValue = this.convertModuleSubscriptionToModuleSubscriptionRawValue({
      ...this.getFormDefaults(),
      ...moduleSubscription,
    });
    form.reset(
      {
        ...moduleSubscriptionRawValue,
        id: { value: moduleSubscriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ModuleSubscriptionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      subscriptionDate: currentTime,
      active: false,
    };
  }

  private convertModuleSubscriptionRawValueToModuleSubscription(
    rawModuleSubscription: ModuleSubscriptionFormRawValue | NewModuleSubscriptionFormRawValue
  ): IModuleSubscription | NewModuleSubscription {
    return {
      ...rawModuleSubscription,
      subscriptionDate: dayjs(rawModuleSubscription.subscriptionDate, DATE_TIME_FORMAT),
    };
  }

  private convertModuleSubscriptionToModuleSubscriptionRawValue(
    moduleSubscription: IModuleSubscription | (Partial<NewModuleSubscription> & ModuleSubscriptionFormDefaults)
  ): ModuleSubscriptionFormRawValue | PartialWithRequiredKeyOf<NewModuleSubscriptionFormRawValue> {
    return {
      ...moduleSubscription,
      subscriptionDate: moduleSubscription.subscriptionDate ? moduleSubscription.subscriptionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
