import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import SellerService from './seller.service';
import { type ISeller } from '@/shared/model/seller.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SellerDetails',
  setup() {
    const sellerService = inject('sellerService', () => new SellerService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const seller: Ref<ISeller> = ref({});

    const retrieveSeller = async sellerId => {
      try {
        const res = await sellerService().find(sellerId);
        seller.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.sellerId) {
      retrieveSeller(route.params.sellerId);
    }

    return {
      alertService,
      seller,

      previousState,
      t$: useI18n().t,
    };
  },
});
