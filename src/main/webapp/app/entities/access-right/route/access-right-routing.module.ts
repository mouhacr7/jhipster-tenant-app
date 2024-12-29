import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccessRightComponent } from '../list/access-right.component';
import { AccessRightDetailComponent } from '../detail/access-right-detail.component';
import { AccessRightUpdateComponent } from '../update/access-right-update.component';
import { AccessRightRoutingResolveService } from './access-right-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const accessRightRoute: Routes = [
  {
    path: '',
    component: AccessRightComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccessRightDetailComponent,
    resolve: {
      accessRight: AccessRightRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccessRightUpdateComponent,
    resolve: {
      accessRight: AccessRightRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccessRightUpdateComponent,
    resolve: {
      accessRight: AccessRightRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accessRightRoute)],
  exports: [RouterModule],
})
export class AccessRightRoutingModule {}
