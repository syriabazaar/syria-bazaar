import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CarModelUpdate from './car-model-update.vue';
import CarModelService from './car-model.service';
import AlertService from '@/shared/alert/alert.service';

import BrandService from '@/entities/brand/brand.service';

type CarModelUpdateComponentType = InstanceType<typeof CarModelUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const carModelSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CarModelUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CarModel Management Update Component', () => {
    let comp: CarModelUpdateComponentType;
    let carModelServiceStub: SinonStubbedInstance<CarModelService>;

    beforeEach(() => {
      route = {};
      carModelServiceStub = sinon.createStubInstance<CarModelService>(CarModelService);
      carModelServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          carModelService: () => carModelServiceStub,
          brandService: () =>
            sinon.createStubInstance<BrandService>(BrandService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CarModelUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.carModel = carModelSample;
        carModelServiceStub.update.resolves(carModelSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(carModelServiceStub.update.calledWith(carModelSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        carModelServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CarModelUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.carModel = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(carModelServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        carModelServiceStub.find.resolves(carModelSample);
        carModelServiceStub.retrieve.resolves([carModelSample]);

        // WHEN
        route = {
          params: {
            carModelId: `${carModelSample.id}`,
          },
        };
        const wrapper = shallowMount(CarModelUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.carModel).toMatchObject(carModelSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        carModelServiceStub.find.resolves(carModelSample);
        const wrapper = shallowMount(CarModelUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
