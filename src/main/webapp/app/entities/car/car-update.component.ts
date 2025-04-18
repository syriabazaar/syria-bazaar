import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CarService from './car.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import BrandService from '@/entities/brand/brand.service';
import { type IBrand } from '@/shared/model/brand.model';
import SellerService from '@/entities/seller/seller.service';
import { type ISeller } from '@/shared/model/seller.model';
import CarTypeService from '@/entities/car-type/car-type.service';
import { type ICarType } from '@/shared/model/car-type.model';
import CarModelService from '@/entities/car-model/car-model.service';
import { type ICarModel } from '@/shared/model/car-model.model';
import CityService from '@/entities/city/city.service';
import { type ICity } from '@/shared/model/city.model';
import { Car, type ICar } from '@/shared/model/car.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CarUpdate',
  setup() {
    const carService = inject('carService', () => new CarService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const car: Ref<ICar> = ref(new Car());

    const brandService = inject('brandService', () => new BrandService());

    const brands: Ref<IBrand[]> = ref([]);

    const sellerService = inject('sellerService', () => new SellerService());

    const sellers: Ref<ISeller[]> = ref([]);

    const carTypeService = inject('carTypeService', () => new CarTypeService());

    const carTypes: Ref<ICarType[]> = ref([]);

    const carModelService = inject('carModelService', () => new CarModelService());

    const carModels: Ref<ICarModel[]> = ref([]);

    const cityService = inject('cityService', () => new CityService());

    const cities: Ref<ICity[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCar = async carId => {
      try {
        const res = await carService().find(carId);
        res.publishedDate = new Date(res.publishedDate);
        car.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.carId) {
      retrieveCar(route.params.carId);
    }

    const initRelationships = () => {
      brandService()
        .retrieve()
        .then(res => {
          brands.value = res.data;
        });
      sellerService()
        .retrieve()
        .then(res => {
          sellers.value = res.data;
        });
      carTypeService()
        .retrieve()
        .then(res => {
          carTypes.value = res.data;
        });
      carModelService()
        .retrieve()
        .then(res => {
          carModels.value = res.data;
        });
      cityService()
        .retrieve()
        .then(res => {
          cities.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      year: {
        integer: validations.integer(t$('entity.validation.number').toString()),
        min: validations.minValue(t$('entity.validation.min', { min: 1886 }).toString(), 1886),
      },
      price: {
        min: validations.minValue(t$('entity.validation.min', { min: 0 }).toString(), 0),
      },
      mileage: {
        min: validations.minValue(t$('entity.validation.min', { min: 0 }).toString(), 0),
      },
      drivetrain: {},
      engine: {},
      transmission: {},
      fuelType: {},
      exteriorColor: {},
      interiorColor: {},
      vin: {},
      location: {},
      description: {},
      publishedDate: {},
      adNumber: {
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      views: {
        integer: validations.integer(t$('entity.validation.number').toString()),
        min: validations.minValue(t$('entity.validation.min', { min: 0 }).toString(), 0),
      },
      alloyWheels: {},
      sunroof: {},
      tintedGlass: {},
      ledHeadlights: {},
      foldableRoof: {},
      towHitch: {},
      adjustableSteeringWheel: {},
      autoDimmingRearview: {},
      heatedFrontSeats: {},
      leatherSeats: {},
      blindSpotMonitor: {},
      adaptiveCruiseControl: {},
      navigationSystem: {},
      backupCamera: {},
      appleCarplay: {},
      androidAuto: {},
      premiumSoundSystem: {},
      isFirstOwn: {},
      isAccedFree: {},
      brand: {},
      seller: {},
      type: {},
      model: {},
      city: {},
    };
    const v$ = useVuelidate(validationRules, car as any);
    v$.value.$validate();

    return {
      carService,
      alertService,
      car,
      previousState,
      isSaving,
      currentLanguage,
      brands,
      sellers,
      carTypes,
      carModels,
      cities,
      v$,
      ...useDateFormat({ entityRef: car }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.car.id) {
        this.carService()
          .update(this.car)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('syriaBazaarApp.car.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.carService()
          .create(this.car)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('syriaBazaarApp.car.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
