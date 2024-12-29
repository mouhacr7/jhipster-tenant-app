import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILicense, NewLicense } from '../license.model';

export type PartialUpdateLicense = Partial<ILicense> & Pick<ILicense, 'id'>;

type RestOf<T extends ILicense | NewLicense> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestLicense = RestOf<ILicense>;

export type NewRestLicense = RestOf<NewLicense>;

export type PartialUpdateRestLicense = RestOf<PartialUpdateLicense>;

export type EntityResponseType = HttpResponse<ILicense>;
export type EntityArrayResponseType = HttpResponse<ILicense[]>;

@Injectable({ providedIn: 'root' })
export class LicenseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/licenses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(license: NewLicense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(license);
    return this.http
      .post<RestLicense>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(license: ILicense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(license);
    return this.http
      .put<RestLicense>(`${this.resourceUrl}/${this.getLicenseIdentifier(license)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(license: PartialUpdateLicense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(license);
    return this.http
      .patch<RestLicense>(`${this.resourceUrl}/${this.getLicenseIdentifier(license)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLicense>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLicense[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLicenseIdentifier(license: Pick<ILicense, 'id'>): number {
    return license.id;
  }

  compareLicense(o1: Pick<ILicense, 'id'> | null, o2: Pick<ILicense, 'id'> | null): boolean {
    return o1 && o2 ? this.getLicenseIdentifier(o1) === this.getLicenseIdentifier(o2) : o1 === o2;
  }

  addLicenseToCollectionIfMissing<Type extends Pick<ILicense, 'id'>>(
    licenseCollection: Type[],
    ...licensesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const licenses: Type[] = licensesToCheck.filter(isPresent);
    if (licenses.length > 0) {
      const licenseCollectionIdentifiers = licenseCollection.map(licenseItem => this.getLicenseIdentifier(licenseItem)!);
      const licensesToAdd = licenses.filter(licenseItem => {
        const licenseIdentifier = this.getLicenseIdentifier(licenseItem);
        if (licenseCollectionIdentifiers.includes(licenseIdentifier)) {
          return false;
        }
        licenseCollectionIdentifiers.push(licenseIdentifier);
        return true;
      });
      return [...licensesToAdd, ...licenseCollection];
    }
    return licenseCollection;
  }

  protected convertDateFromClient<T extends ILicense | NewLicense | PartialUpdateLicense>(license: T): RestOf<T> {
    return {
      ...license,
      startDate: license.startDate?.toJSON() ?? null,
      endDate: license.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLicense: RestLicense): ILicense {
    return {
      ...restLicense,
      startDate: restLicense.startDate ? dayjs(restLicense.startDate) : undefined,
      endDate: restLicense.endDate ? dayjs(restLicense.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLicense>): HttpResponse<ILicense> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLicense[]>): HttpResponse<ILicense[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
