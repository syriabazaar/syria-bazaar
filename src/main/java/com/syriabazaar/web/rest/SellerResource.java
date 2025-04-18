package com.syriabazaar.web.rest;

import com.syriabazaar.domain.Seller;
import com.syriabazaar.repository.SellerRepository;
import com.syriabazaar.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.syriabazaar.domain.Seller}.
 */
@RestController
@RequestMapping("/api/sellers")
@Transactional
public class SellerResource {

    private static final Logger LOG = LoggerFactory.getLogger(SellerResource.class);

    private static final String ENTITY_NAME = "seller";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SellerRepository sellerRepository;

    public SellerResource(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * {@code POST  /sellers} : Create a new seller.
     *
     * @param seller the seller to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new seller, or with status {@code 400 (Bad Request)} if the seller has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Seller> createSeller(@Valid @RequestBody Seller seller) throws URISyntaxException {
        LOG.debug("REST request to save Seller : {}", seller);
        if (seller.getId() != null) {
            throw new BadRequestAlertException("A new seller cannot already have an ID", ENTITY_NAME, "idexists");
        }
        seller = sellerRepository.save(seller);
        return ResponseEntity.created(new URI("/api/sellers/" + seller.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, seller.getId().toString()))
            .body(seller);
    }

    /**
     * {@code PUT  /sellers/:id} : Updates an existing seller.
     *
     * @param id the id of the seller to save.
     * @param seller the seller to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seller,
     * or with status {@code 400 (Bad Request)} if the seller is not valid,
     * or with status {@code 500 (Internal Server Error)} if the seller couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Seller> updateSeller(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Seller seller
    ) throws URISyntaxException {
        LOG.debug("REST request to update Seller : {}, {}", id, seller);
        if (seller.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seller.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sellerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        seller = sellerRepository.save(seller);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, seller.getId().toString()))
            .body(seller);
    }

    /**
     * {@code PATCH  /sellers/:id} : Partial updates given fields of an existing seller, field will ignore if it is null
     *
     * @param id the id of the seller to save.
     * @param seller the seller to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seller,
     * or with status {@code 400 (Bad Request)} if the seller is not valid,
     * or with status {@code 404 (Not Found)} if the seller is not found,
     * or with status {@code 500 (Internal Server Error)} if the seller couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Seller> partialUpdateSeller(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Seller seller
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Seller partially : {}, {}", id, seller);
        if (seller.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seller.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sellerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Seller> result = sellerRepository
            .findById(seller.getId())
            .map(existingSeller -> {
                if (seller.getName() != null) {
                    existingSeller.setName(seller.getName());
                }
                if (seller.getAddress() != null) {
                    existingSeller.setAddress(seller.getAddress());
                }
                if (seller.getMobileNo() != null) {
                    existingSeller.setMobileNo(seller.getMobileNo());
                }
                if (seller.getWhatsApp() != null) {
                    existingSeller.setWhatsApp(seller.getWhatsApp());
                }
                if (seller.getFacebook() != null) {
                    existingSeller.setFacebook(seller.getFacebook());
                }
                if (seller.getInsta() != null) {
                    existingSeller.setInsta(seller.getInsta());
                }

                return existingSeller;
            })
            .map(sellerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, seller.getId().toString())
        );
    }

    /**
     * {@code GET  /sellers} : get all the sellers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sellers in body.
     */
    @GetMapping("")
    public List<Seller> getAllSellers() {
        LOG.debug("REST request to get all Sellers");
        return sellerRepository.findAll();
    }

    /**
     * {@code GET  /sellers/:id} : get the "id" seller.
     *
     * @param id the id of the seller to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the seller, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSeller(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Seller : {}", id);
        Optional<Seller> seller = sellerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(seller);
    }

    /**
     * {@code DELETE  /sellers/:id} : delete the "id" seller.
     *
     * @param id the id of the seller to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Seller : {}", id);
        sellerRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
