import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILicense } from '../license.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../license.test-samples';

import { LicenseService, RestLicense } from './license.service';

const requireRestSample: RestLicense = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
};

describe('License Service', () => {
  let service: LicenseService;
  let httpMock: HttpTestingController;
  let expectedResult: ILicense | ILicense[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LicenseService);
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

    it('should create a License', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const license = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(license).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a License', () => {
      const license = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(license).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a License', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of License', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a License', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLicenseToCollectionIfMissing', () => {
      it('should add a License to an empty array', () => {
        const license: ILicense = sampleWithRequiredData;
        expectedResult = service.addLicenseToCollectionIfMissing([], license);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(license);
      });

      it('should not add a License to an array that contains it', () => {
        const license: ILicense = sampleWithRequiredData;
        const licenseCollection: ILicense[] = [
          {
            ...license,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLicenseToCollectionIfMissing(licenseCollection, license);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a License to an array that doesn't contain it", () => {
        const license: ILicense = sampleWithRequiredData;
        const licenseCollection: ILicense[] = [sampleWithPartialData];
        expectedResult = service.addLicenseToCollectionIfMissing(licenseCollection, license);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(license);
      });

      it('should add only unique License to an array', () => {
        const licenseArray: ILicense[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const licenseCollection: ILicense[] = [sampleWithRequiredData];
        expectedResult = service.addLicenseToCollectionIfMissing(licenseCollection, ...licenseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const license: ILicense = sampleWithRequiredData;
        const license2: ILicense = sampleWithPartialData;
        expectedResult = service.addLicenseToCollectionIfMissing([], license, license2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(license);
        expect(expectedResult).toContain(license2);
      });

      it('should accept null and undefined values', () => {
        const license: ILicense = sampleWithRequiredData;
        expectedResult = service.addLicenseToCollectionIfMissing([], null, license, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(license);
      });

      it('should return initial array if no License is added', () => {
        const licenseCollection: ILicense[] = [sampleWithRequiredData];
        expectedResult = service.addLicenseToCollectionIfMissing(licenseCollection, undefined, null);
        expect(expectedResult).toEqual(licenseCollection);
      });
    });

    describe('compareLicense', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLicense(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLicense(entity1, entity2);
        const compareResult2 = service.compareLicense(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLicense(entity1, entity2);
        const compareResult2 = service.compareLicense(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLicense(entity1, entity2);
        const compareResult2 = service.compareLicense(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
