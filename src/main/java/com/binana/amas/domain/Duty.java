package com.binana.amas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.binana.amas.domain.enumeration.Weekday;

/**
 * A Duty.
 */
@Entity
@Table(name = "duty")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "duty")
public class Duty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "weekday")
    private Weekday weekday;

    @Lob
    @Column(name = "duty_introdution")
    private String dutyIntrodution;

    @OneToOne
    @JoinColumn(unique = true)
    private Department department;

    @OneToMany(mappedBy = "dutymemb")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Amember> amembers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public Duty weekday(Weekday weekday) {
        this.weekday = weekday;
        return this;
    }

    public void setWeekday(Weekday weekday) {
        this.weekday = weekday;
    }

    public String getDutyIntrodution() {
        return dutyIntrodution;
    }

    public Duty dutyIntrodution(String dutyIntrodution) {
        this.dutyIntrodution = dutyIntrodution;
        return this;
    }

    public void setDutyIntrodution(String dutyIntrodution) {
        this.dutyIntrodution = dutyIntrodution;
    }

    public Department getDepartment() {
        return department;
    }

    public Duty department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Amember> getAmembers() {
        return amembers;
    }

    public Duty amembers(Set<Amember> amembers) {
        this.amembers = amembers;
        return this;
    }

    public Duty addAmember(Amember amember) {
        this.amembers.add(amember);
        amember.setDutymemb(this);
        return this;
    }

    public Duty removeAmember(Amember amember) {
        this.amembers.remove(amember);
        amember.setDutymemb(null);
        return this;
    }

    public void setAmembers(Set<Amember> amembers) {
        this.amembers = amembers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Duty duty = (Duty) o;
        if (duty.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, duty.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Duty{" +
            "id=" + id +
            ", weekday='" + weekday + "'" +
            ", dutyIntrodution='" + dutyIntrodution + "'" +
            '}';
    }
}
