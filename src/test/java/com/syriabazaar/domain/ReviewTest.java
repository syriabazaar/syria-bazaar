package com.syriabazaar.domain;

import static com.syriabazaar.domain.ReviewTestSamples.*;
import static com.syriabazaar.domain.SellerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.syriabazaar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Review.class);
        Review review1 = getReviewSample1();
        Review review2 = new Review();
        assertThat(review1).isNotEqualTo(review2);

        review2.setId(review1.getId());
        assertThat(review1).isEqualTo(review2);

        review2 = getReviewSample2();
        assertThat(review1).isNotEqualTo(review2);
    }

    @Test
    void fromUserTest() {
        Review review = getReviewRandomSampleGenerator();
        Seller sellerBack = getSellerRandomSampleGenerator();

        review.setFromUser(sellerBack);
        assertThat(review.getFromUser()).isEqualTo(sellerBack);

        review.fromUser(null);
        assertThat(review.getFromUser()).isNull();
    }

    @Test
    void toUserTest() {
        Review review = getReviewRandomSampleGenerator();
        Seller sellerBack = getSellerRandomSampleGenerator();

        review.setToUser(sellerBack);
        assertThat(review.getToUser()).isEqualTo(sellerBack);

        review.toUser(null);
        assertThat(review.getToUser()).isNull();
    }
}
