import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AccessRightComponent } from './list/access-right.component';
import { AccessRightDetailComponent } from './detail/access-right-detail.component';
import { AccessRightUpdateComponent } from './update/access-right-update.component';
import { AccessRightDeleteDialogComponent } from './delete/access-right-delete-dialog.component';
import { AccessRightRoutingModule } from './route/access-right-routing.module';

@NgModule({
  imports: [SharedModule, AccessRightRoutingModule],
  declarations: [AccessRightComponent, AccessRightDetailComponent, AccessRightUpdateComponent, AccessRightDeleteDialogComponent],
})
export class LicenseAccessRightModule {}
