import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IModule, NewModule } from '../module.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IModule for edit and NewModuleFormGroupInput for create.
 */
type ModuleFormGroupInput = IModule | PartialWithRequiredKeyOf<NewModule>;

type ModuleFormDefaults = Pick<NewModule, 'id' | 'active' | 'employees'>;

type ModuleFormGroupContent = {
  id: FormControl<IModule['id'] | NewModule['id']>;
  name: FormControl<IModule['name']>;
  description: FormControl<IModule['description']>;
  type: FormControl<IModule['type']>;
  category: FormControl<IModule['category']>;
  active: FormControl<IModule['active']>;
  employees: FormControl<IModule['employees']>;
};

export type ModuleFormGroup = FormGroup<ModuleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ModuleFormService {
  createModuleFormGroup(module: ModuleFormGroupInput = { id: null }): ModuleFormGroup {
    const moduleRawValue = {
      ...this.getFormDefaults(),
      ...module,
    };
    return new FormGroup<ModuleFormGroupContent>({
      id: new FormControl(
        { value: moduleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(moduleRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(moduleRawValue.description),
      type: new FormControl(moduleRawValue.type, {
        validators: [Validators.required],
      }),
      category: new FormControl(moduleRawValue.category, {
        validators: [Validators.required],
      }),
      active: new FormControl(moduleRawValue.active, {
        validators: [Validators.required],
      }),
      employees: new FormControl(moduleRawValue.employees ?? []),
    });
  }

  getModule(form: ModuleFormGroup): IModule | NewModule {
    return form.getRawValue() as IModule | NewModule;
  }

  resetForm(form: ModuleFormGroup, module: ModuleFormGroupInput): void {
    const moduleRawValue = { ...this.getFormDefaults(), ...module };
    form.reset(
      {
        ...moduleRawValue,
        id: { value: moduleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ModuleFormDefaults {
    return {
      id: null,
      active: false,
      employees: [],
    };
  }
}
