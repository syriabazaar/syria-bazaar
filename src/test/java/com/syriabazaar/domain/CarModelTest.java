package com.syriabazaar.domain;

import static com.syriabazaar.domain.BrandTestSamples.*;
import static com.syriabazaar.domain.CarModelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.syriabazaar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarModel.class);
        CarModel carModel1 = getCarModelSample1();
        CarModel carModel2 = new CarModel();
        assertThat(carModel1).isNotEqualTo(carModel2);

        carModel2.setId(carModel1.getId());
        assertThat(carModel1).isEqualTo(carModel2);

        carModel2 = getCarModelSample2();
        assertThat(carModel1).isNotEqualTo(carModel2);
    }

    @Test
    void brandTest() {
        CarModel carModel = getCarModelRandomSampleGenerator();
        Brand brandBack = getBrandRandomSampleGenerator();

        carModel.setBrand(brandBack);
        assertThat(carModel.getBrand()).isEqualTo(brandBack);

        carModel.brand(null);
        assertThat(carModel.getBrand()).isNull();
    }
}
