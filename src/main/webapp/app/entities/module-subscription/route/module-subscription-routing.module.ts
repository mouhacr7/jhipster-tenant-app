import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ModuleSubscriptionComponent } from '../list/module-subscription.component';
import { ModuleSubscriptionDetailComponent } from '../detail/module-subscription-detail.component';
import { ModuleSubscriptionUpdateComponent } from '../update/module-subscription-update.component';
import { ModuleSubscriptionRoutingResolveService } from './module-subscription-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const moduleSubscriptionRoute: Routes = [
  {
    path: '',
    component: ModuleSubscriptionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ModuleSubscriptionDetailComponent,
    resolve: {
      moduleSubscription: ModuleSubscriptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ModuleSubscriptionUpdateComponent,
    resolve: {
      moduleSubscription: ModuleSubscriptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ModuleSubscriptionUpdateComponent,
    resolve: {
      moduleSubscription: ModuleSubscriptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(moduleSubscriptionRoute)],
  exports: [RouterModule],
})
export class ModuleSubscriptionRoutingModule {}
