package com.syriabazaar.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SellerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Seller getSellerSample1() {
        return new Seller()
            .id(1L)
            .name("name1")
            .address("address1")
            .mobileNo("mobileNo1")
            .whatsApp("whatsApp1")
            .facebook("facebook1")
            .insta("insta1");
    }

    public static Seller getSellerSample2() {
        return new Seller()
            .id(2L)
            .name("name2")
            .address("address2")
            .mobileNo("mobileNo2")
            .whatsApp("whatsApp2")
            .facebook("facebook2")
            .insta("insta2");
    }

    public static Seller getSellerRandomSampleGenerator() {
        return new Seller()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .mobileNo(UUID.randomUUID().toString())
            .whatsApp(UUID.randomUUID().toString())
            .facebook(UUID.randomUUID().toString())
            .insta(UUID.randomUUID().toString());
    }
}
