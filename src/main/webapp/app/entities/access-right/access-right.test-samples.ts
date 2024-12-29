import { IAccessRight, NewAccessRight } from './access-right.model';

export const sampleWithRequiredData: IAccessRight = {
  id: 72419,
  canRead: true,
  canWrite: true,
  canDelete: true,
};

export const sampleWithPartialData: IAccessRight = {
  id: 74110,
  canRead: false,
  canWrite: false,
  canDelete: true,
};

export const sampleWithFullData: IAccessRight = {
  id: 99337,
  canRead: true,
  canWrite: false,
  canDelete: false,
};

export const sampleWithNewData: NewAccessRight = {
  canRead: true,
  canWrite: true,
  canDelete: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
