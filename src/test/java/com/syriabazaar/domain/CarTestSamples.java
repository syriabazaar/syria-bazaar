package com.syriabazaar.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CarTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Car getCarSample1() {
        return new Car()
            .id(1L)
            .year(1)
            .drivetrain("drivetrain1")
            .engine("engine1")
            .transmission("transmission1")
            .fuelType("fuelType1")
            .exteriorColor("exteriorColor1")
            .interiorColor("interiorColor1")
            .vin("vin1")
            .location("location1")
            .description("description1")
            .adNumber(1L)
            .views(1);
    }

    public static Car getCarSample2() {
        return new Car()
            .id(2L)
            .year(2)
            .drivetrain("drivetrain2")
            .engine("engine2")
            .transmission("transmission2")
            .fuelType("fuelType2")
            .exteriorColor("exteriorColor2")
            .interiorColor("interiorColor2")
            .vin("vin2")
            .location("location2")
            .description("description2")
            .adNumber(2L)
            .views(2);
    }

    public static Car getCarRandomSampleGenerator() {
        return new Car()
            .id(longCount.incrementAndGet())
            .year(intCount.incrementAndGet())
            .drivetrain(UUID.randomUUID().toString())
            .engine(UUID.randomUUID().toString())
            .transmission(UUID.randomUUID().toString())
            .fuelType(UUID.randomUUID().toString())
            .exteriorColor(UUID.randomUUID().toString())
            .interiorColor(UUID.randomUUID().toString())
            .vin(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .adNumber(longCount.incrementAndGet())
            .views(intCount.incrementAndGet());
    }
}
