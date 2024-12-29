import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccessRight } from '../access-right.model';
import { AccessRightService } from '../service/access-right.service';

@Injectable({ providedIn: 'root' })
export class AccessRightRoutingResolveService implements Resolve<IAccessRight | null> {
  constructor(protected service: AccessRightService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAccessRight | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((accessRight: HttpResponse<IAccessRight>) => {
          if (accessRight.body) {
            return of(accessRight.body);
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
