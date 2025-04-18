package com.syriabazaar.web.rest;

import com.syriabazaar.domain.Car;
import com.syriabazaar.repository.CarRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.syriabazaar.domain.Car}.
 */
@RestController
@RequestMapping("/api/cars")
@Transactional
public class CarResource {

    private static final Logger LOG = LoggerFactory.getLogger(CarResource.class);

    private static final String ENTITY_NAME = "car";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarRepository carRepository;

    public CarResource(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * {@code POST  /cars} : Create a new car.
     *
     * @param car the car to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new car, or with status {@code 400 (Bad Request)} if the car has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Car> createCar(@Valid @RequestBody Car car) throws URISyntaxException {
        LOG.debug("REST request to save Car : {}", car);
        if (car.getId() != null) {
            throw new BadRequestAlertException("A new car cannot already have an ID", ENTITY_NAME, "idexists");
        }
        car = carRepository.save(car);
        return ResponseEntity.created(new URI("/api/cars/" + car.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, car.getId().toString()))
            .body(car);
    }

    /**
     * {@code PUT  /cars/:id} : Updates an existing car.
     *
     * @param id the id of the car to save.
     * @param car the car to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated car,
     * or with status {@code 400 (Bad Request)} if the car is not valid,
     * or with status {@code 500 (Internal Server Error)} if the car couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Car car)
        throws URISyntaxException {
        LOG.debug("REST request to update Car : {}, {}", id, car);
        if (car.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, car.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        car = carRepository.save(car);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, car.getId().toString()))
            .body(car);
    }

    /**
     * {@code PATCH  /cars/:id} : Partial updates given fields of an existing car, field will ignore if it is null
     *
     * @param id the id of the car to save.
     * @param car the car to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated car,
     * or with status {@code 400 (Bad Request)} if the car is not valid,
     * or with status {@code 404 (Not Found)} if the car is not found,
     * or with status {@code 500 (Internal Server Error)} if the car couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Car> partialUpdateCar(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Car car)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Car partially : {}, {}", id, car);
        if (car.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, car.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Car> result = carRepository
            .findById(car.getId())
            .map(existingCar -> {
                if (car.getYear() != null) {
                    existingCar.setYear(car.getYear());
                }
                if (car.getPrice() != null) {
                    existingCar.setPrice(car.getPrice());
                }
                if (car.getMileage() != null) {
                    existingCar.setMileage(car.getMileage());
                }
                if (car.getDrivetrain() != null) {
                    existingCar.setDrivetrain(car.getDrivetrain());
                }
                if (car.getEngine() != null) {
                    existingCar.setEngine(car.getEngine());
                }
                if (car.getTransmission() != null) {
                    existingCar.setTransmission(car.getTransmission());
                }
                if (car.getFuelType() != null) {
                    existingCar.setFuelType(car.getFuelType());
                }
                if (car.getExteriorColor() != null) {
                    existingCar.setExteriorColor(car.getExteriorColor());
                }
                if (car.getInteriorColor() != null) {
                    existingCar.setInteriorColor(car.getInteriorColor());
                }
                if (car.getVin() != null) {
                    existingCar.setVin(car.getVin());
                }
                if (car.getLocation() != null) {
                    existingCar.setLocation(car.getLocation());
                }
                if (car.getDescription() != null) {
                    existingCar.setDescription(car.getDescription());
                }
                if (car.getPublishedDate() != null) {
                    existingCar.setPublishedDate(car.getPublishedDate());
                }
                if (car.getAdNumber() != null) {
                    existingCar.setAdNumber(car.getAdNumber());
                }
                if (car.getViews() != null) {
                    existingCar.setViews(car.getViews());
                }
                if (car.getAlloyWheels() != null) {
                    existingCar.setAlloyWheels(car.getAlloyWheels());
                }
                if (car.getSunroof() != null) {
                    existingCar.setSunroof(car.getSunroof());
                }
                if (car.getTintedGlass() != null) {
                    existingCar.setTintedGlass(car.getTintedGlass());
                }
                if (car.getLedHeadlights() != null) {
                    existingCar.setLedHeadlights(car.getLedHeadlights());
                }
                if (car.getFoldableRoof() != null) {
                    existingCar.setFoldableRoof(car.getFoldableRoof());
                }
                if (car.getTowHitch() != null) {
                    existingCar.setTowHitch(car.getTowHitch());
                }
                if (car.getAdjustableSteeringWheel() != null) {
                    existingCar.setAdjustableSteeringWheel(car.getAdjustableSteeringWheel());
                }
                if (car.getAutoDimmingRearview() != null) {
                    existingCar.setAutoDimmingRearview(car.getAutoDimmingRearview());
                }
                if (car.getHeatedFrontSeats() != null) {
                    existingCar.setHeatedFrontSeats(car.getHeatedFrontSeats());
                }
                if (car.getLeatherSeats() != null) {
                    existingCar.setLeatherSeats(car.getLeatherSeats());
                }
                if (car.getBlindSpotMonitor() != null) {
                    existingCar.setBlindSpotMonitor(car.getBlindSpotMonitor());
                }
                if (car.getAdaptiveCruiseControl() != null) {
                    existingCar.setAdaptiveCruiseControl(car.getAdaptiveCruiseControl());
                }
                if (car.getNavigationSystem() != null) {
                    existingCar.setNavigationSystem(car.getNavigationSystem());
                }
                if (car.getBackupCamera() != null) {
                    existingCar.setBackupCamera(car.getBackupCamera());
                }
                if (car.getAppleCarplay() != null) {
                    existingCar.setAppleCarplay(car.getAppleCarplay());
                }
                if (car.getAndroidAuto() != null) {
                    existingCar.setAndroidAuto(car.getAndroidAuto());
                }
                if (car.getPremiumSoundSystem() != null) {
                    existingCar.setPremiumSoundSystem(car.getPremiumSoundSystem());
                }
                if (car.getIsFirstOwn() != null) {
                    existingCar.setIsFirstOwn(car.getIsFirstOwn());
                }
                if (car.getIsAccedFree() != null) {
                    existingCar.setIsAccedFree(car.getIsAccedFree());
                }

                return existingCar;
            })
            .map(carRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, car.getId().toString())
        );
    }

    /**
     * {@code GET  /cars} : get all the cars.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cars in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Car>> getAllCars(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Cars");
        Page<Car> page = carRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cars/:id} : get the "id" car.
     *
     * @param id the id of the car to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the car, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCar(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Car : {}", id);
        Optional<Car> car = carRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(car);
    }

    /**
     * {@code DELETE  /cars/:id} : delete the "id" car.
     *
     * @param id the id of the car to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Car : {}", id);
        carRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
