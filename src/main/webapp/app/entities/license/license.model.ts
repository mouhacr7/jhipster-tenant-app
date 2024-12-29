import dayjs from 'dayjs/esm';
import { IModule } from 'app/entities/module/module.model';

export interface ILicense {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  active?: boolean | null;
  module?: Pick<IModule, 'id'> | null;
}

export type NewLicense = Omit<ILicense, 'id'> & { id: null };
