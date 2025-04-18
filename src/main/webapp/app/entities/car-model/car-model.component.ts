import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import CarModelService from './car-model.service';
import { type ICarModel } from '@/shared/model/car-model.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CarModel',
  setup() {
    const { t: t$ } = useI18n();
    const carModelService = inject('carModelService', () => new CarModelService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const carModels: Ref<ICarModel[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveCarModels = async () => {
      isFetching.value = true;
      try {
        const res = await carModelService().retrieve();
        carModels.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveCarModels();
    };

    onMounted(async () => {
      await retrieveCarModels();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ICarModel) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeCarModel = async () => {
      try {
        await carModelService().delete(removeId.value);
        const message = t$('syriaBazaarApp.carModel.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveCarModels();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      carModels,
      handleSyncList,
      isFetching,
      retrieveCarModels,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeCarModel,
      t$,
    };
  },
});
