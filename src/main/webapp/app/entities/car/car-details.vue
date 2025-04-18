<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="car">
        <h2 class="jh-entity-heading" data-cy="carDetailsHeading">
          <span v-text="t$('syriaBazaarApp.car.detail.title')"></span> {{ car.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('syriaBazaarApp.car.year')"></span>
          </dt>
          <dd>
            <span>{{ car.year }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.price')"></span>
          </dt>
          <dd>
            <span>{{ car.price }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.mileage')"></span>
          </dt>
          <dd>
            <span>{{ car.mileage }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.drivetrain')"></span>
          </dt>
          <dd>
            <span>{{ car.drivetrain }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.engine')"></span>
          </dt>
          <dd>
            <span>{{ car.engine }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.transmission')"></span>
          </dt>
          <dd>
            <span>{{ car.transmission }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.fuelType')"></span>
          </dt>
          <dd>
            <span>{{ car.fuelType }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.exteriorColor')"></span>
          </dt>
          <dd>
            <span>{{ car.exteriorColor }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.interiorColor')"></span>
          </dt>
          <dd>
            <span>{{ car.interiorColor }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.vin')"></span>
          </dt>
          <dd>
            <span>{{ car.vin }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.location')"></span>
          </dt>
          <dd>
            <span>{{ car.location }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.description')"></span>
          </dt>
          <dd>
            <span>{{ car.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.publishedDate')"></span>
          </dt>
          <dd>
            <span v-if="car.publishedDate">{{ formatDateLong(car.publishedDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.adNumber')"></span>
          </dt>
          <dd>
            <span>{{ car.adNumber }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.views')"></span>
          </dt>
          <dd>
            <span>{{ car.views }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.alloyWheels')"></span>
          </dt>
          <dd>
            <span>{{ car.alloyWheels }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.sunroof')"></span>
          </dt>
          <dd>
            <span>{{ car.sunroof }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.tintedGlass')"></span>
          </dt>
          <dd>
            <span>{{ car.tintedGlass }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.ledHeadlights')"></span>
          </dt>
          <dd>
            <span>{{ car.ledHeadlights }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.foldableRoof')"></span>
          </dt>
          <dd>
            <span>{{ car.foldableRoof }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.towHitch')"></span>
          </dt>
          <dd>
            <span>{{ car.towHitch }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.adjustableSteeringWheel')"></span>
          </dt>
          <dd>
            <span>{{ car.adjustableSteeringWheel }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.autoDimmingRearview')"></span>
          </dt>
          <dd>
            <span>{{ car.autoDimmingRearview }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.heatedFrontSeats')"></span>
          </dt>
          <dd>
            <span>{{ car.heatedFrontSeats }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.leatherSeats')"></span>
          </dt>
          <dd>
            <span>{{ car.leatherSeats }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.blindSpotMonitor')"></span>
          </dt>
          <dd>
            <span>{{ car.blindSpotMonitor }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.adaptiveCruiseControl')"></span>
          </dt>
          <dd>
            <span>{{ car.adaptiveCruiseControl }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.navigationSystem')"></span>
          </dt>
          <dd>
            <span>{{ car.navigationSystem }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.backupCamera')"></span>
          </dt>
          <dd>
            <span>{{ car.backupCamera }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.appleCarplay')"></span>
          </dt>
          <dd>
            <span>{{ car.appleCarplay }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.androidAuto')"></span>
          </dt>
          <dd>
            <span>{{ car.androidAuto }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.premiumSoundSystem')"></span>
          </dt>
          <dd>
            <span>{{ car.premiumSoundSystem }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.isFirstOwn')"></span>
          </dt>
          <dd>
            <span>{{ car.isFirstOwn }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.isAccedFree')"></span>
          </dt>
          <dd>
            <span>{{ car.isAccedFree }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.brand')"></span>
          </dt>
          <dd>
            <div v-if="car.brand">
              <router-link :to="{ name: 'BrandView', params: { brandId: car.brand.id } }">{{ car.brand.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.seller')"></span>
          </dt>
          <dd>
            <div v-if="car.seller">
              <router-link :to="{ name: 'SellerView', params: { sellerId: car.seller.id } }">{{ car.seller.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.type')"></span>
          </dt>
          <dd>
            <div v-if="car.type">
              <router-link :to="{ name: 'CarTypeView', params: { carTypeId: car.type.id } }">{{ car.type.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.model')"></span>
          </dt>
          <dd>
            <div v-if="car.model">
              <router-link :to="{ name: 'CarModelView', params: { carModelId: car.model.id } }">{{ car.model.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.car.city')"></span>
          </dt>
          <dd>
            <div v-if="car.city">
              <router-link :to="{ name: 'CityView', params: { cityId: car.city.id } }">{{ car.city.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="car.id" :to="{ name: 'CarEdit', params: { carId: car.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./car-details.component.ts"></script>
