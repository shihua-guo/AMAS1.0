package com.binana.amas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Department.
 */
@Entity
@Table(name = "department")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "dept_name", nullable = false)
    private String deptName;

    @Lob
    @Column(name = "dept_introdution")
    private String deptIntrodution;

    @Lob
    @Column(name = "dept_image")
    private byte[] deptImage;

    @Column(name = "dept_image_content_type")
    private String deptImageContentType;

    @OneToOne(mappedBy = "department")
    @JsonIgnore
    private Duty duty;

    @ManyToOne
    private Association assodept;

    @ManyToMany(mappedBy = "departments")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Amember> membdepts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public Department deptName(String deptName) {
        this.deptName = deptName;
        return this;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptIntrodution() {
        return deptIntrodution;
    }

    public Department deptIntrodution(String deptIntrodution) {
        this.deptIntrodution = deptIntrodution;
        return this;
    }

    public void setDeptIntrodution(String deptIntrodution) {
        this.deptIntrodution = deptIntrodution;
    }

    public byte[] getDeptImage() {
        return deptImage;
    }

    public Department deptImage(byte[] deptImage) {
        this.deptImage = deptImage;
        return this;
    }

    public void setDeptImage(byte[] deptImage) {
        this.deptImage = deptImage;
    }

    public String getDeptImageContentType() {
        return deptImageContentType;
    }

    public Department deptImageContentType(String deptImageContentType) {
        this.deptImageContentType = deptImageContentType;
        return this;
    }

    public void setDeptImageContentType(String deptImageContentType) {
        this.deptImageContentType = deptImageContentType;
    }

    public Duty getDuty() {
        return duty;
    }

    public Department duty(Duty duty) {
        this.duty = duty;
        return this;
    }

    public void setDuty(Duty duty) {
        this.duty = duty;
    }

    public Association getAssodept() {
        return assodept;
    }

    public Department assodept(Association association) {
        this.assodept = association;
        return this;
    }

    public void setAssodept(Association association) {
        this.assodept = association;
    }

    public Set<Amember> getMembdepts() {
        return membdepts;
    }

    public Department membdepts(Set<Amember> amembers) {
        this.membdepts = amembers;
        return this;
    }

    public Department addMembdept(Amember amember) {
        this.membdepts.add(amember);
        amember.getDepartments().add(this);
        return this;
    }

    public Department removeMembdept(Amember amember) {
        this.membdepts.remove(amember);
        amember.getDepartments().remove(this);
        return this;
    }

    public void setMembdepts(Set<Amember> amembers) {
        this.membdepts = amembers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Department department = (Department) o;
        if (department.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, department.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Department{" +
            "id=" + id +
            ", deptName='" + deptName + "'" +
            ", deptIntrodution='" + deptIntrodution + "'" +
            ", deptImage='" + deptImage + "'" +
            ", deptImageContentType='" + deptImageContentType + "'" +
            '}';
    }
}
