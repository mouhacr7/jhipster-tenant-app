import { IEmployee } from 'app/entities/employee/employee.model';
import { ModuleType } from 'app/entities/enumerations/module-type.model';
import { ModuleCategory } from 'app/entities/enumerations/module-category.model';

export interface IModule {
  id: number;
  name?: string | null;
  description?: string | null;
  type?: ModuleType | null;
  category?: ModuleCategory | null;
  active?: boolean | null;
  employees?: Pick<IEmployee, 'id'>[] | null;
}

export type NewModule = Omit<IModule, 'id'> & { id: null };
