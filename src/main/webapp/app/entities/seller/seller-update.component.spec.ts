import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SellerUpdate from './seller-update.vue';
import SellerService from './seller.service';
import AlertService from '@/shared/alert/alert.service';

type SellerUpdateComponentType = InstanceType<typeof SellerUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const sellerSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SellerUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Seller Management Update Component', () => {
    let comp: SellerUpdateComponentType;
    let sellerServiceStub: SinonStubbedInstance<SellerService>;

    beforeEach(() => {
      route = {};
      sellerServiceStub = sinon.createStubInstance<SellerService>(SellerService);
      sellerServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          sellerService: () => sellerServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(SellerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.seller = sellerSample;
        sellerServiceStub.update.resolves(sellerSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(sellerServiceStub.update.calledWith(sellerSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        sellerServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SellerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.seller = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(sellerServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        sellerServiceStub.find.resolves(sellerSample);
        sellerServiceStub.retrieve.resolves([sellerSample]);

        // WHEN
        route = {
          params: {
            sellerId: `${sellerSample.id}`,
          },
        };
        const wrapper = shallowMount(SellerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.seller).toMatchObject(sellerSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        sellerServiceStub.find.resolves(sellerSample);
        const wrapper = shallowMount(SellerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
