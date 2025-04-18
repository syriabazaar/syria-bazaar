import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CarTypeDetails from './car-type-details.vue';
import CarTypeService from './car-type.service';
import AlertService from '@/shared/alert/alert.service';

type CarTypeDetailsComponentType = InstanceType<typeof CarTypeDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const carTypeSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CarType Management Detail Component', () => {
    let carTypeServiceStub: SinonStubbedInstance<CarTypeService>;
    let mountOptions: MountingOptions<CarTypeDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      carTypeServiceStub = sinon.createStubInstance<CarTypeService>(CarTypeService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          carTypeService: () => carTypeServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        carTypeServiceStub.find.resolves(carTypeSample);
        route = {
          params: {
            carTypeId: `${123}`,
          },
        };
        const wrapper = shallowMount(CarTypeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.carType).toMatchObject(carTypeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        carTypeServiceStub.find.resolves(carTypeSample);
        const wrapper = shallowMount(CarTypeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
