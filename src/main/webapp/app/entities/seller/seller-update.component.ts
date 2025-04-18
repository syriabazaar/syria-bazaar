import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SellerService from './seller.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type ISeller, Seller } from '@/shared/model/seller.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SellerUpdate',
  setup() {
    const sellerService = inject('sellerService', () => new SellerService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const seller: Ref<ISeller> = ref(new Seller());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      address: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      mobileNo: {},
      whatsApp: {},
      facebook: {},
      insta: {},
    };
    const v$ = useVuelidate(validationRules, seller as any);
    v$.value.$validate();

    return {
      sellerService,
      alertService,
      seller,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.seller.id) {
        this.sellerService()
          .update(this.seller)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('syriaBazaarApp.seller.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.sellerService()
          .create(this.seller)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('syriaBazaarApp.seller.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
