package com.syriabazaar.web.rest;

import static com.syriabazaar.domain.CarAsserts.*;
import static com.syriabazaar.web.rest.TestUtil.createUpdateProxyForBean;
import static com.syriabazaar.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syriabazaar.IntegrationTest;
import com.syriabazaar.domain.Car;
import com.syriabazaar.repository.CarRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link CarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarResourceIT {

    private static final Integer DEFAULT_YEAR = 1886;
    private static final Integer UPDATED_YEAR = 1887;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final Double DEFAULT_MILEAGE = 0D;
    private static final Double UPDATED_MILEAGE = 1D;

    private static final String DEFAULT_DRIVETRAIN = "AAAAAAAAAA";
    private static final String UPDATED_DRIVETRAIN = "BBBBBBBBBB";

    private static final String DEFAULT_ENGINE = "AAAAAAAAAA";
    private static final String UPDATED_ENGINE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSMISSION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSMISSION = "BBBBBBBBBB";

    private static final String DEFAULT_FUEL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FUEL_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERIOR_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_EXTERIOR_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_INTERIOR_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_INTERIOR_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_VIN = "AAAAAAAAAA";
    private static final String UPDATED_VIN = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_PUBLISHED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLISHED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_AD_NUMBER = 1L;
    private static final Long UPDATED_AD_NUMBER = 2L;

    private static final Integer DEFAULT_VIEWS = 0;
    private static final Integer UPDATED_VIEWS = 1;

    private static final Boolean DEFAULT_ALLOY_WHEELS = false;
    private static final Boolean UPDATED_ALLOY_WHEELS = true;

    private static final Boolean DEFAULT_SUNROOF = false;
    private static final Boolean UPDATED_SUNROOF = true;

    private static final Boolean DEFAULT_TINTED_GLASS = false;
    private static final Boolean UPDATED_TINTED_GLASS = true;

    private static final Boolean DEFAULT_LED_HEADLIGHTS = false;
    private static final Boolean UPDATED_LED_HEADLIGHTS = true;

    private static final Boolean DEFAULT_FOLDABLE_ROOF = false;
    private static final Boolean UPDATED_FOLDABLE_ROOF = true;

    private static final Boolean DEFAULT_TOW_HITCH = false;
    private static final Boolean UPDATED_TOW_HITCH = true;

    private static final Boolean DEFAULT_ADJUSTABLE_STEERING_WHEEL = false;
    private static final Boolean UPDATED_ADJUSTABLE_STEERING_WHEEL = true;

    private static final Boolean DEFAULT_AUTO_DIMMING_REARVIEW = false;
    private static final Boolean UPDATED_AUTO_DIMMING_REARVIEW = true;

    private static final Boolean DEFAULT_HEATED_FRONT_SEATS = false;
    private static final Boolean UPDATED_HEATED_FRONT_SEATS = true;

    private static final Boolean DEFAULT_LEATHER_SEATS = false;
    private static final Boolean UPDATED_LEATHER_SEATS = true;

    private static final Boolean DEFAULT_BLIND_SPOT_MONITOR = false;
    private static final Boolean UPDATED_BLIND_SPOT_MONITOR = true;

    private static final Boolean DEFAULT_ADAPTIVE_CRUISE_CONTROL = false;
    private static final Boolean UPDATED_ADAPTIVE_CRUISE_CONTROL = true;

    private static final Boolean DEFAULT_NAVIGATION_SYSTEM = false;
    private static final Boolean UPDATED_NAVIGATION_SYSTEM = true;

    private static final Boolean DEFAULT_BACKUP_CAMERA = false;
    private static final Boolean UPDATED_BACKUP_CAMERA = true;

    private static final Boolean DEFAULT_APPLE_CARPLAY = false;
    private static final Boolean UPDATED_APPLE_CARPLAY = true;

    private static final Boolean DEFAULT_ANDROID_AUTO = false;
    private static final Boolean UPDATED_ANDROID_AUTO = true;

    private static final Boolean DEFAULT_PREMIUM_SOUND_SYSTEM = false;
    private static final Boolean UPDATED_PREMIUM_SOUND_SYSTEM = true;

    private static final Boolean DEFAULT_IS_FIRST_OWN = false;
    private static final Boolean UPDATED_IS_FIRST_OWN = true;

    private static final Boolean DEFAULT_IS_ACCED_FREE = false;
    private static final Boolean UPDATED_IS_ACCED_FREE = true;

    private static final String ENTITY_API_URL = "/api/cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarMockMvc;

    private Car car;

    private Car insertedCar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity() {
        return new Car()
            .year(DEFAULT_YEAR)
            .price(DEFAULT_PRICE)
            .mileage(DEFAULT_MILEAGE)
            .drivetrain(DEFAULT_DRIVETRAIN)
            .engine(DEFAULT_ENGINE)
            .transmission(DEFAULT_TRANSMISSION)
            .fuelType(DEFAULT_FUEL_TYPE)
            .exteriorColor(DEFAULT_EXTERIOR_COLOR)
            .interiorColor(DEFAULT_INTERIOR_COLOR)
            .vin(DEFAULT_VIN)
            .location(DEFAULT_LOCATION)
            .description(DEFAULT_DESCRIPTION)
            .publishedDate(DEFAULT_PUBLISHED_DATE)
            .adNumber(DEFAULT_AD_NUMBER)
            .views(DEFAULT_VIEWS)
            .alloyWheels(DEFAULT_ALLOY_WHEELS)
            .sunroof(DEFAULT_SUNROOF)
            .tintedGlass(DEFAULT_TINTED_GLASS)
            .ledHeadlights(DEFAULT_LED_HEADLIGHTS)
            .foldableRoof(DEFAULT_FOLDABLE_ROOF)
            .towHitch(DEFAULT_TOW_HITCH)
            .adjustableSteeringWheel(DEFAULT_ADJUSTABLE_STEERING_WHEEL)
            .autoDimmingRearview(DEFAULT_AUTO_DIMMING_REARVIEW)
            .heatedFrontSeats(DEFAULT_HEATED_FRONT_SEATS)
            .leatherSeats(DEFAULT_LEATHER_SEATS)
            .blindSpotMonitor(DEFAULT_BLIND_SPOT_MONITOR)
            .adaptiveCruiseControl(DEFAULT_ADAPTIVE_CRUISE_CONTROL)
            .navigationSystem(DEFAULT_NAVIGATION_SYSTEM)
            .backupCamera(DEFAULT_BACKUP_CAMERA)
            .appleCarplay(DEFAULT_APPLE_CARPLAY)
            .androidAuto(DEFAULT_ANDROID_AUTO)
            .premiumSoundSystem(DEFAULT_PREMIUM_SOUND_SYSTEM)
            .isFirstOwn(DEFAULT_IS_FIRST_OWN)
            .isAccedFree(DEFAULT_IS_ACCED_FREE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity() {
        return new Car()
            .year(UPDATED_YEAR)
            .price(UPDATED_PRICE)
            .mileage(UPDATED_MILEAGE)
            .drivetrain(UPDATED_DRIVETRAIN)
            .engine(UPDATED_ENGINE)
            .transmission(UPDATED_TRANSMISSION)
            .fuelType(UPDATED_FUEL_TYPE)
            .exteriorColor(UPDATED_EXTERIOR_COLOR)
            .interiorColor(UPDATED_INTERIOR_COLOR)
            .vin(UPDATED_VIN)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .publishedDate(UPDATED_PUBLISHED_DATE)
            .adNumber(UPDATED_AD_NUMBER)
            .views(UPDATED_VIEWS)
            .alloyWheels(UPDATED_ALLOY_WHEELS)
            .sunroof(UPDATED_SUNROOF)
            .tintedGlass(UPDATED_TINTED_GLASS)
            .ledHeadlights(UPDATED_LED_HEADLIGHTS)
            .foldableRoof(UPDATED_FOLDABLE_ROOF)
            .towHitch(UPDATED_TOW_HITCH)
            .adjustableSteeringWheel(UPDATED_ADJUSTABLE_STEERING_WHEEL)
            .autoDimmingRearview(UPDATED_AUTO_DIMMING_REARVIEW)
            .heatedFrontSeats(UPDATED_HEATED_FRONT_SEATS)
            .leatherSeats(UPDATED_LEATHER_SEATS)
            .blindSpotMonitor(UPDATED_BLIND_SPOT_MONITOR)
            .adaptiveCruiseControl(UPDATED_ADAPTIVE_CRUISE_CONTROL)
            .navigationSystem(UPDATED_NAVIGATION_SYSTEM)
            .backupCamera(UPDATED_BACKUP_CAMERA)
            .appleCarplay(UPDATED_APPLE_CARPLAY)
            .androidAuto(UPDATED_ANDROID_AUTO)
            .premiumSoundSystem(UPDATED_PREMIUM_SOUND_SYSTEM)
            .isFirstOwn(UPDATED_IS_FIRST_OWN)
            .isAccedFree(UPDATED_IS_ACCED_FREE);
    }

    @BeforeEach
    void initTest() {
        car = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCar != null) {
            carRepository.delete(insertedCar);
            insertedCar = null;
        }
    }

    @Test
    @Transactional
    void createCar() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Car
        var returnedCar = om.readValue(
            restCarMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(car)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Car.class
        );

        // Validate the Car in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCarUpdatableFieldsEquals(returnedCar, getPersistedCar(returnedCar));

        insertedCar = returnedCar;
    }

    @Test
    @Transactional
    void createCarWithExistingId() throws Exception {
        // Create the Car with an existing ID
        car.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCars() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].mileage").value(hasItem(DEFAULT_MILEAGE)))
            .andExpect(jsonPath("$.[*].drivetrain").value(hasItem(DEFAULT_DRIVETRAIN)))
            .andExpect(jsonPath("$.[*].engine").value(hasItem(DEFAULT_ENGINE)))
            .andExpect(jsonPath("$.[*].transmission").value(hasItem(DEFAULT_TRANSMISSION)))
            .andExpect(jsonPath("$.[*].fuelType").value(hasItem(DEFAULT_FUEL_TYPE)))
            .andExpect(jsonPath("$.[*].exteriorColor").value(hasItem(DEFAULT_EXTERIOR_COLOR)))
            .andExpect(jsonPath("$.[*].interiorColor").value(hasItem(DEFAULT_INTERIOR_COLOR)))
            .andExpect(jsonPath("$.[*].vin").value(hasItem(DEFAULT_VIN)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(DEFAULT_PUBLISHED_DATE.toString())))
            .andExpect(jsonPath("$.[*].adNumber").value(hasItem(DEFAULT_AD_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].views").value(hasItem(DEFAULT_VIEWS)))
            .andExpect(jsonPath("$.[*].alloyWheels").value(hasItem(DEFAULT_ALLOY_WHEELS)))
            .andExpect(jsonPath("$.[*].sunroof").value(hasItem(DEFAULT_SUNROOF)))
            .andExpect(jsonPath("$.[*].tintedGlass").value(hasItem(DEFAULT_TINTED_GLASS)))
            .andExpect(jsonPath("$.[*].ledHeadlights").value(hasItem(DEFAULT_LED_HEADLIGHTS)))
            .andExpect(jsonPath("$.[*].foldableRoof").value(hasItem(DEFAULT_FOLDABLE_ROOF)))
            .andExpect(jsonPath("$.[*].towHitch").value(hasItem(DEFAULT_TOW_HITCH)))
            .andExpect(jsonPath("$.[*].adjustableSteeringWheel").value(hasItem(DEFAULT_ADJUSTABLE_STEERING_WHEEL)))
            .andExpect(jsonPath("$.[*].autoDimmingRearview").value(hasItem(DEFAULT_AUTO_DIMMING_REARVIEW)))
            .andExpect(jsonPath("$.[*].heatedFrontSeats").value(hasItem(DEFAULT_HEATED_FRONT_SEATS)))
            .andExpect(jsonPath("$.[*].leatherSeats").value(hasItem(DEFAULT_LEATHER_SEATS)))
            .andExpect(jsonPath("$.[*].blindSpotMonitor").value(hasItem(DEFAULT_BLIND_SPOT_MONITOR)))
            .andExpect(jsonPath("$.[*].adaptiveCruiseControl").value(hasItem(DEFAULT_ADAPTIVE_CRUISE_CONTROL)))
            .andExpect(jsonPath("$.[*].navigationSystem").value(hasItem(DEFAULT_NAVIGATION_SYSTEM)))
            .andExpect(jsonPath("$.[*].backupCamera").value(hasItem(DEFAULT_BACKUP_CAMERA)))
            .andExpect(jsonPath("$.[*].appleCarplay").value(hasItem(DEFAULT_APPLE_CARPLAY)))
            .andExpect(jsonPath("$.[*].androidAuto").value(hasItem(DEFAULT_ANDROID_AUTO)))
            .andExpect(jsonPath("$.[*].premiumSoundSystem").value(hasItem(DEFAULT_PREMIUM_SOUND_SYSTEM)))
            .andExpect(jsonPath("$.[*].isFirstOwn").value(hasItem(DEFAULT_IS_FIRST_OWN)))
            .andExpect(jsonPath("$.[*].isAccedFree").value(hasItem(DEFAULT_IS_ACCED_FREE)));
    }

    @Test
    @Transactional
    void getCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc
            .perform(get(ENTITY_API_URL_ID, car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.mileage").value(DEFAULT_MILEAGE))
            .andExpect(jsonPath("$.drivetrain").value(DEFAULT_DRIVETRAIN))
            .andExpect(jsonPath("$.engine").value(DEFAULT_ENGINE))
            .andExpect(jsonPath("$.transmission").value(DEFAULT_TRANSMISSION))
            .andExpect(jsonPath("$.fuelType").value(DEFAULT_FUEL_TYPE))
            .andExpect(jsonPath("$.exteriorColor").value(DEFAULT_EXTERIOR_COLOR))
            .andExpect(jsonPath("$.interiorColor").value(DEFAULT_INTERIOR_COLOR))
            .andExpect(jsonPath("$.vin").value(DEFAULT_VIN))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.publishedDate").value(DEFAULT_PUBLISHED_DATE.toString()))
            .andExpect(jsonPath("$.adNumber").value(DEFAULT_AD_NUMBER.intValue()))
            .andExpect(jsonPath("$.views").value(DEFAULT_VIEWS))
            .andExpect(jsonPath("$.alloyWheels").value(DEFAULT_ALLOY_WHEELS))
            .andExpect(jsonPath("$.sunroof").value(DEFAULT_SUNROOF))
            .andExpect(jsonPath("$.tintedGlass").value(DEFAULT_TINTED_GLASS))
            .andExpect(jsonPath("$.ledHeadlights").value(DEFAULT_LED_HEADLIGHTS))
            .andExpect(jsonPath("$.foldableRoof").value(DEFAULT_FOLDABLE_ROOF))
            .andExpect(jsonPath("$.towHitch").value(DEFAULT_TOW_HITCH))
            .andExpect(jsonPath("$.adjustableSteeringWheel").value(DEFAULT_ADJUSTABLE_STEERING_WHEEL))
            .andExpect(jsonPath("$.autoDimmingRearview").value(DEFAULT_AUTO_DIMMING_REARVIEW))
            .andExpect(jsonPath("$.heatedFrontSeats").value(DEFAULT_HEATED_FRONT_SEATS))
            .andExpect(jsonPath("$.leatherSeats").value(DEFAULT_LEATHER_SEATS))
            .andExpect(jsonPath("$.blindSpotMonitor").value(DEFAULT_BLIND_SPOT_MONITOR))
            .andExpect(jsonPath("$.adaptiveCruiseControl").value(DEFAULT_ADAPTIVE_CRUISE_CONTROL))
            .andExpect(jsonPath("$.navigationSystem").value(DEFAULT_NAVIGATION_SYSTEM))
            .andExpect(jsonPath("$.backupCamera").value(DEFAULT_BACKUP_CAMERA))
            .andExpect(jsonPath("$.appleCarplay").value(DEFAULT_APPLE_CARPLAY))
            .andExpect(jsonPath("$.androidAuto").value(DEFAULT_ANDROID_AUTO))
            .andExpect(jsonPath("$.premiumSoundSystem").value(DEFAULT_PREMIUM_SOUND_SYSTEM))
            .andExpect(jsonPath("$.isFirstOwn").value(DEFAULT_IS_FIRST_OWN))
            .andExpect(jsonPath("$.isAccedFree").value(DEFAULT_IS_ACCED_FREE));
    }

    @Test
    @Transactional
    void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar
            .year(UPDATED_YEAR)
            .price(UPDATED_PRICE)
            .mileage(UPDATED_MILEAGE)
            .drivetrain(UPDATED_DRIVETRAIN)
            .engine(UPDATED_ENGINE)
            .transmission(UPDATED_TRANSMISSION)
            .fuelType(UPDATED_FUEL_TYPE)
            .exteriorColor(UPDATED_EXTERIOR_COLOR)
            .interiorColor(UPDATED_INTERIOR_COLOR)
            .vin(UPDATED_VIN)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .publishedDate(UPDATED_PUBLISHED_DATE)
            .adNumber(UPDATED_AD_NUMBER)
            .views(UPDATED_VIEWS)
            .alloyWheels(UPDATED_ALLOY_WHEELS)
            .sunroof(UPDATED_SUNROOF)
            .tintedGlass(UPDATED_TINTED_GLASS)
            .ledHeadlights(UPDATED_LED_HEADLIGHTS)
            .foldableRoof(UPDATED_FOLDABLE_ROOF)
            .towHitch(UPDATED_TOW_HITCH)
            .adjustableSteeringWheel(UPDATED_ADJUSTABLE_STEERING_WHEEL)
            .autoDimmingRearview(UPDATED_AUTO_DIMMING_REARVIEW)
            .heatedFrontSeats(UPDATED_HEATED_FRONT_SEATS)
            .leatherSeats(UPDATED_LEATHER_SEATS)
            .blindSpotMonitor(UPDATED_BLIND_SPOT_MONITOR)
            .adaptiveCruiseControl(UPDATED_ADAPTIVE_CRUISE_CONTROL)
            .navigationSystem(UPDATED_NAVIGATION_SYSTEM)
            .backupCamera(UPDATED_BACKUP_CAMERA)
            .appleCarplay(UPDATED_APPLE_CARPLAY)
            .androidAuto(UPDATED_ANDROID_AUTO)
            .premiumSoundSystem(UPDATED_PREMIUM_SOUND_SYSTEM)
            .isFirstOwn(UPDATED_IS_FIRST_OWN)
            .isAccedFree(UPDATED_IS_ACCED_FREE);

        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCar.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarToMatchAllProperties(updatedCar);
    }

    @Test
    @Transactional
    void putNonExistingCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL_ID, car.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(car)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarWithPatch() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar
            .mileage(UPDATED_MILEAGE)
            .drivetrain(UPDATED_DRIVETRAIN)
            .engine(UPDATED_ENGINE)
            .transmission(UPDATED_TRANSMISSION)
            .exteriorColor(UPDATED_EXTERIOR_COLOR)
            .interiorColor(UPDATED_INTERIOR_COLOR)
            .vin(UPDATED_VIN)
            .description(UPDATED_DESCRIPTION)
            .views(UPDATED_VIEWS)
            .tintedGlass(UPDATED_TINTED_GLASS)
            .ledHeadlights(UPDATED_LED_HEADLIGHTS)
            .foldableRoof(UPDATED_FOLDABLE_ROOF)
            .towHitch(UPDATED_TOW_HITCH)
            .adaptiveCruiseControl(UPDATED_ADAPTIVE_CRUISE_CONTROL)
            .navigationSystem(UPDATED_NAVIGATION_SYSTEM)
            .appleCarplay(UPDATED_APPLE_CARPLAY)
            .isFirstOwn(UPDATED_IS_FIRST_OWN);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCar, car), getPersistedCar(car));
    }

    @Test
    @Transactional
    void fullUpdateCarWithPatch() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar
            .year(UPDATED_YEAR)
            .price(UPDATED_PRICE)
            .mileage(UPDATED_MILEAGE)
            .drivetrain(UPDATED_DRIVETRAIN)
            .engine(UPDATED_ENGINE)
            .transmission(UPDATED_TRANSMISSION)
            .fuelType(UPDATED_FUEL_TYPE)
            .exteriorColor(UPDATED_EXTERIOR_COLOR)
            .interiorColor(UPDATED_INTERIOR_COLOR)
            .vin(UPDATED_VIN)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .publishedDate(UPDATED_PUBLISHED_DATE)
            .adNumber(UPDATED_AD_NUMBER)
            .views(UPDATED_VIEWS)
            .alloyWheels(UPDATED_ALLOY_WHEELS)
            .sunroof(UPDATED_SUNROOF)
            .tintedGlass(UPDATED_TINTED_GLASS)
            .ledHeadlights(UPDATED_LED_HEADLIGHTS)
            .foldableRoof(UPDATED_FOLDABLE_ROOF)
            .towHitch(UPDATED_TOW_HITCH)
            .adjustableSteeringWheel(UPDATED_ADJUSTABLE_STEERING_WHEEL)
            .autoDimmingRearview(UPDATED_AUTO_DIMMING_REARVIEW)
            .heatedFrontSeats(UPDATED_HEATED_FRONT_SEATS)
            .leatherSeats(UPDATED_LEATHER_SEATS)
            .blindSpotMonitor(UPDATED_BLIND_SPOT_MONITOR)
            .adaptiveCruiseControl(UPDATED_ADAPTIVE_CRUISE_CONTROL)
            .navigationSystem(UPDATED_NAVIGATION_SYSTEM)
            .backupCamera(UPDATED_BACKUP_CAMERA)
            .appleCarplay(UPDATED_APPLE_CARPLAY)
            .androidAuto(UPDATED_ANDROID_AUTO)
            .premiumSoundSystem(UPDATED_PREMIUM_SOUND_SYSTEM)
            .isFirstOwn(UPDATED_IS_FIRST_OWN)
            .isAccedFree(UPDATED_IS_ACCED_FREE);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarUpdatableFieldsEquals(partialUpdatedCar, getPersistedCar(partialUpdatedCar));
    }

    @Test
    @Transactional
    void patchNonExistingCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(patch(ENTITY_API_URL_ID, car.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(car)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the car
        restCarMockMvc.perform(delete(ENTITY_API_URL_ID, car.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carRepository.count();
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

    protected Car getPersistedCar(Car car) {
        return carRepository.findById(car.getId()).orElseThrow();
    }

    protected void assertPersistedCarToMatchAllProperties(Car expectedCar) {
        assertCarAllPropertiesEquals(expectedCar, getPersistedCar(expectedCar));
    }

    protected void assertPersistedCarToMatchUpdatableProperties(Car expectedCar) {
        assertCarAllUpdatablePropertiesEquals(expectedCar, getPersistedCar(expectedCar));
    }
}
