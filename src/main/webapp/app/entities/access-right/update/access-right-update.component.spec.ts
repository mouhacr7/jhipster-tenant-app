import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AccessRightFormService } from './access-right-form.service';
import { AccessRightService } from '../service/access-right.service';
import { IAccessRight } from '../access-right.model';
import { IModule } from 'app/entities/module/module.model';
import { ModuleService } from 'app/entities/module/service/module.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { AccessRightUpdateComponent } from './access-right-update.component';

describe('AccessRight Management Update Component', () => {
  let comp: AccessRightUpdateComponent;
  let fixture: ComponentFixture<AccessRightUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accessRightFormService: AccessRightFormService;
  let accessRightService: AccessRightService;
  let moduleService: ModuleService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AccessRightUpdateComponent],
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
      .overrideTemplate(AccessRightUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccessRightUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accessRightFormService = TestBed.inject(AccessRightFormService);
    accessRightService = TestBed.inject(AccessRightService);
    moduleService = TestBed.inject(ModuleService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Module query and add missing value', () => {
      const accessRight: IAccessRight = { id: 456 };
      const module: IModule = { id: 17415 };
      accessRight.module = module;

      const moduleCollection: IModule[] = [{ id: 83991 }];
      jest.spyOn(moduleService, 'query').mockReturnValue(of(new HttpResponse({ body: moduleCollection })));
      const additionalModules = [module];
      const expectedCollection: IModule[] = [...additionalModules, ...moduleCollection];
      jest.spyOn(moduleService, 'addModuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accessRight });
      comp.ngOnInit();

      expect(moduleService.query).toHaveBeenCalled();
      expect(moduleService.addModuleToCollectionIfMissing).toHaveBeenCalledWith(
        moduleCollection,
        ...additionalModules.map(expect.objectContaining)
      );
      expect(comp.modulesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const accessRight: IAccessRight = { id: 456 };
      const employee: IEmployee = { id: 61927 };
      accessRight.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 40779 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accessRight });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accessRight: IAccessRight = { id: 456 };
      const module: IModule = { id: 2922 };
      accessRight.module = module;
      const employee: IEmployee = { id: 58531 };
      accessRight.employee = employee;

      activatedRoute.data = of({ accessRight });
      comp.ngOnInit();

      expect(comp.modulesSharedCollection).toContain(module);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.accessRight).toEqual(accessRight);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccessRight>>();
      const accessRight = { id: 123 };
      jest.spyOn(accessRightFormService, 'getAccessRight').mockReturnValue(accessRight);
      jest.spyOn(accessRightService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accessRight });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accessRight }));
      saveSubject.complete();

      // THEN
      expect(accessRightFormService.getAccessRight).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accessRightService.update).toHaveBeenCalledWith(expect.objectContaining(accessRight));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccessRight>>();
      const accessRight = { id: 123 };
      jest.spyOn(accessRightFormService, 'getAccessRight').mockReturnValue({ id: null });
      jest.spyOn(accessRightService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accessRight: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accessRight }));
      saveSubject.complete();

      // THEN
      expect(accessRightFormService.getAccessRight).toHaveBeenCalled();
      expect(accessRightService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccessRight>>();
      const accessRight = { id: 123 };
      jest.spyOn(accessRightService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accessRight });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accessRightService.update).toHaveBeenCalled();
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

    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
