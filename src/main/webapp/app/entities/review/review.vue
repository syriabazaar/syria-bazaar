<template>
  <div>
    <h2 id="page-heading" data-cy="ReviewHeading">
      <span v-text="t$('syriaBazaarApp.review.home.title')" id="review-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('syriaBazaarApp.review.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ReviewCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-review"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('syriaBazaarApp.review.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && reviews && reviews.length === 0">
      <span v-text="t$('syriaBazaarApp.review.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="reviews && reviews.length > 0">
      <table class="table table-striped" aria-describedby="reviews">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('createdAt')">
              <span v-text="t$('syriaBazaarApp.review.createdAt')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdAt'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('revVal')">
              <span v-text="t$('syriaBazaarApp.review.revVal')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'revVal'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('revTxt')">
              <span v-text="t$('syriaBazaarApp.review.revTxt')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'revTxt'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fromUser.id')">
              <span v-text="t$('syriaBazaarApp.review.fromUser')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fromUser.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('toUser.id')">
              <span v-text="t$('syriaBazaarApp.review.toUser')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'toUser.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="review in reviews" :key="review.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ReviewView', params: { reviewId: review.id } }">{{ review.id }}</router-link>
            </td>
            <td>{{ formatDateShort(review.createdAt) || '' }}</td>
            <td>{{ review.revVal }}</td>
            <td>{{ review.revTxt }}</td>
            <td>
              <div v-if="review.fromUser">
                <router-link :to="{ name: 'SellerView', params: { sellerId: review.fromUser.id } }">{{ review.fromUser.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="review.toUser">
                <router-link :to="{ name: 'SellerView', params: { sellerId: review.toUser.id } }">{{ review.toUser.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ReviewView', params: { reviewId: review.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ReviewEdit', params: { reviewId: review.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(review)"
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
        <span id="syriaBazaarApp.review.delete.question" data-cy="reviewDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-review-heading" v-text="t$('syriaBazaarApp.review.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-review"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeReview()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="reviews && reviews.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./review.component.ts"></script>
