import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import SellerService from './seller.service';
import { type ISeller } from '@/shared/model/seller.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Seller',
  setup() {
    const { t: t$ } = useI18n();
    const sellerService = inject('sellerService', () => new SellerService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const sellers: Ref<ISeller[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveSellers = async () => {
      isFetching.value = true;
      try {
        const res = await sellerService().retrieve();
        sellers.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveSellers();
    };

    onMounted(async () => {
      await retrieveSellers();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ISeller) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeSeller = async () => {
      try {
        await sellerService().delete(removeId.value);
        const message = t$('syriaBazaarApp.seller.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveSellers();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      sellers,
      handleSyncList,
      isFetching,
      retrieveSellers,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeSeller,
      t$,
    };
  },
});
