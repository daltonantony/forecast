package com.opensolutions.forecast.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A ROLE_TARIFF.
 */
@Entity
@Table(name = "role_tariff")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "role_tariff")
public class ROLE_TARIFF implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "role")
    private String role;

    @NotNull
    @Column(name = "tariff", precision=10, scale=2)
    private BigDecimal tariff;

    @NotNull
    @Column(name = "location")
    private String location;

    @NotNull
    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "last_changed_date")
    private LocalDate lastChangedDate;

    @Column(name = "last_changed_by")
    private String lastChangedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getTariff() {
        return tariff;
    }

    public void setTariff(BigDecimal tariff) {
        this.tariff = tariff;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getLastChangedDate() {
        return lastChangedDate;
    }

    public void setLastChangedDate(LocalDate lastChangedDate) {
        this.lastChangedDate = lastChangedDate;
    }

    public String getLastChangedBy() {
        return lastChangedBy;
    }

    public void setLastChangedBy(String lastChangedBy) {
        this.lastChangedBy = lastChangedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ROLE_TARIFF rOLE_TARIFF = (ROLE_TARIFF) o;
        return Objects.equals(id, rOLE_TARIFF.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ROLE_TARIFF{" +
            "id=" + id +
            ", role='" + role + "'" +
            ", tariff='" + tariff + "'" +
            ", location='" + location + "'" +
            ", effectiveDate='" + effectiveDate + "'" +
            ", expiryDate='" + expiryDate + "'" +
            ", lastChangedDate='" + lastChangedDate + "'" +
            ", lastChangedBy='" + lastChangedBy + "'" +
            '}';
    }
}
