import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ModuleSubscriptionComponent } from './list/module-subscription.component';
import { ModuleSubscriptionDetailComponent } from './detail/module-subscription-detail.component';
import { ModuleSubscriptionUpdateComponent } from './update/module-subscription-update.component';
import { ModuleSubscriptionDeleteDialogComponent } from './delete/module-subscription-delete-dialog.component';
import { ModuleSubscriptionRoutingModule } from './route/module-subscription-routing.module';

@NgModule({
  imports: [SharedModule, ModuleSubscriptionRoutingModule],
  declarations: [
    ModuleSubscriptionComponent,
    ModuleSubscriptionDetailComponent,
    ModuleSubscriptionUpdateComponent,
    ModuleSubscriptionDeleteDialogComponent,
  ],
})
export class LicenseModuleSubscriptionModule {}
