import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const Brand = () => import('@/entities/brand/brand.vue');
const BrandUpdate = () => import('@/entities/brand/brand-update.vue');
const BrandDetails = () => import('@/entities/brand/brand-details.vue');

const CarModel = () => import('@/entities/car-model/car-model.vue');
const CarModelUpdate = () => import('@/entities/car-model/car-model-update.vue');
const CarModelDetails = () => import('@/entities/car-model/car-model-details.vue');

const CarType = () => import('@/entities/car-type/car-type.vue');
const CarTypeUpdate = () => import('@/entities/car-type/car-type-update.vue');
const CarTypeDetails = () => import('@/entities/car-type/car-type-details.vue');

const City = () => import('@/entities/city/city.vue');
const CityUpdate = () => import('@/entities/city/city-update.vue');
const CityDetails = () => import('@/entities/city/city-details.vue');

const Seller = () => import('@/entities/seller/seller.vue');
const SellerUpdate = () => import('@/entities/seller/seller-update.vue');
const SellerDetails = () => import('@/entities/seller/seller-details.vue');

const Car = () => import('@/entities/car/car.vue');
const CarUpdate = () => import('@/entities/car/car-update.vue');
const CarDetails = () => import('@/entities/car/car-details.vue');

const Review = () => import('@/entities/review/review.vue');
const ReviewUpdate = () => import('@/entities/review/review-update.vue');
const ReviewDetails = () => import('@/entities/review/review-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'brand',
      name: 'Brand',
      component: Brand,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'brand/new',
      name: 'BrandCreate',
      component: BrandUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'brand/:brandId/edit',
      name: 'BrandEdit',
      component: BrandUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'brand/:brandId/view',
      name: 'BrandView',
      component: BrandDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'car-model',
      name: 'CarModel',
      component: CarModel,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'car-model/new',
      name: 'CarModelCreate',
      component: CarModelUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'car-model/:carModelId/edit',
      name: 'CarModelEdit',
      component: CarModelUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'car-model/:carModelId/view',
      name: 'CarModelView',
      component: CarModelDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'car-type',
      name: 'CarType',
      component: CarType,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'car-type/new',
      name: 'CarTypeCreate',
      component: CarTypeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'car-type/:carTypeId/edit',
      name: 'CarTypeEdit',
      component: CarTypeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'car-type/:carTypeId/view',
      name: 'CarTypeView',
      component: CarTypeDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'city',
      name: 'City',
      component: City,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'city/new',
      name: 'CityCreate',
      component: CityUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'city/:cityId/edit',
      name: 'CityEdit',
      component: CityUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'city/:cityId/view',
      name: 'CityView',
      component: CityDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'seller',
      name: 'Seller',
      component: Seller,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'seller/new',
      name: 'SellerCreate',
      component: SellerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'seller/:sellerId/edit',
      name: 'SellerEdit',
      component: SellerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'seller/:sellerId/view',
      name: 'SellerView',
      component: SellerDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'car',
      name: 'Car',
      component: Car,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'car/new',
      name: 'CarCreate',
      component: CarUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'car/:carId/edit',
      name: 'CarEdit',
      component: CarUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'car/:carId/view',
      name: 'CarView',
      component: CarDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'review',
      name: 'Review',
      component: Review,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'review/new',
      name: 'ReviewCreate',
      component: ReviewUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'review/:reviewId/edit',
      name: 'ReviewEdit',
      component: ReviewUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'review/:reviewId/view',
      name: 'ReviewView',
      component: ReviewDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
