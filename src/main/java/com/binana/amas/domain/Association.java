package com.binana.amas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Association.
 */
@Entity
@Table(name = "association")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "association")
public class Association implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "asso_id", nullable = false)
    private String assoId;

    @NotNull
    @Column(name = "asso_name", nullable = false)
    private String assoName;

    @NotNull
    @Column(name = "asso_found_date", nullable = false)
    private LocalDate assoFoundDate;

    @Lob
    @Column(name = "asso_introdution")
    private String assoIntrodution;

    @Lob
    @Column(name = "asso_image")
    private byte[] assoImage;

    @Column(name = "asso_image_content_type")
    private String assoImageContentType;

    @OneToMany(mappedBy = "assodept")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Department> departments = new HashSet<>();

    @OneToMany(mappedBy = "assoacti")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Activity> activities = new HashSet<>();

    @OneToMany(mappedBy = "assoprop")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Property> properties = new HashSet<>();

    @ManyToMany(mappedBy = "associations")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Amember> membassos = new HashSet<>();

    @ManyToOne
    private User user;

    public Association() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Association(Long id, String assoId, String assoName) {
		super();
		this.id = id;
		this.assoId = assoId;
		this.assoName = assoName;
	}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssoId() {
        return assoId;
    }

    public Association assoId(String assoId) {
        this.assoId = assoId;
        return this;
    }

    public void setAssoId(String assoId) {
        this.assoId = assoId;
    }

    public String getAssoName() {
        return assoName;
    }

    public Association assoName(String assoName) {
        this.assoName = assoName;
        return this;
    }

    public void setAssoName(String assoName) {
        this.assoName = assoName;
    }

    public LocalDate getAssoFoundDate() {
        return assoFoundDate;
    }

    public Association assoFoundDate(LocalDate assoFoundDate) {
        this.assoFoundDate = assoFoundDate;
        return this;
    }

    public void setAssoFoundDate(LocalDate assoFoundDate) {
        this.assoFoundDate = assoFoundDate;
    }

    public String getAssoIntrodution() {
        return assoIntrodution;
    }

    public Association assoIntrodution(String assoIntrodution) {
        this.assoIntrodution = assoIntrodution;
        return this;
    }

    public void setAssoIntrodution(String assoIntrodution) {
        this.assoIntrodution = assoIntrodution;
    }

    public byte[] getAssoImage() {
        return assoImage;
    }

    public Association assoImage(byte[] assoImage) {
        this.assoImage = assoImage;
        return this;
    }

    public void setAssoImage(byte[] assoImage) {
        this.assoImage = assoImage;
    }

    public String getAssoImageContentType() {
        return assoImageContentType;
    }

    public Association assoImageContentType(String assoImageContentType) {
        this.assoImageContentType = assoImageContentType;
        return this;
    }

    public void setAssoImageContentType(String assoImageContentType) {
        this.assoImageContentType = assoImageContentType;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public Association departments(Set<Department> departments) {
        this.departments = departments;
        return this;
    }

    public Association addDepartment(Department department) {
        this.departments.add(department);
        department.setAssodept(this);
        return this;
    }

    public Association removeDepartment(Department department) {
        this.departments.remove(department);
        department.setAssodept(null);
        return this;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public Association activities(Set<Activity> activities) {
        this.activities = activities;
        return this;
    }

    public Association addActivity(Activity activity) {
        this.activities.add(activity);
        activity.setAssoacti(this);
        return this;
    }

    public Association removeActivity(Activity activity) {
        this.activities.remove(activity);
        activity.setAssoacti(null);
        return this;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public Association properties(Set<Property> properties) {
        this.properties = properties;
        return this;
    }

    public Association addProperty(Property property) {
        this.properties.add(property);
        property.setAssoprop(this);
        return this;
    }

    public Association removeProperty(Property property) {
        this.properties.remove(property);
        property.setAssoprop(null);
        return this;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

    public Set<Amember> getMembassos() {
        return membassos;
    }

    public Association membassos(Set<Amember> amembers) {
        this.membassos = amembers;
        return this;
    }

    public Association addMembasso(Amember amember) {
        this.membassos.add(amember);
        amember.getAssociations().add(this);
        return this;
    }

    public Association removeMembasso(Amember amember) {
        this.membassos.remove(amember);
        amember.getAssociations().remove(this);
        return this;
    }

    public void setMembassos(Set<Amember> amembers) {
        this.membassos = amembers;
    }

    public User getUser() {
        return user;
    }

    public Association user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Association association = (Association) o;
        if (association.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, association.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Association{" +
            "id=" + id +
            ", assoId='" + assoId + "'" +
            ", assoName='" + assoName + "'" +
            ", assoFoundDate='" + assoFoundDate + "'" +
            ", assoIntrodution='" + assoIntrodution + "'" +
            ", assoImage='" + assoImage + "'" +
            ", assoImageContentType='" + assoImageContentType + "'" +
            '}';
    }
}
