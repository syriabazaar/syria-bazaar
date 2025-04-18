<template>
  <div>
    <b-dropdown-item to="/brand">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.brand')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/car-model">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.carModel')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/car-type">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.carType')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/city">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.city')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/seller">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.seller')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/car">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.car')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/review">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.review')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
