package com.syriabazaar.web.rest;

import com.syriabazaar.domain.City;
import com.syriabazaar.repository.CityRepository;
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
 * REST controller for managing {@link com.syriabazaar.domain.City}.
 */
@RestController
@RequestMapping("/api/cities")
@Transactional
public class CityResource {

    private static final Logger LOG = LoggerFactory.getLogger(CityResource.class);

    private static final String ENTITY_NAME = "city";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CityRepository cityRepository;

    public CityResource(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    /**
     * {@code POST  /cities} : Create a new city.
     *
     * @param city the city to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new city, or with status {@code 400 (Bad Request)} if the city has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<City> createCity(@Valid @RequestBody City city) throws URISyntaxException {
        LOG.debug("REST request to save City : {}", city);
        if (city.getId() != null) {
            throw new BadRequestAlertException("A new city cannot already have an ID", ENTITY_NAME, "idexists");
        }
        city = cityRepository.save(city);
        return ResponseEntity.created(new URI("/api/cities/" + city.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, city.getId().toString()))
            .body(city);
    }

    /**
     * {@code PUT  /cities/:id} : Updates an existing city.
     *
     * @param id the id of the city to save.
     * @param city the city to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated city,
     * or with status {@code 400 (Bad Request)} if the city is not valid,
     * or with status {@code 500 (Internal Server Error)} if the city couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody City city)
        throws URISyntaxException {
        LOG.debug("REST request to update City : {}, {}", id, city);
        if (city.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, city.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        city = cityRepository.save(city);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, city.getId().toString()))
            .body(city);
    }

    /**
     * {@code PATCH  /cities/:id} : Partial updates given fields of an existing city, field will ignore if it is null
     *
     * @param id the id of the city to save.
     * @param city the city to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated city,
     * or with status {@code 400 (Bad Request)} if the city is not valid,
     * or with status {@code 404 (Not Found)} if the city is not found,
     * or with status {@code 500 (Internal Server Error)} if the city couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<City> partialUpdateCity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody City city
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update City partially : {}, {}", id, city);
        if (city.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, city.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<City> result = cityRepository
            .findById(city.getId())
            .map(existingCity -> {
                if (city.getName() != null) {
                    existingCity.setName(city.getName());
                }

                return existingCity;
            })
            .map(cityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, city.getId().toString())
        );
    }

    /**
     * {@code GET  /cities} : get all the cities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cities in body.
     */
    @GetMapping("")
    public List<City> getAllCities() {
        LOG.debug("REST request to get all Cities");
        return cityRepository.findAll();
    }

    /**
     * {@code GET  /cities/:id} : get the "id" city.
     *
     * @param id the id of the city to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the city, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<City> getCity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get City : {}", id);
        Optional<City> city = cityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(city);
    }

    /**
     * {@code DELETE  /cities/:id} : delete the "id" city.
     *
     * @param id the id of the city to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete City : {}", id);
        cityRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
