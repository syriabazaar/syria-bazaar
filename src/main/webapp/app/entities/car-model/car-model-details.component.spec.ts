import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CarModelDetails from './car-model-details.vue';
import CarModelService from './car-model.service';
import AlertService from '@/shared/alert/alert.service';

type CarModelDetailsComponentType = InstanceType<typeof CarModelDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const carModelSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CarModel Management Detail Component', () => {
    let carModelServiceStub: SinonStubbedInstance<CarModelService>;
    let mountOptions: MountingOptions<CarModelDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      carModelServiceStub = sinon.createStubInstance<CarModelService>(CarModelService);

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
          carModelService: () => carModelServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        carModelServiceStub.find.resolves(carModelSample);
        route = {
          params: {
            carModelId: `${123}`,
          },
        };
        const wrapper = shallowMount(CarModelDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.carModel).toMatchObject(carModelSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        carModelServiceStub.find.resolves(carModelSample);
        const wrapper = shallowMount(CarModelDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
