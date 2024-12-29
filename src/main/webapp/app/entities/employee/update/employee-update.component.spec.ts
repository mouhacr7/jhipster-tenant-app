import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeFormService } from './employee-form.service';
import { EmployeeService } from '../service/employee.service';
import { IEmployee } from '../employee.model';
import { IModule } from 'app/entities/module/module.model';
import { ModuleService } from 'app/entities/module/service/module.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { EmployeeUpdateComponent } from './employee-update.component';

describe('Employee Management Update Component', () => {
  let comp: EmployeeUpdateComponent;
  let fixture: ComponentFixture<EmployeeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeFormService: EmployeeFormService;
  let employeeService: EmployeeService;
  let moduleService: ModuleService;
  let clientService: ClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeUpdateComponent],
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
      .overrideTemplate(EmployeeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeFormService = TestBed.inject(EmployeeFormService);
    employeeService = TestBed.inject(EmployeeService);
    moduleService = TestBed.inject(ModuleService);
    clientService = TestBed.inject(ClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Module query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const accessRights: IModule[] = [{ id: 10951 }];
      employee.accessRights = accessRights;

      const moduleCollection: IModule[] = [{ id: 12934 }];
      jest.spyOn(moduleService, 'query').mockReturnValue(of(new HttpResponse({ body: moduleCollection })));
      const additionalModules = [...accessRights];
      const expectedCollection: IModule[] = [...additionalModules, ...moduleCollection];
      jest.spyOn(moduleService, 'addModuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(moduleService.query).toHaveBeenCalled();
      expect(moduleService.addModuleToCollectionIfMissing).toHaveBeenCalledWith(
        moduleCollection,
        ...additionalModules.map(expect.objectContaining)
      );
      expect(comp.modulesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Client query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const client: IClient = { id: 68993 };
      employee.client = client;

      const clientCollection: IClient[] = [{ id: 3363 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(expect.objectContaining)
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employee: IEmployee = { id: 456 };
      const accessRights: IModule = { id: 19357 };
      employee.accessRights = [accessRights];
      const client: IClient = { id: 98107 };
      employee.client = client;

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(comp.modulesSharedCollection).toContain(accessRights);
      expect(comp.clientsSharedCollection).toContain(client);
      expect(comp.employee).toEqual(employee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeFormService, 'getEmployee').mockReturnValue(employee);
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeFormService.getEmployee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeService.update).toHaveBeenCalledWith(expect.objectContaining(employee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeFormService, 'getEmployee').mockReturnValue({ id: null });
      jest.spyOn(employeeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeFormService.getEmployee).toHaveBeenCalled();
      expect(employeeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeService.update).toHaveBeenCalled();
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
