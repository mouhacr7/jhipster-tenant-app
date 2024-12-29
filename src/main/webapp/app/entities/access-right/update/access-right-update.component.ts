import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AccessRightFormService, AccessRightFormGroup } from './access-right-form.service';
import { IAccessRight } from '../access-right.model';
import { AccessRightService } from '../service/access-right.service';
import { IEmployee } from '../../employee/employee.model';
import { IModule } from '../../module/module.model';
import { ModuleService } from '../../module/service/module.service';
import { EmployeeService } from '../../employee/service/employee.service';

@Component({
  selector: 'jhi-access-right-update',
  templateUrl: './access-right-update.component.html',
})
export class AccessRightUpdateComponent implements OnInit {
  isSaving = false;
  accessRight: IAccessRight | null = null;

  modulesSharedCollection: IModule[] = [];
  employeesSharedCollection: IEmployee[] = [];

  editForm: AccessRightFormGroup = this.accessRightFormService.createAccessRightFormGroup();

  constructor(
    protected accessRightService: AccessRightService,
    protected accessRightFormService: AccessRightFormService,
    protected moduleService: ModuleService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareModule = (o1: IModule | null, o2: IModule | null): boolean => this.moduleService.compareModule(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accessRight }) => {
      this.accessRight = accessRight;
      if (accessRight) {
        this.updateForm(accessRight);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accessRight = this.accessRightFormService.getAccessRight(this.editForm);
    if (accessRight.id !== null) {
      this.subscribeToSaveResponse(this.accessRightService.update(accessRight));
    } else {
      this.subscribeToSaveResponse(this.accessRightService.create(accessRight));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccessRight>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(accessRight: IAccessRight): void {
    this.accessRight = accessRight;
    this.accessRightFormService.resetForm(this.editForm, accessRight);

    this.modulesSharedCollection = this.moduleService.addModuleToCollectionIfMissing<IModule>(
      this.modulesSharedCollection,
      accessRight.module
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      accessRight.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.moduleService
      .query()
      .pipe(map((res: HttpResponse<IModule[]>) => res.body ?? []))
      .pipe(map((modules: IModule[]) => this.moduleService.addModuleToCollectionIfMissing<IModule>(modules, this.accessRight?.module)))
      .subscribe((modules: IModule[]) => (this.modulesSharedCollection = modules));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.accessRight?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
