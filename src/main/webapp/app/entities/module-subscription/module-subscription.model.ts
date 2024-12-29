import dayjs from 'dayjs/esm';
import { IModule } from 'app/entities/module/module.model';
import { IClient } from 'app/entities/client/client.model';

export interface IModuleSubscription {
  id: number;
  subscriptionDate?: dayjs.Dayjs | null;
  active?: boolean | null;
  module?: Pick<IModule, 'id'> | null;
  client?: Pick<IClient, 'id'> | null;
}

export type NewModuleSubscription = Omit<IModuleSubscription, 'id'> & { id: null };
