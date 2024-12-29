import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IModuleSubscription } from '../module-subscription.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../module-subscription.test-samples';

import { ModuleSubscriptionService, RestModuleSubscription } from './module-subscription.service';

const requireRestSample: RestModuleSubscription = {
  ...sampleWithRequiredData,
  subscriptionDate: sampleWithRequiredData.subscriptionDate?.toJSON(),
};

describe('ModuleSubscription Service', () => {
  let service: ModuleSubscriptionService;
  let httpMock: HttpTestingController;
  let expectedResult: IModuleSubscription | IModuleSubscription[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ModuleSubscriptionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ModuleSubscription', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const moduleSubscription = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(moduleSubscription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ModuleSubscription', () => {
      const moduleSubscription = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(moduleSubscription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ModuleSubscription', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ModuleSubscription', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ModuleSubscription', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addModuleSubscriptionToCollectionIfMissing', () => {
      it('should add a ModuleSubscription to an empty array', () => {
        const moduleSubscription: IModuleSubscription = sampleWithRequiredData;
        expectedResult = service.addModuleSubscriptionToCollectionIfMissing([], moduleSubscription);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moduleSubscription);
      });

      it('should not add a ModuleSubscription to an array that contains it', () => {
        const moduleSubscription: IModuleSubscription = sampleWithRequiredData;
        const moduleSubscriptionCollection: IModuleSubscription[] = [
          {
            ...moduleSubscription,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addModuleSubscriptionToCollectionIfMissing(moduleSubscriptionCollection, moduleSubscription);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ModuleSubscription to an array that doesn't contain it", () => {
        const moduleSubscription: IModuleSubscription = sampleWithRequiredData;
        const moduleSubscriptionCollection: IModuleSubscription[] = [sampleWithPartialData];
        expectedResult = service.addModuleSubscriptionToCollectionIfMissing(moduleSubscriptionCollection, moduleSubscription);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moduleSubscription);
      });

      it('should add only unique ModuleSubscription to an array', () => {
        const moduleSubscriptionArray: IModuleSubscription[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const moduleSubscriptionCollection: IModuleSubscription[] = [sampleWithRequiredData];
        expectedResult = service.addModuleSubscriptionToCollectionIfMissing(moduleSubscriptionCollection, ...moduleSubscriptionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const moduleSubscription: IModuleSubscription = sampleWithRequiredData;
        const moduleSubscription2: IModuleSubscription = sampleWithPartialData;
        expectedResult = service.addModuleSubscriptionToCollectionIfMissing([], moduleSubscription, moduleSubscription2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moduleSubscription);
        expect(expectedResult).toContain(moduleSubscription2);
      });

      it('should accept null and undefined values', () => {
        const moduleSubscription: IModuleSubscription = sampleWithRequiredData;
        expectedResult = service.addModuleSubscriptionToCollectionIfMissing([], null, moduleSubscription, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moduleSubscription);
      });

      it('should return initial array if no ModuleSubscription is added', () => {
        const moduleSubscriptionCollection: IModuleSubscription[] = [sampleWithRequiredData];
        expectedResult = service.addModuleSubscriptionToCollectionIfMissing(moduleSubscriptionCollection, undefined, null);
        expect(expectedResult).toEqual(moduleSubscriptionCollection);
      });
    });

    describe('compareModuleSubscription', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareModuleSubscription(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareModuleSubscription(entity1, entity2);
        const compareResult2 = service.compareModuleSubscription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareModuleSubscription(entity1, entity2);
        const compareResult2 = service.compareModuleSubscription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareModuleSubscription(entity1, entity2);
        const compareResult2 = service.compareModuleSubscription(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
