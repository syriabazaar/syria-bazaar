package com.syriabazaar.web.rest;

import static com.syriabazaar.domain.CarModelAsserts.*;
import static com.syriabazaar.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syriabazaar.IntegrationTest;
import com.syriabazaar.domain.CarModel;
import com.syriabazaar.repository.CarModelRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CarModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/car-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarModelMockMvc;

    private CarModel carModel;

    private CarModel insertedCarModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarModel createEntity() {
        return new CarModel().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarModel createUpdatedEntity() {
        return new CarModel().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        carModel = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCarModel != null) {
            carModelRepository.delete(insertedCarModel);
            insertedCarModel = null;
        }
    }

    @Test
    @Transactional
    void createCarModel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CarModel
        var returnedCarModel = om.readValue(
            restCarModelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carModel)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CarModel.class
        );

        // Validate the CarModel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCarModelUpdatableFieldsEquals(returnedCarModel, getPersistedCarModel(returnedCarModel));

        insertedCarModel = returnedCarModel;
    }

    @Test
    @Transactional
    void createCarModelWithExistingId() throws Exception {
        // Create the CarModel with an existing ID
        carModel.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carModel)))
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carModel.setName(null);

        // Create the CarModel, which fails.

        restCarModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carModel)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCarModels() throws Exception {
        // Initialize the database
        insertedCarModel = carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList
        restCarModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCarModel() throws Exception {
        // Initialize the database
        insertedCarModel = carModelRepository.saveAndFlush(carModel);

        // Get the carModel
        restCarModelMockMvc
            .perform(get(ENTITY_API_URL_ID, carModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCarModel() throws Exception {
        // Get the carModel
        restCarModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarModel() throws Exception {
        // Initialize the database
        insertedCarModel = carModelRepository.saveAndFlush(carModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carModel
        CarModel updatedCarModel = carModelRepository.findById(carModel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCarModel are not directly saved in db
        em.detach(updatedCarModel);
        updatedCarModel.name(UPDATED_NAME);

        restCarModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCarModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCarModel))
            )
            .andExpect(status().isOk());

        // Validate the CarModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarModelToMatchAllProperties(updatedCarModel);
    }

    @Test
    @Transactional
    void putNonExistingCarModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carModel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carModel.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carModel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carModel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carModel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarModelWithPatch() throws Exception {
        // Initialize the database
        insertedCarModel = carModelRepository.saveAndFlush(carModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carModel using partial update
        CarModel partialUpdatedCarModel = new CarModel();
        partialUpdatedCarModel.setId(carModel.getId());

        restCarModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarModel))
            )
            .andExpect(status().isOk());

        // Validate the CarModel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarModelUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCarModel, carModel), getPersistedCarModel(carModel));
    }

    @Test
    @Transactional
    void fullUpdateCarModelWithPatch() throws Exception {
        // Initialize the database
        insertedCarModel = carModelRepository.saveAndFlush(carModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carModel using partial update
        CarModel partialUpdatedCarModel = new CarModel();
        partialUpdatedCarModel.setId(carModel.getId());

        partialUpdatedCarModel.name(UPDATED_NAME);

        restCarModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarModel))
            )
            .andExpect(status().isOk());

        // Validate the CarModel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarModelUpdatableFieldsEquals(partialUpdatedCarModel, getPersistedCarModel(partialUpdatedCarModel));
    }

    @Test
    @Transactional
    void patchNonExistingCarModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carModel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carModel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carModel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarModelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carModel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarModel() throws Exception {
        // Initialize the database
        insertedCarModel = carModelRepository.saveAndFlush(carModel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the carModel
        restCarModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, carModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carModelRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CarModel getPersistedCarModel(CarModel carModel) {
        return carModelRepository.findById(carModel.getId()).orElseThrow();
    }

    protected void assertPersistedCarModelToMatchAllProperties(CarModel expectedCarModel) {
        assertCarModelAllPropertiesEquals(expectedCarModel, getPersistedCarModel(expectedCarModel));
    }

    protected void assertPersistedCarModelToMatchUpdatableProperties(CarModel expectedCarModel) {
        assertCarModelAllUpdatablePropertiesEquals(expectedCarModel, getPersistedCarModel(expectedCarModel));
    }
}
