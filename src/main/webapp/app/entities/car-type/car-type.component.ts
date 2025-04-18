import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import CarTypeService from './car-type.service';
import { type ICarType } from '@/shared/model/car-type.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CarType',
  setup() {
    const { t: t$ } = useI18n();
    const carTypeService = inject('carTypeService', () => new CarTypeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const carTypes: Ref<ICarType[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveCarTypes = async () => {
      isFetching.value = true;
      try {
        const res = await carTypeService().retrieve();
        carTypes.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveCarTypes();
    };

    onMounted(async () => {
      await retrieveCarTypes();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ICarType) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeCarType = async () => {
      try {
        await carTypeService().delete(removeId.value);
        const message = t$('syriaBazaarApp.carType.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveCarTypes();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      carTypes,
      handleSyncList,
      isFetching,
      retrieveCarTypes,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeCarType,
      t$,
    };
  },
});
