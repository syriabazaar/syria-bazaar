import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CarModel from './car-model.vue';
import CarModelService from './car-model.service';
import AlertService from '@/shared/alert/alert.service';

type CarModelComponentType = InstanceType<typeof CarModel>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CarModel Management Component', () => {
    let carModelServiceStub: SinonStubbedInstance<CarModelService>;
    let mountOptions: MountingOptions<CarModelComponentType>['global'];

    beforeEach(() => {
      carModelServiceStub = sinon.createStubInstance<CarModelService>(CarModelService);
      carModelServiceStub.retrieve.resolves({ headers: {} });

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
          carModelService: () => carModelServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        carModelServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CarModel, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(carModelServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.carModels[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: CarModelComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CarModel, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        carModelServiceStub.retrieve.reset();
        carModelServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        carModelServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCarModel();
        await comp.$nextTick(); // clear components

        // THEN
        expect(carModelServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(carModelServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
