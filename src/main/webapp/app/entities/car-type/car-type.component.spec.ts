import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CarType from './car-type.vue';
import CarTypeService from './car-type.service';
import AlertService from '@/shared/alert/alert.service';

type CarTypeComponentType = InstanceType<typeof CarType>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CarType Management Component', () => {
    let carTypeServiceStub: SinonStubbedInstance<CarTypeService>;
    let mountOptions: MountingOptions<CarTypeComponentType>['global'];

    beforeEach(() => {
      carTypeServiceStub = sinon.createStubInstance<CarTypeService>(CarTypeService);
      carTypeServiceStub.retrieve.resolves({ headers: {} });

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
          carTypeService: () => carTypeServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        carTypeServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CarType, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(carTypeServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.carTypes[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: CarTypeComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CarType, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        carTypeServiceStub.retrieve.reset();
        carTypeServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        carTypeServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCarType();
        await comp.$nextTick(); // clear components

        // THEN
        expect(carTypeServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(carTypeServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
