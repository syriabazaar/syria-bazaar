import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CarTypeUpdate from './car-type-update.vue';
import CarTypeService from './car-type.service';
import AlertService from '@/shared/alert/alert.service';

type CarTypeUpdateComponentType = InstanceType<typeof CarTypeUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const carTypeSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CarTypeUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CarType Management Update Component', () => {
    let comp: CarTypeUpdateComponentType;
    let carTypeServiceStub: SinonStubbedInstance<CarTypeService>;

    beforeEach(() => {
      route = {};
      carTypeServiceStub = sinon.createStubInstance<CarTypeService>(CarTypeService);
      carTypeServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          carTypeService: () => carTypeServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CarTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.carType = carTypeSample;
        carTypeServiceStub.update.resolves(carTypeSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(carTypeServiceStub.update.calledWith(carTypeSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        carTypeServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CarTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.carType = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(carTypeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        carTypeServiceStub.find.resolves(carTypeSample);
        carTypeServiceStub.retrieve.resolves([carTypeSample]);

        // WHEN
        route = {
          params: {
            carTypeId: `${carTypeSample.id}`,
          },
        };
        const wrapper = shallowMount(CarTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.carType).toMatchObject(carTypeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        carTypeServiceStub.find.resolves(carTypeSample);
        const wrapper = shallowMount(CarTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
