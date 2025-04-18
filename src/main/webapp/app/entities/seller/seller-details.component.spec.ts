import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SellerDetails from './seller-details.vue';
import SellerService from './seller.service';
import AlertService from '@/shared/alert/alert.service';

type SellerDetailsComponentType = InstanceType<typeof SellerDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const sellerSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Seller Management Detail Component', () => {
    let sellerServiceStub: SinonStubbedInstance<SellerService>;
    let mountOptions: MountingOptions<SellerDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      sellerServiceStub = sinon.createStubInstance<SellerService>(SellerService);

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
          sellerService: () => sellerServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        sellerServiceStub.find.resolves(sellerSample);
        route = {
          params: {
            sellerId: `${123}`,
          },
        };
        const wrapper = shallowMount(SellerDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.seller).toMatchObject(sellerSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        sellerServiceStub.find.resolves(sellerSample);
        const wrapper = shallowMount(SellerDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
