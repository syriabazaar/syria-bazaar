import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Seller from './seller.vue';
import SellerService from './seller.service';
import AlertService from '@/shared/alert/alert.service';

type SellerComponentType = InstanceType<typeof Seller>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Seller Management Component', () => {
    let sellerServiceStub: SinonStubbedInstance<SellerService>;
    let mountOptions: MountingOptions<SellerComponentType>['global'];

    beforeEach(() => {
      sellerServiceStub = sinon.createStubInstance<SellerService>(SellerService);
      sellerServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          sellerService: () => sellerServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        sellerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Seller, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(sellerServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.sellers[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: SellerComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Seller, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        sellerServiceStub.retrieve.reset();
        sellerServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        sellerServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSeller();
        await comp.$nextTick(); // clear components

        // THEN
        expect(sellerServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(sellerServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
