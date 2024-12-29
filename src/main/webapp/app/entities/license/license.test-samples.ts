import dayjs from 'dayjs/esm';

import { ILicense, NewLicense } from './license.model';

export const sampleWithRequiredData: ILicense = {
  id: 86783,
  startDate: dayjs('2024-12-27T13:34'),
  endDate: dayjs('2024-12-26T22:25'),
  active: false,
};

export const sampleWithPartialData: ILicense = {
  id: 52251,
  startDate: dayjs('2024-12-26T21:35'),
  endDate: dayjs('2024-12-27T00:59'),
  active: false,
};

export const sampleWithFullData: ILicense = {
  id: 84759,
  startDate: dayjs('2024-12-27T06:10'),
  endDate: dayjs('2024-12-26T21:33'),
  active: false,
};

export const sampleWithNewData: NewLicense = {
  startDate: dayjs('2024-12-27T04:59'),
  endDate: dayjs('2024-12-27T00:00'),
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
