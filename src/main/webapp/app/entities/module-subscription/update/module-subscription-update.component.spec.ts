import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ModuleSubscriptionFormService } from './module-subscription-form.service';
import { ModuleSubscriptionService } from '../service/module-subscription.service';
import { IModuleSubscription } from '../module-subscription.model';
import { IModule } from 'app/entities/module/module.model';
import { ModuleService } from 'app/entities/module/service/module.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { ModuleSubscriptionUpdateComponent } from './module-subscription-update.component';

describe('ModuleSubscription Management Update Component', () => {
  let comp: ModuleSubscriptionUpdateComponent;
  let fixture: ComponentFixture<ModuleSubscriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let moduleSubscriptionFormService: ModuleSubscriptionFormService;
  let moduleSubscriptionService: ModuleSubscriptionService;
  let moduleService: ModuleService;
  let clientService: ClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ModuleSubscriptionUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ModuleSubscriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ModuleSubscriptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moduleSubscriptionFormService = TestBed.inject(ModuleSubscriptionFormService);
    moduleSubscriptionService = TestBed.inject(ModuleSubscriptionService);
    moduleService = TestBed.inject(ModuleService);
    clientService = TestBed.inject(ClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Module query and add missing value', () => {
      const moduleSubscription: IModuleSubscription = { id: 456 };
      const module: IModule = { id: 85989 };
      moduleSubscription.module = module;

      const moduleCollection: IModule[] = [{ id: 94330 }];
      jest.spyOn(moduleService, 'query').mockReturnValue(of(new HttpResponse({ body: moduleCollection })));
      const additionalModules = [module];
      const expectedCollection: IModule[] = [...additionalModules, ...moduleCollection];
      jest.spyOn(moduleService, 'addModuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ moduleSubscription });
      comp.ngOnInit();

      expect(moduleService.query).toHaveBeenCalled();
      expect(moduleService.addModuleToCollectionIfMissing).toHaveBeenCalledWith(
        moduleCollection,
        ...additionalModules.map(expect.objectContaining)
      );
      expect(comp.modulesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Client query and add missing value', () => {
      const moduleSubscription: IModuleSubscription = { id: 456 };
      const client: IClient = { id: 94516 };
      moduleSubscription.client = client;

      const clientCollection: IClient[] = [{ id: 79536 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ moduleSubscription });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(expect.objectContaining)
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const moduleSubscription: IModuleSubscription = { id: 456 };
      const module: IModule = { id: 24332 };
      moduleSubscription.module = module;
      const client: IClient = { id: 75133 };
      moduleSubscription.client = client;

      activatedRoute.data = of({ moduleSubscription });
      comp.ngOnInit();

      expect(comp.modulesSharedCollection).toContain(module);
      expect(comp.clientsSharedCollection).toContain(client);
      expect(comp.moduleSubscription).toEqual(moduleSubscription);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IModuleSubscription>>();
      const moduleSubscription = { id: 123 };
      jest.spyOn(moduleSubscriptionFormService, 'getModuleSubscription').mockReturnValue(moduleSubscription);
      jest.spyOn(moduleSubscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moduleSubscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moduleSubscription }));
      saveSubject.complete();

      // THEN
      expect(moduleSubscriptionFormService.getModuleSubscription).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(moduleSubscriptionService.update).toHaveBeenCalledWith(expect.objectContaining(moduleSubscription));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IModuleSubscription>>();
      const moduleSubscription = { id: 123 };
      jest.spyOn(moduleSubscriptionFormService, 'getModuleSubscription').mockReturnValue({ id: null });
      jest.spyOn(moduleSubscriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moduleSubscription: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moduleSubscription }));
      saveSubject.complete();

      // THEN
      expect(moduleSubscriptionFormService.getModuleSubscription).toHaveBeenCalled();
      expect(moduleSubscriptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IModuleSubscription>>();
      const moduleSubscription = { id: 123 };
      jest.spyOn(moduleSubscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moduleSubscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moduleSubscriptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareModule', () => {
      it('Should forward to moduleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(moduleService, 'compareModule');
        comp.compareModule(entity, entity2);
        expect(moduleService.compareModule).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareClient', () => {
      it('Should forward to clientService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
