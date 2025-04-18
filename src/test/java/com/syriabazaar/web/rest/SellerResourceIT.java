package com.syriabazaar.web.rest;

import static com.syriabazaar.domain.SellerAsserts.*;
import static com.syriabazaar.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syriabazaar.IntegrationTest;
import com.syriabazaar.domain.Seller;
import com.syriabazaar.repository.SellerRepository;
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
 * Integration tests for the {@link SellerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SellerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_NO = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_WHATS_APP = "AAAAAAAAAA";
    private static final String UPDATED_WHATS_APP = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_INSTA = "AAAAAAAAAA";
    private static final String UPDATED_INSTA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sellers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSellerMockMvc;

    private Seller seller;

    private Seller insertedSeller;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seller createEntity() {
        return new Seller()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .mobileNo(DEFAULT_MOBILE_NO)
            .whatsApp(DEFAULT_WHATS_APP)
            .facebook(DEFAULT_FACEBOOK)
            .insta(DEFAULT_INSTA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seller createUpdatedEntity() {
        return new Seller()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .mobileNo(UPDATED_MOBILE_NO)
            .whatsApp(UPDATED_WHATS_APP)
            .facebook(UPDATED_FACEBOOK)
            .insta(UPDATED_INSTA);
    }

    @BeforeEach
    void initTest() {
        seller = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSeller != null) {
            sellerRepository.delete(insertedSeller);
            insertedSeller = null;
        }
    }

    @Test
    @Transactional
    void createSeller() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Seller
        var returnedSeller = om.readValue(
            restSellerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seller)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Seller.class
        );

        // Validate the Seller in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSellerUpdatableFieldsEquals(returnedSeller, getPersistedSeller(returnedSeller));

        insertedSeller = returnedSeller;
    }

    @Test
    @Transactional
    void createSellerWithExistingId() throws Exception {
        // Create the Seller with an existing ID
        seller.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seller)))
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seller.setName(null);

        // Create the Seller, which fails.

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seller)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seller.setAddress(null);

        // Create the Seller, which fails.

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seller)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSellers() throws Exception {
        // Initialize the database
        insertedSeller = sellerRepository.saveAndFlush(seller);

        // Get all the sellerList
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seller.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO)))
            .andExpect(jsonPath("$.[*].whatsApp").value(hasItem(DEFAULT_WHATS_APP)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].insta").value(hasItem(DEFAULT_INSTA)));
    }

    @Test
    @Transactional
    void getSeller() throws Exception {
        // Initialize the database
        insertedSeller = sellerRepository.saveAndFlush(seller);

        // Get the seller
        restSellerMockMvc
            .perform(get(ENTITY_API_URL_ID, seller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seller.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.mobileNo").value(DEFAULT_MOBILE_NO))
            .andExpect(jsonPath("$.whatsApp").value(DEFAULT_WHATS_APP))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK))
            .andExpect(jsonPath("$.insta").value(DEFAULT_INSTA));
    }

    @Test
    @Transactional
    void getNonExistingSeller() throws Exception {
        // Get the seller
        restSellerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeller() throws Exception {
        // Initialize the database
        insertedSeller = sellerRepository.saveAndFlush(seller);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the seller
        Seller updatedSeller = sellerRepository.findById(seller.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSeller are not directly saved in db
        em.detach(updatedSeller);
        updatedSeller
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .mobileNo(UPDATED_MOBILE_NO)
            .whatsApp(UPDATED_WHATS_APP)
            .facebook(UPDATED_FACEBOOK)
            .insta(UPDATED_INSTA);

        restSellerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeller.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSeller))
            )
            .andExpect(status().isOk());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSellerToMatchAllProperties(updatedSeller);
    }

    @Test
    @Transactional
    void putNonExistingSeller() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seller.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(put(ENTITY_API_URL_ID, seller.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seller)))
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeller() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seller.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(seller))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeller() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seller.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seller)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSellerWithPatch() throws Exception {
        // Initialize the database
        insertedSeller = sellerRepository.saveAndFlush(seller);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the seller using partial update
        Seller partialUpdatedSeller = new Seller();
        partialUpdatedSeller.setId(seller.getId());

        partialUpdatedSeller.mobileNo(UPDATED_MOBILE_NO).facebook(UPDATED_FACEBOOK).insta(UPDATED_INSTA);

        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeller.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeller))
            )
            .andExpect(status().isOk());

        // Validate the Seller in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSellerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSeller, seller), getPersistedSeller(seller));
    }

    @Test
    @Transactional
    void fullUpdateSellerWithPatch() throws Exception {
        // Initialize the database
        insertedSeller = sellerRepository.saveAndFlush(seller);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the seller using partial update
        Seller partialUpdatedSeller = new Seller();
        partialUpdatedSeller.setId(seller.getId());

        partialUpdatedSeller
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .mobileNo(UPDATED_MOBILE_NO)
            .whatsApp(UPDATED_WHATS_APP)
            .facebook(UPDATED_FACEBOOK)
            .insta(UPDATED_INSTA);

        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeller.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeller))
            )
            .andExpect(status().isOk());

        // Validate the Seller in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSellerUpdatableFieldsEquals(partialUpdatedSeller, getPersistedSeller(partialUpdatedSeller));
    }

    @Test
    @Transactional
    void patchNonExistingSeller() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seller.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seller.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(seller))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeller() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seller.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(seller))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeller() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seller.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(seller)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeller() throws Exception {
        // Initialize the database
        insertedSeller = sellerRepository.saveAndFlush(seller);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the seller
        restSellerMockMvc
            .perform(delete(ENTITY_API_URL_ID, seller.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sellerRepository.count();
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

    protected Seller getPersistedSeller(Seller seller) {
        return sellerRepository.findById(seller.getId()).orElseThrow();
    }

    protected void assertPersistedSellerToMatchAllProperties(Seller expectedSeller) {
        assertSellerAllPropertiesEquals(expectedSeller, getPersistedSeller(expectedSeller));
    }

    protected void assertPersistedSellerToMatchUpdatableProperties(Seller expectedSeller) {
        assertSellerAllUpdatablePropertiesEquals(expectedSeller, getPersistedSeller(expectedSeller));
    }
}
