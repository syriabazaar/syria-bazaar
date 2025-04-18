import { defineComponent, provide } from 'vue';

import BrandService from './brand/brand.service';
import CarModelService from './car-model/car-model.service';
import CarTypeService from './car-type/car-type.service';
import CityService from './city/city.service';
import SellerService from './seller/seller.service';
import CarService from './car/car.service';
import ReviewService from './review/review.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('brandService', () => new BrandService());
    provide('carModelService', () => new CarModelService());
    provide('carTypeService', () => new CarTypeService());
    provide('cityService', () => new CityService());
    provide('sellerService', () => new SellerService());
    provide('carService', () => new CarService());
    provide('reviewService', () => new ReviewService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
