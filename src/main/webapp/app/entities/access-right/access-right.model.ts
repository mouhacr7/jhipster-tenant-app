import { IModule } from 'app/entities/module/module.model';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IAccessRight {
  id: number;
  canRead?: boolean | null;
  canWrite?: boolean | null;
  canDelete?: boolean | null;
  module?: Pick<IModule, 'id'> | null;
  employee?: Pick<IEmployee, 'id'> | null;
}

export type NewAccessRight = Omit<IAccessRight, 'id'> & { id: null };
