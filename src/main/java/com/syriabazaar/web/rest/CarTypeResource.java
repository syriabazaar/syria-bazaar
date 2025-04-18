package com.syriabazaar.web.rest;

import com.syriabazaar.domain.CarType;
import com.syriabazaar.repository.CarTypeRepository;
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
 * REST controller for managing {@link com.syriabazaar.domain.CarType}.
 */
@RestController
@RequestMapping("/api/car-types")
@Transactional
public class CarTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CarTypeResource.class);

    private static final String ENTITY_NAME = "carType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarTypeRepository carTypeRepository;

    public CarTypeResource(CarTypeRepository carTypeRepository) {
        this.carTypeRepository = carTypeRepository;
    }

    /**
     * {@code POST  /car-types} : Create a new carType.
     *
     * @param carType the carType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carType, or with status {@code 400 (Bad Request)} if the carType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CarType> createCarType(@Valid @RequestBody CarType carType) throws URISyntaxException {
        LOG.debug("REST request to save CarType : {}", carType);
        if (carType.getId() != null) {
            throw new BadRequestAlertException("A new carType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        carType = carTypeRepository.save(carType);
        return ResponseEntity.created(new URI("/api/car-types/" + carType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, carType.getId().toString()))
            .body(carType);
    }

    /**
     * {@code PUT  /car-types/:id} : Updates an existing carType.
     *
     * @param id the id of the carType to save.
     * @param carType the carType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carType,
     * or with status {@code 400 (Bad Request)} if the carType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarType> updateCarType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarType carType
    ) throws URISyntaxException {
        LOG.debug("REST request to update CarType : {}, {}", id, carType);
        if (carType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        carType = carTypeRepository.save(carType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carType.getId().toString()))
            .body(carType);
    }

    /**
     * {@code PATCH  /car-types/:id} : Partial updates given fields of an existing carType, field will ignore if it is null
     *
     * @param id the id of the carType to save.
     * @param carType the carType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carType,
     * or with status {@code 400 (Bad Request)} if the carType is not valid,
     * or with status {@code 404 (Not Found)} if the carType is not found,
     * or with status {@code 500 (Internal Server Error)} if the carType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarType> partialUpdateCarType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarType carType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CarType partially : {}, {}", id, carType);
        if (carType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarType> result = carTypeRepository
            .findById(carType.getId())
            .map(existingCarType -> {
                if (carType.getTypeName() != null) {
                    existingCarType.setTypeName(carType.getTypeName());
                }

                return existingCarType;
            })
            .map(carTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carType.getId().toString())
        );
    }

    /**
     * {@code GET  /car-types} : get all the carTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carTypes in body.
     */
    @GetMapping("")
    public List<CarType> getAllCarTypes() {
        LOG.debug("REST request to get all CarTypes");
        return carTypeRepository.findAll();
    }

    /**
     * {@code GET  /car-types/:id} : get the "id" carType.
     *
     * @param id the id of the carType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarType> getCarType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CarType : {}", id);
        Optional<CarType> carType = carTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(carType);
    }

    /**
     * {@code DELETE  /car-types/:id} : delete the "id" carType.
     *
     * @param id the id of the carType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CarType : {}", id);
        carTypeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
