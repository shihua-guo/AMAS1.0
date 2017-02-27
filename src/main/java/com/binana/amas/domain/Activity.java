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
 * A Activity.
 */
@Entity
@Table(name = "activity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "activity")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "acti_name", nullable = false)
    private String actiName;

    @NotNull
    @Column(name = "acti_date", nullable = false)
    private LocalDate actiDate;

    @NotNull
    @Column(name = "acti_place", nullable = false)
    private String actiPlace;

    @Column(name = "acti_content")
    private String actiContent;

    @ManyToOne
    private Association assoacti;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActiName() {
        return actiName;
    }

    public Activity actiName(String actiName) {
        this.actiName = actiName;
        return this;
    }

    public void setActiName(String actiName) {
        this.actiName = actiName;
    }

    public LocalDate getActiDate() {
        return actiDate;
    }

    public Activity actiDate(LocalDate actiDate) {
        this.actiDate = actiDate;
        return this;
    }

    public void setActiDate(LocalDate actiDate) {
        this.actiDate = actiDate;
    }

    public String getActiPlace() {
        return actiPlace;
    }

    public Activity actiPlace(String actiPlace) {
        this.actiPlace = actiPlace;
        return this;
    }

    public void setActiPlace(String actiPlace) {
        this.actiPlace = actiPlace;
    }

    public String getActiContent() {
        return actiContent;
    }

    public Activity actiContent(String actiContent) {
        this.actiContent = actiContent;
        return this;
    }

    public void setActiContent(String actiContent) {
        this.actiContent = actiContent;
    }

    public Association getAssoacti() {
        return assoacti;
    }

    public Activity assoacti(Association association) {
        this.assoacti = association;
        return this;
    }

    public void setAssoacti(Association association) {
        this.assoacti = association;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Activity activity = (Activity) o;
        if (activity.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, activity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Activity{" +
            "id=" + id +
            ", actiName='" + actiName + "'" +
            ", actiDate='" + actiDate + "'" +
            ", actiPlace='" + actiPlace + "'" +
            ", actiContent='" + actiContent + "'" +
            '}';
    }
}
