package com.syriabazaar.web.rest;

import static com.syriabazaar.domain.CarTypeAsserts.*;
import static com.syriabazaar.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syriabazaar.IntegrationTest;
import com.syriabazaar.domain.CarType;
import com.syriabazaar.repository.CarTypeRepository;
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
 * Integration tests for the {@link CarTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarTypeResourceIT {

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/car-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarTypeRepository carTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarTypeMockMvc;

    private CarType carType;

    private CarType insertedCarType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarType createEntity() {
        return new CarType().typeName(DEFAULT_TYPE_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarType createUpdatedEntity() {
        return new CarType().typeName(UPDATED_TYPE_NAME);
    }

    @BeforeEach
    void initTest() {
        carType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCarType != null) {
            carTypeRepository.delete(insertedCarType);
            insertedCarType = null;
        }
    }

    @Test
    @Transactional
    void createCarType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CarType
        var returnedCarType = om.readValue(
            restCarTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CarType.class
        );

        // Validate the CarType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCarTypeUpdatableFieldsEquals(returnedCarType, getPersistedCarType(returnedCarType));

        insertedCarType = returnedCarType;
    }

    @Test
    @Transactional
    void createCarTypeWithExistingId() throws Exception {
        // Create the CarType with an existing ID
        carType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carType)))
            .andExpect(status().isBadRequest());

        // Validate the CarType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carType.setTypeName(null);

        // Create the CarType, which fails.

        restCarTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCarTypes() throws Exception {
        // Initialize the database
        insertedCarType = carTypeRepository.saveAndFlush(carType);

        // Get all the carTypeList
        restCarTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));
    }

    @Test
    @Transactional
    void getCarType() throws Exception {
        // Initialize the database
        insertedCarType = carTypeRepository.saveAndFlush(carType);

        // Get the carType
        restCarTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, carType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carType.getId().intValue()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCarType() throws Exception {
        // Get the carType
        restCarTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarType() throws Exception {
        // Initialize the database
        insertedCarType = carTypeRepository.saveAndFlush(carType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carType
        CarType updatedCarType = carTypeRepository.findById(carType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCarType are not directly saved in db
        em.detach(updatedCarType);
        updatedCarType.typeName(UPDATED_TYPE_NAME);

        restCarTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCarType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCarType))
            )
            .andExpect(status().isOk());

        // Validate the CarType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarTypeToMatchAllProperties(updatedCarType);
    }

    @Test
    @Transactional
    void putNonExistingCarType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarTypeMockMvc
            .perform(put(ENTITY_API_URL_ID, carType.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carType)))
            .andExpect(status().isBadRequest());

        // Validate the CarType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCarType = carTypeRepository.saveAndFlush(carType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carType using partial update
        CarType partialUpdatedCarType = new CarType();
        partialUpdatedCarType.setId(carType.getId());

        restCarTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarType))
            )
            .andExpect(status().isOk());

        // Validate the CarType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCarType, carType), getPersistedCarType(carType));
    }

    @Test
    @Transactional
    void fullUpdateCarTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCarType = carTypeRepository.saveAndFlush(carType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carType using partial update
        CarType partialUpdatedCarType = new CarType();
        partialUpdatedCarType.setId(carType.getId());

        partialUpdatedCarType.typeName(UPDATED_TYPE_NAME);

        restCarTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarType))
            )
            .andExpect(status().isOk());

        // Validate the CarType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarTypeUpdatableFieldsEquals(partialUpdatedCarType, getPersistedCarType(partialUpdatedCarType));
    }

    @Test
    @Transactional
    void patchNonExistingCarType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carType.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarType() throws Exception {
        // Initialize the database
        insertedCarType = carTypeRepository.saveAndFlush(carType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the carType
        restCarTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, carType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carTypeRepository.count();
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

    protected CarType getPersistedCarType(CarType carType) {
        return carTypeRepository.findById(carType.getId()).orElseThrow();
    }

    protected void assertPersistedCarTypeToMatchAllProperties(CarType expectedCarType) {
        assertCarTypeAllPropertiesEquals(expectedCarType, getPersistedCarType(expectedCarType));
    }

    protected void assertPersistedCarTypeToMatchUpdatableProperties(CarType expectedCarType) {
        assertCarTypeAllUpdatablePropertiesEquals(expectedCarType, getPersistedCarType(expectedCarType));
    }
}
