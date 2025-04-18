import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CarModelService from './car-model.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import BrandService from '@/entities/brand/brand.service';
import { type IBrand } from '@/shared/model/brand.model';
import { CarModel, type ICarModel } from '@/shared/model/car-model.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CarModelUpdate',
  setup() {
    const carModelService = inject('carModelService', () => new CarModelService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const carModel: Ref<ICarModel> = ref(new CarModel());

    const brandService = inject('brandService', () => new BrandService());

    const brands: Ref<IBrand[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      brandService()
        .retrieve()
        .then(res => {
          brands.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      brand: {},
    };
    const v$ = useVuelidate(validationRules, carModel as any);
    v$.value.$validate();

    return {
      carModelService,
      alertService,
      carModel,
      previousState,
      isSaving,
      currentLanguage,
      brands,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.carModel.id) {
        this.carModelService()
          .update(this.carModel)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('syriaBazaarApp.carModel.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.carModelService()
          .create(this.carModel)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('syriaBazaarApp.carModel.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
