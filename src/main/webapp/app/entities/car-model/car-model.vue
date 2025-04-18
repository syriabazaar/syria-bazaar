<template>
  <div>
    <h2 id="page-heading" data-cy="CarModelHeading">
      <span v-text="t$('syriaBazaarApp.carModel.home.title')" id="car-model-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('syriaBazaarApp.carModel.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CarModelCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-car-model"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('syriaBazaarApp.carModel.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && carModels && carModels.length === 0">
      <span v-text="t$('syriaBazaarApp.carModel.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="carModels && carModels.length > 0">
      <table class="table table-striped" aria-describedby="carModels">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('syriaBazaarApp.carModel.name')"></span></th>
            <th scope="row"><span v-text="t$('syriaBazaarApp.carModel.brand')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="carModel in carModels" :key="carModel.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CarModelView', params: { carModelId: carModel.id } }">{{ carModel.id }}</router-link>
            </td>
            <td>{{ carModel.name }}</td>
            <td>
              <div v-if="carModel.brand">
                <router-link :to="{ name: 'BrandView', params: { brandId: carModel.brand.id } }">{{ carModel.brand.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CarModelView', params: { carModelId: carModel.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CarModelEdit', params: { carModelId: carModel.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(carModel)"
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
        <span id="syriaBazaarApp.carModel.delete.question" data-cy="carModelDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-carModel-heading" v-text="t$('syriaBazaarApp.carModel.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-carModel"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCarModel()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./car-model.component.ts"></script>
