package com.syriabazaar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Min(value = 1886)
    @Column(name = "year")
    private Integer year;

    @DecimalMin(value = "0")
    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @DecimalMin(value = "0")
    @Column(name = "mileage")
    private Double mileage;

    @Column(name = "drivetrain")
    private String drivetrain;

    @Column(name = "engine")
    private String engine;

    @Column(name = "transmission")
    private String transmission;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "exterior_color")
    private String exteriorColor;

    @Column(name = "interior_color")
    private String interiorColor;

    @Column(name = "vin", unique = true)
    private String vin;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "published_date")
    private Instant publishedDate;

    @Column(name = "ad_number", unique = true)
    private Long adNumber;

    @Min(value = 0)
    @Column(name = "views")
    private Integer views;

    @Column(name = "alloy_wheels")
    private Boolean alloyWheels;

    @Column(name = "sunroof")
    private Boolean sunroof;

    @Column(name = "tinted_glass")
    private Boolean tintedGlass;

    @Column(name = "led_headlights")
    private Boolean ledHeadlights;

    @Column(name = "foldable_roof")
    private Boolean foldableRoof;

    @Column(name = "tow_hitch")
    private Boolean towHitch;

    @Column(name = "adjustable_steering_wheel")
    private Boolean adjustableSteeringWheel;

    @Column(name = "auto_dimming_rearview")
    private Boolean autoDimmingRearview;

    @Column(name = "heated_front_seats")
    private Boolean heatedFrontSeats;

    @Column(name = "leather_seats")
    private Boolean leatherSeats;

    @Column(name = "blind_spot_monitor")
    private Boolean blindSpotMonitor;

    @Column(name = "adaptive_cruise_control")
    private Boolean adaptiveCruiseControl;

    @Column(name = "navigation_system")
    private Boolean navigationSystem;

    @Column(name = "backup_camera")
    private Boolean backupCamera;

    @Column(name = "apple_carplay")
    private Boolean appleCarplay;

    @Column(name = "android_auto")
    private Boolean androidAuto;

    @Column(name = "premium_sound_system")
    private Boolean premiumSoundSystem;

    @Column(name = "is_first_own")
    private Boolean isFirstOwn;

    @Column(name = "is_acced_free")
    private Boolean isAccedFree;

    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    private CarType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "brand" }, allowSetters = true)
    private CarModel model;

    @ManyToOne(fetch = FetchType.LAZY)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Car id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return this.year;
    }

    public Car year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Car price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getMileage() {
        return this.mileage;
    }

    public Car mileage(Double mileage) {
        this.setMileage(mileage);
        return this;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public String getDrivetrain() {
        return this.drivetrain;
    }

    public Car drivetrain(String drivetrain) {
        this.setDrivetrain(drivetrain);
        return this;
    }

    public void setDrivetrain(String drivetrain) {
        this.drivetrain = drivetrain;
    }

    public String getEngine() {
        return this.engine;
    }

    public Car engine(String engine) {
        this.setEngine(engine);
        return this;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getTransmission() {
        return this.transmission;
    }

    public Car transmission(String transmission) {
        this.setTransmission(transmission);
        return this;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getFuelType() {
        return this.fuelType;
    }

    public Car fuelType(String fuelType) {
        this.setFuelType(fuelType);
        return this;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getExteriorColor() {
        return this.exteriorColor;
    }

    public Car exteriorColor(String exteriorColor) {
        this.setExteriorColor(exteriorColor);
        return this;
    }

    public void setExteriorColor(String exteriorColor) {
        this.exteriorColor = exteriorColor;
    }

    public String getInteriorColor() {
        return this.interiorColor;
    }

    public Car interiorColor(String interiorColor) {
        this.setInteriorColor(interiorColor);
        return this;
    }

    public void setInteriorColor(String interiorColor) {
        this.interiorColor = interiorColor;
    }

    public String getVin() {
        return this.vin;
    }

    public Car vin(String vin) {
        this.setVin(vin);
        return this;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getLocation() {
        return this.location;
    }

    public Car location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return this.description;
    }

    public Car description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getPublishedDate() {
        return this.publishedDate;
    }

    public Car publishedDate(Instant publishedDate) {
        this.setPublishedDate(publishedDate);
        return this;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Long getAdNumber() {
        return this.adNumber;
    }

    public Car adNumber(Long adNumber) {
        this.setAdNumber(adNumber);
        return this;
    }

    public void setAdNumber(Long adNumber) {
        this.adNumber = adNumber;
    }

    public Integer getViews() {
        return this.views;
    }

    public Car views(Integer views) {
        this.setViews(views);
        return this;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Boolean getAlloyWheels() {
        return this.alloyWheels;
    }

    public Car alloyWheels(Boolean alloyWheels) {
        this.setAlloyWheels(alloyWheels);
        return this;
    }

    public void setAlloyWheels(Boolean alloyWheels) {
        this.alloyWheels = alloyWheels;
    }

    public Boolean getSunroof() {
        return this.sunroof;
    }

    public Car sunroof(Boolean sunroof) {
        this.setSunroof(sunroof);
        return this;
    }

    public void setSunroof(Boolean sunroof) {
        this.sunroof = sunroof;
    }

    public Boolean getTintedGlass() {
        return this.tintedGlass;
    }

    public Car tintedGlass(Boolean tintedGlass) {
        this.setTintedGlass(tintedGlass);
        return this;
    }

    public void setTintedGlass(Boolean tintedGlass) {
        this.tintedGlass = tintedGlass;
    }

    public Boolean getLedHeadlights() {
        return this.ledHeadlights;
    }

    public Car ledHeadlights(Boolean ledHeadlights) {
        this.setLedHeadlights(ledHeadlights);
        return this;
    }

    public void setLedHeadlights(Boolean ledHeadlights) {
        this.ledHeadlights = ledHeadlights;
    }

    public Boolean getFoldableRoof() {
        return this.foldableRoof;
    }

    public Car foldableRoof(Boolean foldableRoof) {
        this.setFoldableRoof(foldableRoof);
        return this;
    }

    public void setFoldableRoof(Boolean foldableRoof) {
        this.foldableRoof = foldableRoof;
    }

    public Boolean getTowHitch() {
        return this.towHitch;
    }

    public Car towHitch(Boolean towHitch) {
        this.setTowHitch(towHitch);
        return this;
    }

    public void setTowHitch(Boolean towHitch) {
        this.towHitch = towHitch;
    }

    public Boolean getAdjustableSteeringWheel() {
        return this.adjustableSteeringWheel;
    }

    public Car adjustableSteeringWheel(Boolean adjustableSteeringWheel) {
        this.setAdjustableSteeringWheel(adjustableSteeringWheel);
        return this;
    }

    public void setAdjustableSteeringWheel(Boolean adjustableSteeringWheel) {
        this.adjustableSteeringWheel = adjustableSteeringWheel;
    }

    public Boolean getAutoDimmingRearview() {
        return this.autoDimmingRearview;
    }

    public Car autoDimmingRearview(Boolean autoDimmingRearview) {
        this.setAutoDimmingRearview(autoDimmingRearview);
        return this;
    }

    public void setAutoDimmingRearview(Boolean autoDimmingRearview) {
        this.autoDimmingRearview = autoDimmingRearview;
    }

    public Boolean getHeatedFrontSeats() {
        return this.heatedFrontSeats;
    }

    public Car heatedFrontSeats(Boolean heatedFrontSeats) {
        this.setHeatedFrontSeats(heatedFrontSeats);
        return this;
    }

    public void setHeatedFrontSeats(Boolean heatedFrontSeats) {
        this.heatedFrontSeats = heatedFrontSeats;
    }

    public Boolean getLeatherSeats() {
        return this.leatherSeats;
    }

    public Car leatherSeats(Boolean leatherSeats) {
        this.setLeatherSeats(leatherSeats);
        return this;
    }

    public void setLeatherSeats(Boolean leatherSeats) {
        this.leatherSeats = leatherSeats;
    }

    public Boolean getBlindSpotMonitor() {
        return this.blindSpotMonitor;
    }

    public Car blindSpotMonitor(Boolean blindSpotMonitor) {
        this.setBlindSpotMonitor(blindSpotMonitor);
        return this;
    }

    public void setBlindSpotMonitor(Boolean blindSpotMonitor) {
        this.blindSpotMonitor = blindSpotMonitor;
    }

    public Boolean getAdaptiveCruiseControl() {
        return this.adaptiveCruiseControl;
    }

    public Car adaptiveCruiseControl(Boolean adaptiveCruiseControl) {
        this.setAdaptiveCruiseControl(adaptiveCruiseControl);
        return this;
    }

    public void setAdaptiveCruiseControl(Boolean adaptiveCruiseControl) {
        this.adaptiveCruiseControl = adaptiveCruiseControl;
    }

    public Boolean getNavigationSystem() {
        return this.navigationSystem;
    }

    public Car navigationSystem(Boolean navigationSystem) {
        this.setNavigationSystem(navigationSystem);
        return this;
    }

    public void setNavigationSystem(Boolean navigationSystem) {
        this.navigationSystem = navigationSystem;
    }

    public Boolean getBackupCamera() {
        return this.backupCamera;
    }

    public Car backupCamera(Boolean backupCamera) {
        this.setBackupCamera(backupCamera);
        return this;
    }

    public void setBackupCamera(Boolean backupCamera) {
        this.backupCamera = backupCamera;
    }

    public Boolean getAppleCarplay() {
        return this.appleCarplay;
    }

    public Car appleCarplay(Boolean appleCarplay) {
        this.setAppleCarplay(appleCarplay);
        return this;
    }

    public void setAppleCarplay(Boolean appleCarplay) {
        this.appleCarplay = appleCarplay;
    }

    public Boolean getAndroidAuto() {
        return this.androidAuto;
    }

    public Car androidAuto(Boolean androidAuto) {
        this.setAndroidAuto(androidAuto);
        return this;
    }

    public void setAndroidAuto(Boolean androidAuto) {
        this.androidAuto = androidAuto;
    }

    public Boolean getPremiumSoundSystem() {
        return this.premiumSoundSystem;
    }

    public Car premiumSoundSystem(Boolean premiumSoundSystem) {
        this.setPremiumSoundSystem(premiumSoundSystem);
        return this;
    }

    public void setPremiumSoundSystem(Boolean premiumSoundSystem) {
        this.premiumSoundSystem = premiumSoundSystem;
    }

    public Boolean getIsFirstOwn() {
        return this.isFirstOwn;
    }

    public Car isFirstOwn(Boolean isFirstOwn) {
        this.setIsFirstOwn(isFirstOwn);
        return this;
    }

    public void setIsFirstOwn(Boolean isFirstOwn) {
        this.isFirstOwn = isFirstOwn;
    }

    public Boolean getIsAccedFree() {
        return this.isAccedFree;
    }

    public Car isAccedFree(Boolean isAccedFree) {
        this.setIsAccedFree(isAccedFree);
        return this;
    }

    public void setIsAccedFree(Boolean isAccedFree) {
        this.isAccedFree = isAccedFree;
    }

    public Brand getBrand() {
        return this.brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Car brand(Brand brand) {
        this.setBrand(brand);
        return this;
    }

    public Seller getSeller() {
        return this.seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Car seller(Seller seller) {
        this.setSeller(seller);
        return this;
    }

    public CarType getType() {
        return this.type;
    }

    public void setType(CarType carType) {
        this.type = carType;
    }

    public Car type(CarType carType) {
        this.setType(carType);
        return this;
    }

    public CarModel getModel() {
        return this.model;
    }

    public void setModel(CarModel carModel) {
        this.model = carModel;
    }

    public Car model(CarModel carModel) {
        this.setModel(carModel);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Car city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        return getId() != null && getId().equals(((Car) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", price=" + getPrice() +
            ", mileage=" + getMileage() +
            ", drivetrain='" + getDrivetrain() + "'" +
            ", engine='" + getEngine() + "'" +
            ", transmission='" + getTransmission() + "'" +
            ", fuelType='" + getFuelType() + "'" +
            ", exteriorColor='" + getExteriorColor() + "'" +
            ", interiorColor='" + getInteriorColor() + "'" +
            ", vin='" + getVin() + "'" +
            ", location='" + getLocation() + "'" +
            ", description='" + getDescription() + "'" +
            ", publishedDate='" + getPublishedDate() + "'" +
            ", adNumber=" + getAdNumber() +
            ", views=" + getViews() +
            ", alloyWheels='" + getAlloyWheels() + "'" +
            ", sunroof='" + getSunroof() + "'" +
            ", tintedGlass='" + getTintedGlass() + "'" +
            ", ledHeadlights='" + getLedHeadlights() + "'" +
            ", foldableRoof='" + getFoldableRoof() + "'" +
            ", towHitch='" + getTowHitch() + "'" +
            ", adjustableSteeringWheel='" + getAdjustableSteeringWheel() + "'" +
            ", autoDimmingRearview='" + getAutoDimmingRearview() + "'" +
            ", heatedFrontSeats='" + getHeatedFrontSeats() + "'" +
            ", leatherSeats='" + getLeatherSeats() + "'" +
            ", blindSpotMonitor='" + getBlindSpotMonitor() + "'" +
            ", adaptiveCruiseControl='" + getAdaptiveCruiseControl() + "'" +
            ", navigationSystem='" + getNavigationSystem() + "'" +
            ", backupCamera='" + getBackupCamera() + "'" +
            ", appleCarplay='" + getAppleCarplay() + "'" +
            ", androidAuto='" + getAndroidAuto() + "'" +
            ", premiumSoundSystem='" + getPremiumSoundSystem() + "'" +
            ", isFirstOwn='" + getIsFirstOwn() + "'" +
            ", isAccedFree='" + getIsAccedFree() + "'" +
            "}";
    }
}
