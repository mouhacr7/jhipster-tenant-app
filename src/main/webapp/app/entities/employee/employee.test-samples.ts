import { UserRole } from 'app/entities/enumerations/user-role.model';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 7813,
  firstName: 'Francine',
  lastName: 'Le roux',
  email: 'Axeline_Dasilva@hotmail.fr',
  role: UserRole['EMPLOYEE'],
};

export const sampleWithPartialData: IEmployee = {
  id: 56543,
  firstName: 'Adenet',
  lastName: 'Cousin',
  email: 'Hlne50@yahoo.fr',
  phone: '+33 742842935',
  role: UserRole['MANAGER'],
};

export const sampleWithFullData: IEmployee = {
  id: 44612,
  firstName: 'Taurin',
  lastName: 'Garnier',
  email: 'Barbe31@yahoo.fr',
  phone: '+33 449983695',
  role: UserRole['MANAGER'],
};

export const sampleWithNewData: NewEmployee = {
  firstName: 'Adéodat',
  lastName: 'Guyot',
  email: 'Marion.Nguyen63@hotmail.fr',
  role: UserRole['EMPLOYEE'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
