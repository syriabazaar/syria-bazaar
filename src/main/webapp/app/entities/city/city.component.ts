import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import CityService from './city.service';
import { type ICity } from '@/shared/model/city.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'City',
  setup() {
    const { t: t$ } = useI18n();
    const cityService = inject('cityService', () => new CityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const cities: Ref<ICity[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveCitys = async () => {
      isFetching.value = true;
      try {
        const res = await cityService().retrieve();
        cities.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveCitys();
    };

    onMounted(async () => {
      await retrieveCitys();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ICity) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeCity = async () => {
      try {
        await cityService().delete(removeId.value);
        const message = t$('syriaBazaarApp.city.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveCitys();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      cities,
      handleSyncList,
      isFetching,
      retrieveCitys,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeCity,
      t$,
    };
  },
});
