import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LicenseFormService, LicenseFormGroup } from './license-form.service';
import { ILicense } from '../license.model';
import { LicenseService } from '../service/license.service';
import { ModuleService } from '../../module/service/module.service';
import { IModule } from '../../module/module.model';

@Component({
  selector: 'jhi-license-update',
  templateUrl: './license-update.component.html',
})
export class LicenseUpdateComponent implements OnInit {
  isSaving = false;
  license: ILicense | null = null;

  modulesSharedCollection: IModule[] = [];

  editForm: LicenseFormGroup = this.licenseFormService.createLicenseFormGroup();

  constructor(
    protected licenseService: LicenseService,
    protected licenseFormService: LicenseFormService,
    protected moduleService: ModuleService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareModule = (o1: IModule | null, o2: IModule | null): boolean => this.moduleService.compareModule(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ license }) => {
      this.license = license;
      if (license) {
        this.updateForm(license);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const license = this.licenseFormService.getLicense(this.editForm);
    if (license.id !== null) {
      this.subscribeToSaveResponse(this.licenseService.update(license));
    } else {
      this.subscribeToSaveResponse(this.licenseService.create(license));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILicense>>): void {
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

  protected updateForm(license: ILicense): void {
    this.license = license;
    this.licenseFormService.resetForm(this.editForm, license);

    this.modulesSharedCollection = this.moduleService.addModuleToCollectionIfMissing<IModule>(this.modulesSharedCollection, license.module);
  }

  protected loadRelationshipsOptions(): void {
    this.moduleService
      .query()
      .pipe(map((res: HttpResponse<IModule[]>) => res.body ?? []))
      .pipe(map((modules: IModule[]) => this.moduleService.addModuleToCollectionIfMissing<IModule>(modules, this.license?.module)))
      .subscribe((modules: IModule[]) => (this.modulesSharedCollection = modules));
  }
}
