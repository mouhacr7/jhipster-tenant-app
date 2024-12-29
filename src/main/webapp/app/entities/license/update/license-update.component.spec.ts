import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LicenseFormService } from './license-form.service';
import { LicenseService } from '../service/license.service';
import { ILicense } from '../license.model';
import { IModule } from 'app/entities/module/module.model';
import { ModuleService } from 'app/entities/module/service/module.service';

import { LicenseUpdateComponent } from './license-update.component';

describe('License Management Update Component', () => {
  let comp: LicenseUpdateComponent;
  let fixture: ComponentFixture<LicenseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let licenseFormService: LicenseFormService;
  let licenseService: LicenseService;
  let moduleService: ModuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LicenseUpdateComponent],
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
      .overrideTemplate(LicenseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LicenseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    licenseFormService = TestBed.inject(LicenseFormService);
    licenseService = TestBed.inject(LicenseService);
    moduleService = TestBed.inject(ModuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Module query and add missing value', () => {
      const license: ILicense = { id: 456 };
      const module: IModule = { id: 68201 };
      license.module = module;

      const moduleCollection: IModule[] = [{ id: 19648 }];
      jest.spyOn(moduleService, 'query').mockReturnValue(of(new HttpResponse({ body: moduleCollection })));
      const additionalModules = [module];
      const expectedCollection: IModule[] = [...additionalModules, ...moduleCollection];
      jest.spyOn(moduleService, 'addModuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ license });
      comp.ngOnInit();

      expect(moduleService.query).toHaveBeenCalled();
      expect(moduleService.addModuleToCollectionIfMissing).toHaveBeenCalledWith(
        moduleCollection,
        ...additionalModules.map(expect.objectContaining)
      );
      expect(comp.modulesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const license: ILicense = { id: 456 };
      const module: IModule = { id: 77971 };
      license.module = module;

      activatedRoute.data = of({ license });
      comp.ngOnInit();

      expect(comp.modulesSharedCollection).toContain(module);
      expect(comp.license).toEqual(license);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILicense>>();
      const license = { id: 123 };
      jest.spyOn(licenseFormService, 'getLicense').mockReturnValue(license);
      jest.spyOn(licenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ license });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: license }));
      saveSubject.complete();

      // THEN
      expect(licenseFormService.getLicense).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(licenseService.update).toHaveBeenCalledWith(expect.objectContaining(license));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILicense>>();
      const license = { id: 123 };
      jest.spyOn(licenseFormService, 'getLicense').mockReturnValue({ id: null });
      jest.spyOn(licenseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ license: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: license }));
      saveSubject.complete();

      // THEN
      expect(licenseFormService.getLicense).toHaveBeenCalled();
      expect(licenseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILicense>>();
      const license = { id: 123 };
      jest.spyOn(licenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ license });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(licenseService.update).toHaveBeenCalled();
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
  });
});
