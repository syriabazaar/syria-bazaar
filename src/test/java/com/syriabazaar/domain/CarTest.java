package com.syriabazaar.domain;

import static com.syriabazaar.domain.BrandTestSamples.*;
import static com.syriabazaar.domain.CarModelTestSamples.*;
import static com.syriabazaar.domain.CarTestSamples.*;
import static com.syriabazaar.domain.CarTypeTestSamples.*;
import static com.syriabazaar.domain.CityTestSamples.*;
import static com.syriabazaar.domain.SellerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.syriabazaar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Car.class);
        Car car1 = getCarSample1();
        Car car2 = new Car();
        assertThat(car1).isNotEqualTo(car2);

        car2.setId(car1.getId());
        assertThat(car1).isEqualTo(car2);

        car2 = getCarSample2();
        assertThat(car1).isNotEqualTo(car2);
    }

    @Test
    void brandTest() {
        Car car = getCarRandomSampleGenerator();
        Brand brandBack = getBrandRandomSampleGenerator();

        car.setBrand(brandBack);
        assertThat(car.getBrand()).isEqualTo(brandBack);

        car.brand(null);
        assertThat(car.getBrand()).isNull();
    }

    @Test
    void sellerTest() {
        Car car = getCarRandomSampleGenerator();
        Seller sellerBack = getSellerRandomSampleGenerator();

        car.setSeller(sellerBack);
        assertThat(car.getSeller()).isEqualTo(sellerBack);

        car.seller(null);
        assertThat(car.getSeller()).isNull();
    }

    @Test
    void typeTest() {
        Car car = getCarRandomSampleGenerator();
        CarType carTypeBack = getCarTypeRandomSampleGenerator();

        car.setType(carTypeBack);
        assertThat(car.getType()).isEqualTo(carTypeBack);

        car.type(null);
        assertThat(car.getType()).isNull();
    }

    @Test
    void modelTest() {
        Car car = getCarRandomSampleGenerator();
        CarModel carModelBack = getCarModelRandomSampleGenerator();

        car.setModel(carModelBack);
        assertThat(car.getModel()).isEqualTo(carModelBack);

        car.model(null);
        assertThat(car.getModel()).isNull();
    }

    @Test
    void cityTest() {
        Car car = getCarRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        car.setCity(cityBack);
        assertThat(car.getCity()).isEqualTo(cityBack);

        car.city(null);
        assertThat(car.getCity()).isNull();
    }
}
