import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ModuleSubscriptionFormService, ModuleSubscriptionFormGroup } from './module-subscription-form.service';
import { IModuleSubscription } from '../module-subscription.model';
import { ModuleSubscriptionService } from '../service/module-subscription.service';
import { IModule } from '../../module/module.model';
import { IClient } from '../../client/client.model';
import { ModuleService } from '../../module/service/module.service';
import { ClientService } from '../../client/service/client.service';

@Component({
  selector: 'jhi-module-subscription-update',
  templateUrl: './module-subscription-update.component.html',
})
export class ModuleSubscriptionUpdateComponent implements OnInit {
  isSaving = false;
  moduleSubscription: IModuleSubscription | null = null;

  modulesSharedCollection: IModule[] = [];
  clientsSharedCollection: IClient[] = [];

  editForm: ModuleSubscriptionFormGroup = this.moduleSubscriptionFormService.createModuleSubscriptionFormGroup();

  constructor(
    protected moduleSubscriptionService: ModuleSubscriptionService,
    protected moduleSubscriptionFormService: ModuleSubscriptionFormService,
    protected moduleService: ModuleService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareModule = (o1: IModule | null, o2: IModule | null): boolean => this.moduleService.compareModule(o1, o2);

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moduleSubscription }) => {
      this.moduleSubscription = moduleSubscription;
      if (moduleSubscription) {
        this.updateForm(moduleSubscription);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const moduleSubscription = this.moduleSubscriptionFormService.getModuleSubscription(this.editForm);
    if (moduleSubscription.id !== null) {
      this.subscribeToSaveResponse(this.moduleSubscriptionService.update(moduleSubscription));
    } else {
      this.subscribeToSaveResponse(this.moduleSubscriptionService.create(moduleSubscription));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IModuleSubscription>>): void {
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

  protected updateForm(moduleSubscription: IModuleSubscription): void {
    this.moduleSubscription = moduleSubscription;
    this.moduleSubscriptionFormService.resetForm(this.editForm, moduleSubscription);

    this.modulesSharedCollection = this.moduleService.addModuleToCollectionIfMissing<IModule>(
      this.modulesSharedCollection,
      moduleSubscription.module
    );
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing<IClient>(
      this.clientsSharedCollection,
      moduleSubscription.client
    );
  }

  protected loadRelationshipsOptions(): void {
    this.moduleService
      .query()
      .pipe(map((res: HttpResponse<IModule[]>) => res.body ?? []))
      .pipe(
        map((modules: IModule[]) => this.moduleService.addModuleToCollectionIfMissing<IModule>(modules, this.moduleSubscription?.module))
      )
      .subscribe((modules: IModule[]) => (this.modulesSharedCollection = modules));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(
        map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.moduleSubscription?.client))
      )
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }
}
