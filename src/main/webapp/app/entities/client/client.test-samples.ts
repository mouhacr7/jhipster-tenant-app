import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: 71655,
  name: 'hacking',
  email: 'Judical_Simon@hotmail.fr',
};

export const sampleWithPartialData: IClient = {
  id: 11249,
  name: 'reboot',
  email: 'Agnan_Lemoine90@gmail.com',
  phone: '0441242145',
  address: 'c ability input',
};

export const sampleWithFullData: IClient = {
  id: 7148,
  name: 'Cotton silver',
  email: 'Ysaline.Gerard@hotmail.fr',
  phone: '0610770941',
  address: 'yellow monitor',
};

export const sampleWithNewData: NewClient = {
  name: 'contextually-based',
  email: 'Romo_Leroy71@gmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
