import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeeFormService, EmployeeFormGroup } from './employee-form.service';
import { IEmployee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { UserRole } from 'app/entities/enumerations/user-role.model';
import { IModule } from '../../module/module.model';
import { IClient } from '../../client/client.model';
import { ClientService } from '../../client/service/client.service';
import { ModuleService } from '../../module/service/module.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  employee: IEmployee | null = null;
  userRoleValues = Object.keys(UserRole);

  modulesSharedCollection: IModule[] = [];
  clientsSharedCollection: IClient[] = [];

  editForm: EmployeeFormGroup = this.employeeFormService.createEmployeeFormGroup();

  constructor(
    protected employeeService: EmployeeService,
    protected employeeFormService: EmployeeFormService,
    protected moduleService: ModuleService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareModule = (o1: IModule | null, o2: IModule | null): boolean => this.moduleService.compareModule(o1, o2);

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.employee = employee;
      if (employee) {
        this.updateForm(employee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.employeeFormService.getEmployee(this.editForm);
    if (employee.id !== null) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.employee = employee;
    this.employeeFormService.resetForm(this.editForm, employee);

    this.modulesSharedCollection = this.moduleService.addModuleToCollectionIfMissing<IModule>(
      this.modulesSharedCollection,
      ...(employee.accessRights ?? [])
    );
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing<IClient>(
      this.clientsSharedCollection,
      employee.client
    );
  }

  protected loadRelationshipsOptions(): void {
    this.moduleService
      .query()
      .pipe(map((res: HttpResponse<IModule[]>) => res.body ?? []))
      .pipe(
        map((modules: IModule[]) =>
          this.moduleService.addModuleToCollectionIfMissing<IModule>(modules, ...(this.employee?.accessRights ?? []))
        )
      )
      .subscribe((modules: IModule[]) => (this.modulesSharedCollection = modules));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.employee?.client)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }
}
