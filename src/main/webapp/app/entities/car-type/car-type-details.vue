<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="carType">
        <h2 class="jh-entity-heading" data-cy="carTypeDetailsHeading">
          <span v-text="t$('syriaBazaarApp.carType.detail.title')"></span> {{ carType.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('syriaBazaarApp.carType.typeName')"></span>
          </dt>
          <dd>
            <span>{{ carType.typeName }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="carType.id" :to="{ name: 'CarTypeEdit', params: { carTypeId: carType.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./car-type-details.component.ts"></script>
