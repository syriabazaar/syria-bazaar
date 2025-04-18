<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="syriaBazaarApp.carModel.home.createOrEditLabel"
          data-cy="CarModelCreateUpdateHeading"
          v-text="t$('syriaBazaarApp.carModel.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="carModel.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="carModel.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('syriaBazaarApp.carModel.name')" for="car-model-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="car-model-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('syriaBazaarApp.carModel.brand')" for="car-model-brand"></label>
            <select class="form-control" id="car-model-brand" data-cy="brand" name="brand" v-model="carModel.brand">
              <option :value="null"></option>
              <option
                :value="carModel.brand && brandOption.id === carModel.brand.id ? carModel.brand : brandOption"
                v-for="brandOption in brands"
                :key="brandOption.id"
              >
                {{ brandOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./car-model-update.component.ts"></script>
