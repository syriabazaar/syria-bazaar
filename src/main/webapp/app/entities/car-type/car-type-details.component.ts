import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import CarTypeService from './car-type.service';
import { type ICarType } from '@/shared/model/car-type.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CarTypeDetails',
  setup() {
    const carTypeService = inject('carTypeService', () => new CarTypeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const carType: Ref<ICarType> = ref({});

    const retrieveCarType = async carTypeId => {
      try {
        const res = await carTypeService().find(carTypeId);
        carType.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.carTypeId) {
      retrieveCarType(route.params.carTypeId);
    }

    return {
      alertService,
      carType,

      previousState,
      t$: useI18n().t,
    };
  },
});
