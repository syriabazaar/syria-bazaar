import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import CarService from './car.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Car } from '@/shared/model/car.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Car Service', () => {
    let service: CarService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new CarService();
      currentDate = new Date();
      elemDefault = new Car(
        123,
        0,
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        0,
        0,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { publishedDate: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Car', async () => {
        const returnedFromService = { id: 123, publishedDate: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { publishedDate: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Car', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Car', async () => {
        const returnedFromService = {
          year: 1,
          price: 1,
          mileage: 1,
          drivetrain: 'BBBBBB',
          engine: 'BBBBBB',
          transmission: 'BBBBBB',
          fuelType: 'BBBBBB',
          exteriorColor: 'BBBBBB',
          interiorColor: 'BBBBBB',
          vin: 'BBBBBB',
          location: 'BBBBBB',
          description: 'BBBBBB',
          publishedDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          adNumber: 1,
          views: 1,
          alloyWheels: true,
          sunroof: true,
          tintedGlass: true,
          ledHeadlights: true,
          foldableRoof: true,
          towHitch: true,
          adjustableSteeringWheel: true,
          autoDimmingRearview: true,
          heatedFrontSeats: true,
          leatherSeats: true,
          blindSpotMonitor: true,
          adaptiveCruiseControl: true,
          navigationSystem: true,
          backupCamera: true,
          appleCarplay: true,
          androidAuto: true,
          premiumSoundSystem: true,
          isFirstOwn: true,
          isAccedFree: true,
          ...elemDefault,
        };

        const expected = { publishedDate: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Car', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Car', async () => {
        const patchObject = {
          mileage: 1,
          drivetrain: 'BBBBBB',
          engine: 'BBBBBB',
          transmission: 'BBBBBB',
          exteriorColor: 'BBBBBB',
          interiorColor: 'BBBBBB',
          description: 'BBBBBB',
          adNumber: 1,
          tintedGlass: true,
          leatherSeats: true,
          blindSpotMonitor: true,
          adaptiveCruiseControl: true,
          backupCamera: true,
          androidAuto: true,
          premiumSoundSystem: true,
          isAccedFree: true,
          ...new Car(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { publishedDate: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Car', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Car', async () => {
        const returnedFromService = {
          year: 1,
          price: 1,
          mileage: 1,
          drivetrain: 'BBBBBB',
          engine: 'BBBBBB',
          transmission: 'BBBBBB',
          fuelType: 'BBBBBB',
          exteriorColor: 'BBBBBB',
          interiorColor: 'BBBBBB',
          vin: 'BBBBBB',
          location: 'BBBBBB',
          description: 'BBBBBB',
          publishedDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          adNumber: 1,
          views: 1,
          alloyWheels: true,
          sunroof: true,
          tintedGlass: true,
          ledHeadlights: true,
          foldableRoof: true,
          towHitch: true,
          adjustableSteeringWheel: true,
          autoDimmingRearview: true,
          heatedFrontSeats: true,
          leatherSeats: true,
          blindSpotMonitor: true,
          adaptiveCruiseControl: true,
          navigationSystem: true,
          backupCamera: true,
          appleCarplay: true,
          androidAuto: true,
          premiumSoundSystem: true,
          isFirstOwn: true,
          isAccedFree: true,
          ...elemDefault,
        };
        const expected = { publishedDate: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Car', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Car', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Car', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
