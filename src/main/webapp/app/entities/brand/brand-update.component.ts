import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import BrandService from './brand.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { Brand, type IBrand } from '@/shared/model/brand.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'BrandUpdate',
  setup() {
    const brandService = inject('brandService', () => new BrandService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const brand: Ref<IBrand> = ref(new Brand());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveBrand = async brandId => {
      try {
        const res = await brandService().find(brandId);
        brand.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.brandId) {
      retrieveBrand(route.params.brandId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, brand as any);
    v$.value.$validate();

    return {
      brandService,
      alertService,
      brand,
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
      if (this.brand.id) {
        this.brandService()
          .update(this.brand)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('syriaBazaarApp.brand.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.brandService()
          .create(this.brand)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('syriaBazaarApp.brand.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
