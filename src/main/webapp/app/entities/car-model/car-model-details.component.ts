import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import CarModelService from './car-model.service';
import { type ICarModel } from '@/shared/model/car-model.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CarModelDetails',
  setup() {
    const carModelService = inject('carModelService', () => new CarModelService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const carModel: Ref<ICarModel> = ref({});

    const retrieveCarModel = async carModelId => {
      try {
        const res = await carModelService().find(carModelId);
        carModel.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.carModelId) {
      retrieveCarModel(route.params.carModelId);
    }

    return {
      alertService,
      carModel,

      previousState,
      t$: useI18n().t,
    };
  },
});
