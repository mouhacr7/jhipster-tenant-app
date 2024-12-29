import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IModuleSubscription } from '../module-subscription.model';
import { ModuleSubscriptionService } from '../service/module-subscription.service';

@Injectable({ providedIn: 'root' })
export class ModuleSubscriptionRoutingResolveService implements Resolve<IModuleSubscription | null> {
  constructor(protected service: ModuleSubscriptionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IModuleSubscription | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((moduleSubscription: HttpResponse<IModuleSubscription>) => {
          if (moduleSubscription.body) {
            return of(moduleSubscription.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
