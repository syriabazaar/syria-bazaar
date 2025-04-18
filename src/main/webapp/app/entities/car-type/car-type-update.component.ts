import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CarTypeService from './car-type.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { CarType, type ICarType } from '@/shared/model/car-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CarTypeUpdate',
  setup() {
    const carTypeService = inject('carTypeService', () => new CarTypeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const carType: Ref<ICarType> = ref(new CarType());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      typeName: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, carType as any);
    v$.value.$validate();

    return {
      carTypeService,
      alertService,
      carType,
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
      if (this.carType.id) {
        this.carTypeService()
          .update(this.carType)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('syriaBazaarApp.carType.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.carTypeService()
          .create(this.carType)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('syriaBazaarApp.carType.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
