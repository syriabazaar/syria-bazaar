<template>
  <div>
    <h2 id="page-heading" data-cy="CarTypeHeading">
      <span v-text="t$('syriaBazaarApp.carType.home.title')" id="car-type-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('syriaBazaarApp.carType.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CarTypeCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-car-type"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('syriaBazaarApp.carType.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && carTypes && carTypes.length === 0">
      <span v-text="t$('syriaBazaarApp.carType.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="carTypes && carTypes.length > 0">
      <table class="table table-striped" aria-describedby="carTypes">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('syriaBazaarApp.carType.typeName')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="carType in carTypes" :key="carType.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CarTypeView', params: { carTypeId: carType.id } }">{{ carType.id }}</router-link>
            </td>
            <td>{{ carType.typeName }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CarTypeView', params: { carTypeId: carType.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CarTypeEdit', params: { carTypeId: carType.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(carType)"
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
        <span id="syriaBazaarApp.carType.delete.question" data-cy="carTypeDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-carType-heading" v-text="t$('syriaBazaarApp.carType.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-carType"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCarType()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./car-type.component.ts"></script>
