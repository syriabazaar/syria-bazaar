package com.syriabazaar.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Review.
 */
@Entity
@Table(name = "review")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "rev_val")
    private Integer revVal;

    @Column(name = "rev_txt")
    private String revTxt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Seller fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private Seller toUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Review id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Review createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getRevVal() {
        return this.revVal;
    }

    public Review revVal(Integer revVal) {
        this.setRevVal(revVal);
        return this;
    }

    public void setRevVal(Integer revVal) {
        this.revVal = revVal;
    }

    public String getRevTxt() {
        return this.revTxt;
    }

    public Review revTxt(String revTxt) {
        this.setRevTxt(revTxt);
        return this;
    }

    public void setRevTxt(String revTxt) {
        this.revTxt = revTxt;
    }

    public Seller getFromUser() {
        return this.fromUser;
    }

    public void setFromUser(Seller seller) {
        this.fromUser = seller;
    }

    public Review fromUser(Seller seller) {
        this.setFromUser(seller);
        return this;
    }

    public Seller getToUser() {
        return this.toUser;
    }

    public void setToUser(Seller seller) {
        this.toUser = seller;
    }

    public Review toUser(Seller seller) {
        this.setToUser(seller);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Review)) {
            return false;
        }
        return getId() != null && getId().equals(((Review) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Review{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", revVal=" + getRevVal() +
            ", revTxt='" + getRevTxt() + "'" +
            "}";
    }
}
