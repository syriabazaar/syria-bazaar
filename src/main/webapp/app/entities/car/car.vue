<template>
  <div>
    <h2 id="page-heading" data-cy="CarHeading">
      <span v-text="t$('syriaBazaarApp.car.home.title')" id="car-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('syriaBazaarApp.car.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CarCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-car">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('syriaBazaarApp.car.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && cars && cars.length === 0">
      <span v-text="t$('syriaBazaarApp.car.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="cars && cars.length > 0">
      <table class="table table-striped" aria-describedby="cars">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('year')">
              <span v-text="t$('syriaBazaarApp.car.year')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'year'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('price')">
              <span v-text="t$('syriaBazaarApp.car.price')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'price'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('mileage')">
              <span v-text="t$('syriaBazaarApp.car.mileage')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'mileage'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('drivetrain')">
              <span v-text="t$('syriaBazaarApp.car.drivetrain')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'drivetrain'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('engine')">
              <span v-text="t$('syriaBazaarApp.car.engine')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'engine'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('transmission')">
              <span v-text="t$('syriaBazaarApp.car.transmission')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'transmission'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fuelType')">
              <span v-text="t$('syriaBazaarApp.car.fuelType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fuelType'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('exteriorColor')">
              <span v-text="t$('syriaBazaarApp.car.exteriorColor')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'exteriorColor'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('interiorColor')">
              <span v-text="t$('syriaBazaarApp.car.interiorColor')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'interiorColor'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('vin')">
              <span v-text="t$('syriaBazaarApp.car.vin')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'vin'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('location')">
              <span v-text="t$('syriaBazaarApp.car.location')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'location'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('description')">
              <span v-text="t$('syriaBazaarApp.car.description')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('publishedDate')">
              <span v-text="t$('syriaBazaarApp.car.publishedDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'publishedDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('adNumber')">
              <span v-text="t$('syriaBazaarApp.car.adNumber')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'adNumber'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('views')">
              <span v-text="t$('syriaBazaarApp.car.views')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'views'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('alloyWheels')">
              <span v-text="t$('syriaBazaarApp.car.alloyWheels')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'alloyWheels'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('sunroof')">
              <span v-text="t$('syriaBazaarApp.car.sunroof')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sunroof'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('tintedGlass')">
              <span v-text="t$('syriaBazaarApp.car.tintedGlass')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tintedGlass'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('ledHeadlights')">
              <span v-text="t$('syriaBazaarApp.car.ledHeadlights')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ledHeadlights'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('foldableRoof')">
              <span v-text="t$('syriaBazaarApp.car.foldableRoof')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'foldableRoof'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('towHitch')">
              <span v-text="t$('syriaBazaarApp.car.towHitch')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'towHitch'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('adjustableSteeringWheel')">
              <span v-text="t$('syriaBazaarApp.car.adjustableSteeringWheel')"></span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'adjustableSteeringWheel'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('autoDimmingRearview')">
              <span v-text="t$('syriaBazaarApp.car.autoDimmingRearview')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'autoDimmingRearview'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('heatedFrontSeats')">
              <span v-text="t$('syriaBazaarApp.car.heatedFrontSeats')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'heatedFrontSeats'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('leatherSeats')">
              <span v-text="t$('syriaBazaarApp.car.leatherSeats')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'leatherSeats'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('blindSpotMonitor')">
              <span v-text="t$('syriaBazaarApp.car.blindSpotMonitor')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'blindSpotMonitor'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('adaptiveCruiseControl')">
              <span v-text="t$('syriaBazaarApp.car.adaptiveCruiseControl')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'adaptiveCruiseControl'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('navigationSystem')">
              <span v-text="t$('syriaBazaarApp.car.navigationSystem')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'navigationSystem'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('backupCamera')">
              <span v-text="t$('syriaBazaarApp.car.backupCamera')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'backupCamera'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('appleCarplay')">
              <span v-text="t$('syriaBazaarApp.car.appleCarplay')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'appleCarplay'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('androidAuto')">
              <span v-text="t$('syriaBazaarApp.car.androidAuto')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'androidAuto'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('premiumSoundSystem')">
              <span v-text="t$('syriaBazaarApp.car.premiumSoundSystem')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'premiumSoundSystem'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('isFirstOwn')">
              <span v-text="t$('syriaBazaarApp.car.isFirstOwn')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isFirstOwn'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('isAccedFree')">
              <span v-text="t$('syriaBazaarApp.car.isAccedFree')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isAccedFree'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('brand.id')">
              <span v-text="t$('syriaBazaarApp.car.brand')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'brand.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('seller.id')">
              <span v-text="t$('syriaBazaarApp.car.seller')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'seller.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('type.id')">
              <span v-text="t$('syriaBazaarApp.car.type')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'type.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('model.id')">
              <span v-text="t$('syriaBazaarApp.car.model')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'model.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('city.id')">
              <span v-text="t$('syriaBazaarApp.car.city')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'city.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="car in cars" :key="car.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CarView', params: { carId: car.id } }">{{ car.id }}</router-link>
            </td>
            <td>{{ car.year }}</td>
            <td>{{ car.price }}</td>
            <td>{{ car.mileage }}</td>
            <td>{{ car.drivetrain }}</td>
            <td>{{ car.engine }}</td>
            <td>{{ car.transmission }}</td>
            <td>{{ car.fuelType }}</td>
            <td>{{ car.exteriorColor }}</td>
            <td>{{ car.interiorColor }}</td>
            <td>{{ car.vin }}</td>
            <td>{{ car.location }}</td>
            <td>{{ car.description }}</td>
            <td>{{ formatDateShort(car.publishedDate) || '' }}</td>
            <td>{{ car.adNumber }}</td>
            <td>{{ car.views }}</td>
            <td>{{ car.alloyWheels }}</td>
            <td>{{ car.sunroof }}</td>
            <td>{{ car.tintedGlass }}</td>
            <td>{{ car.ledHeadlights }}</td>
            <td>{{ car.foldableRoof }}</td>
            <td>{{ car.towHitch }}</td>
            <td>{{ car.adjustableSteeringWheel }}</td>
            <td>{{ car.autoDimmingRearview }}</td>
            <td>{{ car.heatedFrontSeats }}</td>
            <td>{{ car.leatherSeats }}</td>
            <td>{{ car.blindSpotMonitor }}</td>
            <td>{{ car.adaptiveCruiseControl }}</td>
            <td>{{ car.navigationSystem }}</td>
            <td>{{ car.backupCamera }}</td>
            <td>{{ car.appleCarplay }}</td>
            <td>{{ car.androidAuto }}</td>
            <td>{{ car.premiumSoundSystem }}</td>
            <td>{{ car.isFirstOwn }}</td>
            <td>{{ car.isAccedFree }}</td>
            <td>
              <div v-if="car.brand">
                <router-link :to="{ name: 'BrandView', params: { brandId: car.brand.id } }">{{ car.brand.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="car.seller">
                <router-link :to="{ name: 'SellerView', params: { sellerId: car.seller.id } }">{{ car.seller.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="car.type">
                <router-link :to="{ name: 'CarTypeView', params: { carTypeId: car.type.id } }">{{ car.type.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="car.model">
                <router-link :to="{ name: 'CarModelView', params: { carModelId: car.model.id } }">{{ car.model.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="car.city">
                <router-link :to="{ name: 'CityView', params: { cityId: car.city.id } }">{{ car.city.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CarView', params: { carId: car.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CarEdit', params: { carId: car.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(car)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="syriaBazaarApp.car.delete.question" data-cy="carDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-car-heading" v-text="t$('syriaBazaarApp.car.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-car"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCar()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="cars && cars.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./car.component.ts"></script>
