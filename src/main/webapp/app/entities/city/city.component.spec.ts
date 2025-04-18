import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import City from './city.vue';
import CityService from './city.service';
import AlertService from '@/shared/alert/alert.service';

type CityComponentType = InstanceType<typeof City>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('City Management Component', () => {
    let cityServiceStub: SinonStubbedInstance<CityService>;
    let mountOptions: MountingOptions<CityComponentType>['global'];

    beforeEach(() => {
      cityServiceStub = sinon.createStubInstance<CityService>(CityService);
      cityServiceStub.retrieve.resolves({ headers: {} });

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
          cityService: () => cityServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        cityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(City, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(cityServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.cities[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: CityComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(City, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        cityServiceStub.retrieve.reset();
        cityServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        cityServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCity();
        await comp.$nextTick(); // clear components

        // THEN
        expect(cityServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(cityServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
