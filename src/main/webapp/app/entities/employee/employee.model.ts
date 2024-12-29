import { IModule } from 'app/entities/module/module.model';
import { IClient } from 'app/entities/client/client.model';
import { UserRole } from 'app/entities/enumerations/user-role.model';

export interface IEmployee {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phone?: string | null;
  role?: UserRole | null;
  accessRights?: Pick<IModule, 'id'>[] | null;
  client?: Pick<IClient, 'id'> | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
