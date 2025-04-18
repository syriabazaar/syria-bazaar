<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="seller">
        <h2 class="jh-entity-heading" data-cy="sellerDetailsHeading">
          <span v-text="t$('syriaBazaarApp.seller.detail.title')"></span> {{ seller.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('syriaBazaarApp.seller.name')"></span>
          </dt>
          <dd>
            <span>{{ seller.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.seller.address')"></span>
          </dt>
          <dd>
            <span>{{ seller.address }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.seller.mobileNo')"></span>
          </dt>
          <dd>
            <span>{{ seller.mobileNo }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.seller.whatsApp')"></span>
          </dt>
          <dd>
            <span>{{ seller.whatsApp }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.seller.facebook')"></span>
          </dt>
          <dd>
            <span>{{ seller.facebook }}</span>
          </dd>
          <dt>
            <span v-text="t$('syriaBazaarApp.seller.insta')"></span>
          </dt>
          <dd>
            <span>{{ seller.insta }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="seller.id" :to="{ name: 'SellerEdit', params: { sellerId: seller.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./seller-details.component.ts"></script>
