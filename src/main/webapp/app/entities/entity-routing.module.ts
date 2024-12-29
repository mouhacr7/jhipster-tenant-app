import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'module',
        data: { pageTitle: 'jhipstertenantappApp.module.home.title' },
        loadChildren: () => import('./module/module.module').then(m => m.LicenseModuleModule),
      },
      {
        path: 'license',
        data: { pageTitle: 'jhipstertenantappApp.license.home.title' },
        loadChildren: () => import('./license/license.module').then(m => m.LicenseLicenseModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'jhipstertenantappApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.LicenseClientModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'jhipstertenantappApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.LicenseEmployeeModule),
      },
      {
        path: 'access-right',
        data: { pageTitle: 'jhipstertenantappApp.accessRight.home.title' },
        loadChildren: () => import('./access-right/access-right.module').then(m => m.LicenseAccessRightModule),
      },
      {
        path: 'module-subscription',
        data: { pageTitle: 'jhipstertenantappApp.moduleSubscription.home.title' },
        loadChildren: () => import('./module-subscription/module-subscription.module').then(m => m.LicenseModuleSubscriptionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
