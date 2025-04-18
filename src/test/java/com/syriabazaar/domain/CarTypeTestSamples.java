package com.syriabazaar.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CarTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CarType getCarTypeSample1() {
        return new CarType().id(1L).typeName("typeName1");
    }

    public static CarType getCarTypeSample2() {
        return new CarType().id(2L).typeName("typeName2");
    }

    public static CarType getCarTypeRandomSampleGenerator() {
        return new CarType().id(longCount.incrementAndGet()).typeName(UUID.randomUUID().toString());
    }
}
