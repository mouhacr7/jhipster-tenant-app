import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccessRight, NewAccessRight } from '../access-right.model';

export type PartialUpdateAccessRight = Partial<IAccessRight> & Pick<IAccessRight, 'id'>;

export type EntityResponseType = HttpResponse<IAccessRight>;
export type EntityArrayResponseType = HttpResponse<IAccessRight[]>;

@Injectable({ providedIn: 'root' })
export class AccessRightService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/access-rights');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(accessRight: NewAccessRight): Observable<EntityResponseType> {
    return this.http.post<IAccessRight>(this.resourceUrl, accessRight, { observe: 'response' });
  }

  update(accessRight: IAccessRight): Observable<EntityResponseType> {
    return this.http.put<IAccessRight>(`${this.resourceUrl}/${this.getAccessRightIdentifier(accessRight)}`, accessRight, {
      observe: 'response',
    });
  }

  partialUpdate(accessRight: PartialUpdateAccessRight): Observable<EntityResponseType> {
    return this.http.patch<IAccessRight>(`${this.resourceUrl}/${this.getAccessRightIdentifier(accessRight)}`, accessRight, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccessRight>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccessRight[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAccessRightIdentifier(accessRight: Pick<IAccessRight, 'id'>): number {
    return accessRight.id;
  }

  compareAccessRight(o1: Pick<IAccessRight, 'id'> | null, o2: Pick<IAccessRight, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccessRightIdentifier(o1) === this.getAccessRightIdentifier(o2) : o1 === o2;
  }

  addAccessRightToCollectionIfMissing<Type extends Pick<IAccessRight, 'id'>>(
    accessRightCollection: Type[],
    ...accessRightsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accessRights: Type[] = accessRightsToCheck.filter(isPresent);
    if (accessRights.length > 0) {
      const accessRightCollectionIdentifiers = accessRightCollection.map(
        accessRightItem => this.getAccessRightIdentifier(accessRightItem)!
      );
      const accessRightsToAdd = accessRights.filter(accessRightItem => {
        const accessRightIdentifier = this.getAccessRightIdentifier(accessRightItem);
        if (accessRightCollectionIdentifiers.includes(accessRightIdentifier)) {
          return false;
        }
        accessRightCollectionIdentifiers.push(accessRightIdentifier);
        return true;
      });
      return [...accessRightsToAdd, ...accessRightCollection];
    }
    return accessRightCollection;
  }
}
