import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import CarUpdate from './car-update.vue';
import CarService from './car.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import BrandService from '@/entities/brand/brand.service';
import SellerService from '@/entities/seller/seller.service';
import CarTypeService from '@/entities/car-type/car-type.service';
import CarModelService from '@/entities/car-model/car-model.service';
import CityService from '@/entities/city/city.service';

type CarUpdateComponentType = InstanceType<typeof CarUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const carSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CarUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Car Management Update Component', () => {
    let comp: CarUpdateComponentType;
    let carServiceStub: SinonStubbedInstance<CarService>;

    beforeEach(() => {
      route = {};
      carServiceStub = sinon.createStubInstance<CarService>(CarService);
      carServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          carService: () => carServiceStub,
          brandService: () =>
            sinon.createStubInstance<BrandService>(BrandService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          sellerService: () =>
            sinon.createStubInstance<SellerService>(SellerService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          carTypeService: () =>
            sinon.createStubInstance<CarTypeService>(CarTypeService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          carModelService: () =>
            sinon.createStubInstance<CarModelService>(CarModelService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          cityService: () =>
            sinon.createStubInstance<CityService>(CityService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(CarUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CarUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.car = carSample;
        carServiceStub.update.resolves(carSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(carServiceStub.update.calledWith(carSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        carServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CarUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.car = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(carServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        carServiceStub.find.resolves(carSample);
        carServiceStub.retrieve.resolves([carSample]);

        // WHEN
        route = {
          params: {
            carId: `${carSample.id}`,
          },
        };
        const wrapper = shallowMount(CarUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.car).toMatchObject(carSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        carServiceStub.find.resolves(carSample);
        const wrapper = shallowMount(CarUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
