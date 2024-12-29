import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILicense, NewLicense } from '../license.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILicense for edit and NewLicenseFormGroupInput for create.
 */
type LicenseFormGroupInput = ILicense | PartialWithRequiredKeyOf<NewLicense>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILicense | NewLicense> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type LicenseFormRawValue = FormValueOf<ILicense>;

type NewLicenseFormRawValue = FormValueOf<NewLicense>;

type LicenseFormDefaults = Pick<NewLicense, 'id' | 'startDate' | 'endDate' | 'active'>;

type LicenseFormGroupContent = {
  id: FormControl<LicenseFormRawValue['id'] | NewLicense['id']>;
  startDate: FormControl<LicenseFormRawValue['startDate']>;
  endDate: FormControl<LicenseFormRawValue['endDate']>;
  active: FormControl<LicenseFormRawValue['active']>;
  module: FormControl<LicenseFormRawValue['module']>;
};

export type LicenseFormGroup = FormGroup<LicenseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LicenseFormService {
  createLicenseFormGroup(license: LicenseFormGroupInput = { id: null }): LicenseFormGroup {
    const licenseRawValue = this.convertLicenseToLicenseRawValue({
      ...this.getFormDefaults(),
      ...license,
    });
    return new FormGroup<LicenseFormGroupContent>({
      id: new FormControl(
        { value: licenseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startDate: new FormControl(licenseRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(licenseRawValue.endDate, {
        validators: [Validators.required],
      }),
      active: new FormControl(licenseRawValue.active, {
        validators: [Validators.required],
      }),
      module: new FormControl(licenseRawValue.module),
    });
  }

  getLicense(form: LicenseFormGroup): ILicense | NewLicense {
    return this.convertLicenseRawValueToLicense(form.getRawValue() as LicenseFormRawValue | NewLicenseFormRawValue);
  }

  resetForm(form: LicenseFormGroup, license: LicenseFormGroupInput): void {
    const licenseRawValue = this.convertLicenseToLicenseRawValue({ ...this.getFormDefaults(), ...license });
    form.reset(
      {
        ...licenseRawValue,
        id: { value: licenseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LicenseFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      active: false,
    };
  }

  private convertLicenseRawValueToLicense(rawLicense: LicenseFormRawValue | NewLicenseFormRawValue): ILicense | NewLicense {
    return {
      ...rawLicense,
      startDate: dayjs(rawLicense.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawLicense.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertLicenseToLicenseRawValue(
    license: ILicense | (Partial<NewLicense> & LicenseFormDefaults)
  ): LicenseFormRawValue | PartialWithRequiredKeyOf<NewLicenseFormRawValue> {
    return {
      ...license,
      startDate: license.startDate ? license.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: license.endDate ? license.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
