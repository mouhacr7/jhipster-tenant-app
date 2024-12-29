import { ModuleType } from 'app/entities/enumerations/module-type.model';
import { ModuleCategory } from 'app/entities/enumerations/module-category.model';

import { IModule, NewModule } from './module.model';

export const sampleWithRequiredData: IModule = {
  id: 49866,
  name: 'Fantastic end-to-end b',
  type: ModuleType['GESTION'],
  category: ModuleCategory['TRANSACTIONS'],
  active: false,
};

export const sampleWithPartialData: IModule = {
  id: 1686,
  name: 'b Practical',
  type: ModuleType['GESTION'],
  category: ModuleCategory['CLIENTELE'],
  active: false,
};

export const sampleWithFullData: IModule = {
  id: 38333,
  name: 'high-level',
  description: 'azure Stand-alone',
  type: ModuleType['CLIENTELE'],
  category: ModuleCategory['TRANSACTIONS'],
  active: true,
};

export const sampleWithNewData: NewModule = {
  name: 'b',
  type: ModuleType['TRANSACTIONS'],
  category: ModuleCategory['CLIENTELE'],
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
