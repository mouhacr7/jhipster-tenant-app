import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAccessRight, NewAccessRight } from '../access-right.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccessRight for edit and NewAccessRightFormGroupInput for create.
 */
type AccessRightFormGroupInput = IAccessRight | PartialWithRequiredKeyOf<NewAccessRight>;

type AccessRightFormDefaults = Pick<NewAccessRight, 'id' | 'canRead' | 'canWrite' | 'canDelete'>;

type AccessRightFormGroupContent = {
  id: FormControl<IAccessRight['id'] | NewAccessRight['id']>;
  canRead: FormControl<IAccessRight['canRead']>;
  canWrite: FormControl<IAccessRight['canWrite']>;
  canDelete: FormControl<IAccessRight['canDelete']>;
  module: FormControl<IAccessRight['module']>;
  employee: FormControl<IAccessRight['employee']>;
};

export type AccessRightFormGroup = FormGroup<AccessRightFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccessRightFormService {
  createAccessRightFormGroup(accessRight: AccessRightFormGroupInput = { id: null }): AccessRightFormGroup {
    const accessRightRawValue = {
      ...this.getFormDefaults(),
      ...accessRight,
    };
    return new FormGroup<AccessRightFormGroupContent>({
      id: new FormControl(
        { value: accessRightRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      canRead: new FormControl(accessRightRawValue.canRead, {
        validators: [Validators.required],
      }),
      canWrite: new FormControl(accessRightRawValue.canWrite, {
        validators: [Validators.required],
      }),
      canDelete: new FormControl(accessRightRawValue.canDelete, {
        validators: [Validators.required],
      }),
      module: new FormControl(accessRightRawValue.module),
      employee: new FormControl(accessRightRawValue.employee),
    });
  }

  getAccessRight(form: AccessRightFormGroup): IAccessRight | NewAccessRight {
    return form.getRawValue() as IAccessRight | NewAccessRight;
  }

  resetForm(form: AccessRightFormGroup, accessRight: AccessRightFormGroupInput): void {
    const accessRightRawValue = { ...this.getFormDefaults(), ...accessRight };
    form.reset(
      {
        ...accessRightRawValue,
        id: { value: accessRightRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AccessRightFormDefaults {
    return {
      id: null,
      canRead: false,
      canWrite: false,
      canDelete: false,
    };
  }
}
