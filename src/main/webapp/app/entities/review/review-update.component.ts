import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ReviewService from './review.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SellerService from '@/entities/seller/seller.service';
import { type ISeller } from '@/shared/model/seller.model';
import { type IReview, Review } from '@/shared/model/review.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ReviewUpdate',
  setup() {
    const reviewService = inject('reviewService', () => new ReviewService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const review: Ref<IReview> = ref(new Review());

    const sellerService = inject('sellerService', () => new SellerService());

    const sellers: Ref<ISeller[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveReview = async reviewId => {
      try {
        const res = await reviewService().find(reviewId);
        res.createdAt = new Date(res.createdAt);
        review.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.reviewId) {
      retrieveReview(route.params.reviewId);
    }

    const initRelationships = () => {
      sellerService()
        .retrieve()
        .then(res => {
          sellers.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      createdAt: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      revVal: {},
      revTxt: {},
      fromUser: {},
      toUser: {},
    };
    const v$ = useVuelidate(validationRules, review as any);
    v$.value.$validate();

    return {
      reviewService,
      alertService,
      review,
      previousState,
      isSaving,
      currentLanguage,
      sellers,
      v$,
      ...useDateFormat({ entityRef: review }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.review.id) {
        this.reviewService()
          .update(this.review)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('syriaBazaarApp.review.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.reviewService()
          .create(this.review)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('syriaBazaarApp.review.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
