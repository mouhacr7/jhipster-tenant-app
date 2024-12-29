import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IModuleSubscription, NewModuleSubscription } from '../module-subscription.model';

export type PartialUpdateModuleSubscription = Partial<IModuleSubscription> & Pick<IModuleSubscription, 'id'>;

type RestOf<T extends IModuleSubscription | NewModuleSubscription> = Omit<T, 'subscriptionDate'> & {
  subscriptionDate?: string | null;
};

export type RestModuleSubscription = RestOf<IModuleSubscription>;

export type NewRestModuleSubscription = RestOf<NewModuleSubscription>;

export type PartialUpdateRestModuleSubscription = RestOf<PartialUpdateModuleSubscription>;

export type EntityResponseType = HttpResponse<IModuleSubscription>;
export type EntityArrayResponseType = HttpResponse<IModuleSubscription[]>;

@Injectable({ providedIn: 'root' })
export class ModuleSubscriptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/module-subscriptions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(moduleSubscription: NewModuleSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moduleSubscription);
    return this.http
      .post<RestModuleSubscription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(moduleSubscription: IModuleSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moduleSubscription);
    return this.http
      .put<RestModuleSubscription>(`${this.resourceUrl}/${this.getModuleSubscriptionIdentifier(moduleSubscription)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(moduleSubscription: PartialUpdateModuleSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moduleSubscription);
    return this.http
      .patch<RestModuleSubscription>(`${this.resourceUrl}/${this.getModuleSubscriptionIdentifier(moduleSubscription)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestModuleSubscription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestModuleSubscription[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getModuleSubscriptionIdentifier(moduleSubscription: Pick<IModuleSubscription, 'id'>): number {
    return moduleSubscription.id;
  }

  compareModuleSubscription(o1: Pick<IModuleSubscription, 'id'> | null, o2: Pick<IModuleSubscription, 'id'> | null): boolean {
    return o1 && o2 ? this.getModuleSubscriptionIdentifier(o1) === this.getModuleSubscriptionIdentifier(o2) : o1 === o2;
  }

  addModuleSubscriptionToCollectionIfMissing<Type extends Pick<IModuleSubscription, 'id'>>(
    moduleSubscriptionCollection: Type[],
    ...moduleSubscriptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const moduleSubscriptions: Type[] = moduleSubscriptionsToCheck.filter(isPresent);
    if (moduleSubscriptions.length > 0) {
      const moduleSubscriptionCollectionIdentifiers = moduleSubscriptionCollection.map(
        moduleSubscriptionItem => this.getModuleSubscriptionIdentifier(moduleSubscriptionItem)!
      );
      const moduleSubscriptionsToAdd = moduleSubscriptions.filter(moduleSubscriptionItem => {
        const moduleSubscriptionIdentifier = this.getModuleSubscriptionIdentifier(moduleSubscriptionItem);
        if (moduleSubscriptionCollectionIdentifiers.includes(moduleSubscriptionIdentifier)) {
          return false;
        }
        moduleSubscriptionCollectionIdentifiers.push(moduleSubscriptionIdentifier);
        return true;
      });
      return [...moduleSubscriptionsToAdd, ...moduleSubscriptionCollection];
    }
    return moduleSubscriptionCollection;
  }

  protected convertDateFromClient<T extends IModuleSubscription | NewModuleSubscription | PartialUpdateModuleSubscription>(
    moduleSubscription: T
  ): RestOf<T> {
    return {
      ...moduleSubscription,
      subscriptionDate: moduleSubscription.subscriptionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restModuleSubscription: RestModuleSubscription): IModuleSubscription {
    return {
      ...restModuleSubscription,
      subscriptionDate: restModuleSubscription.subscriptionDate ? dayjs(restModuleSubscription.subscriptionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestModuleSubscription>): HttpResponse<IModuleSubscription> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestModuleSubscription[]>): HttpResponse<IModuleSubscription[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
