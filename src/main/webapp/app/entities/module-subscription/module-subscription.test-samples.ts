import dayjs from 'dayjs/esm';

import { IModuleSubscription, NewModuleSubscription } from './module-subscription.model';

export const sampleWithRequiredData: IModuleSubscription = {
  id: 72357,
  subscriptionDate: dayjs('2024-12-27T16:27'),
  active: false,
};

export const sampleWithPartialData: IModuleSubscription = {
  id: 72742,
  subscriptionDate: dayjs('2024-12-27T08:13'),
  active: true,
};

export const sampleWithFullData: IModuleSubscription = {
  id: 6136,
  subscriptionDate: dayjs('2024-12-27T02:17'),
  active: true,
};

export const sampleWithNewData: NewModuleSubscription = {
  subscriptionDate: dayjs('2024-12-27T05:12'),
  active: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
