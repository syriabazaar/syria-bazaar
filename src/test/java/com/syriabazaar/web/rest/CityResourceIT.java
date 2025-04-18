package com.syriabazaar.web.rest;

import static com.syriabazaar.domain.CityAsserts.*;
import static com.syriabazaar.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syriabazaar.IntegrationTest;
import com.syriabazaar.domain.City;
import com.syriabazaar.repository.CityRepository;
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
 * Integration tests for the {@link CityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCityMockMvc;

    private City city;

    private City insertedCity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createEntity() {
        return new City().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createUpdatedEntity() {
        return new City().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        city = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCity != null) {
            cityRepository.delete(insertedCity);
            insertedCity = null;
        }
    }

    @Test
    @Transactional
    void createCity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the City
        var returnedCity = om.readValue(
            restCityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(city)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            City.class
        );

        // Validate the City in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCityUpdatableFieldsEquals(returnedCity, getPersistedCity(returnedCity));

        insertedCity = returnedCity;
    }

    @Test
    @Transactional
    void createCityWithExistingId() throws Exception {
        // Create the City with an existing ID
        city.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(city)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        city.setName(null);

        // Create the City, which fails.

        restCityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(city)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCities() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCity() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get the city
        restCityMockMvc
            .perform(get(ENTITY_API_URL_ID, city.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(city.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCity() throws Exception {
        // Get the city
        restCityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCity() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the city
        City updatedCity = cityRepository.findById(city.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCity are not directly saved in db
        em.detach(updatedCity);
        updatedCity.name(UPDATED_NAME);

        restCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCity))
            )
            .andExpect(status().isOk());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCityToMatchAllProperties(updatedCity);
    }

    @Test
    @Transactional
    void putNonExistingCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        city.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(put(ENTITY_API_URL_ID, city.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(city)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        city.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(city))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        city.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(city)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCityWithPatch() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the city using partial update
        City partialUpdatedCity = new City();
        partialUpdatedCity.setId(city.getId());

        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCity))
            )
            .andExpect(status().isOk());

        // Validate the City in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCityUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCity, city), getPersistedCity(city));
    }

    @Test
    @Transactional
    void fullUpdateCityWithPatch() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the city using partial update
        City partialUpdatedCity = new City();
        partialUpdatedCity.setId(city.getId());

        partialUpdatedCity.name(UPDATED_NAME);

        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCity))
            )
            .andExpect(status().isOk());

        // Validate the City in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCityUpdatableFieldsEquals(partialUpdatedCity, getPersistedCity(partialUpdatedCity));
    }

    @Test
    @Transactional
    void patchNonExistingCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        city.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(patch(ENTITY_API_URL_ID, city.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(city)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        city.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(city))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        city.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(city)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCity() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the city
        restCityMockMvc
            .perform(delete(ENTITY_API_URL_ID, city.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cityRepository.count();
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

    protected City getPersistedCity(City city) {
        return cityRepository.findById(city.getId()).orElseThrow();
    }

    protected void assertPersistedCityToMatchAllProperties(City expectedCity) {
        assertCityAllPropertiesEquals(expectedCity, getPersistedCity(expectedCity));
    }

    protected void assertPersistedCityToMatchUpdatableProperties(City expectedCity) {
        assertCityAllUpdatablePropertiesEquals(expectedCity, getPersistedCity(expectedCity));
    }
}
