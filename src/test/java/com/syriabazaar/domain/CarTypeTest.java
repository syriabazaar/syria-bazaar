package com.syriabazaar.domain;

import static com.syriabazaar.domain.CarTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.syriabazaar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarType.class);
        CarType carType1 = getCarTypeSample1();
        CarType carType2 = new CarType();
        assertThat(carType1).isNotEqualTo(carType2);

        carType2.setId(carType1.getId());
        assertThat(carType1).isEqualTo(carType2);

        carType2 = getCarTypeSample2();
        assertThat(carType1).isNotEqualTo(carType2);
    }
}
