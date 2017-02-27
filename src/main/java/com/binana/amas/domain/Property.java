package com.binana.amas.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Property.
 */
@Entity
@Table(name = "property")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "property")
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "prop_name", nullable = false)
    private String propName;

    @Column(name = "prop_price")
    private Double propPrice;

    @Column(name = "prop_buy_date")
    private LocalDate propBuyDate;

    @Column(name = "prop_number")
    private Long propNumber;

    @ManyToOne
    private Association assoprop;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropName() {
        return propName;
    }

    public Property propName(String propName) {
        this.propName = propName;
        return this;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public Double getPropPrice() {
        return propPrice;
    }

    public Property propPrice(Double propPrice) {
        this.propPrice = propPrice;
        return this;
    }

    public void setPropPrice(Double propPrice) {
        this.propPrice = propPrice;
    }

    public LocalDate getPropBuyDate() {
        return propBuyDate;
    }

    public Property propBuyDate(LocalDate propBuyDate) {
        this.propBuyDate = propBuyDate;
        return this;
    }

    public void setPropBuyDate(LocalDate propBuyDate) {
        this.propBuyDate = propBuyDate;
    }

    public Long getPropNumber() {
        return propNumber;
    }

    public Property propNumber(Long propNumber) {
        this.propNumber = propNumber;
        return this;
    }

    public void setPropNumber(Long propNumber) {
        this.propNumber = propNumber;
    }

    public Association getAssoprop() {
        return assoprop;
    }

    public Property assoprop(Association association) {
        this.assoprop = association;
        return this;
    }

    public void setAssoprop(Association association) {
        this.assoprop = association;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Property property = (Property) o;
        if (property.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, property.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Property{" +
            "id=" + id +
            ", propName='" + propName + "'" +
            ", propPrice='" + propPrice + "'" +
            ", propBuyDate='" + propBuyDate + "'" +
            ", propNumber='" + propNumber + "'" +
            '}';
    }
}
