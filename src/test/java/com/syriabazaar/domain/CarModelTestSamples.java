package com.syriabazaar.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CarModelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CarModel getCarModelSample1() {
        return new CarModel().id(1L).name("name1");
    }

    public static CarModel getCarModelSample2() {
        return new CarModel().id(2L).name("name2");
    }

    public static CarModel getCarModelRandomSampleGenerator() {
        return new CarModel().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
