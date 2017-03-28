package com.binana.amas.domain;

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

import com.binana.amas.domain.enumeration.GENDER;

import com.binana.amas.domain.enumeration.POLITICSSTATUS;

import com.binana.amas.domain.enumeration.College;

/**
 * A Amember.
 */
@Entity
@Table(name = "amember")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "amember")
public class Amember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "memb_name", nullable = false)
    private String membName;

    @NotNull
    @Column(name = "memb_no", nullable = false)
    private String membNO;

    @Column(name = "memb_class")
    private String membClass;

    @NotNull
    @Column(name = "memb_phone", nullable = false)
    private String membPhone;

    @NotNull
    @Column(name = "memb_qq", nullable = false)
    private String membQQ;

    @Column(name = "memb_email")
    private String membEmail;

    @Column(name = "memb_join_date")
    private LocalDate membJoinDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GENDER gender;

    @Column(name = "dorm_num")
    private String dormNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "politics_status")
    private POLITICSSTATUS politicsStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "college")
    private College college;

    @Column(name = "major")
    private String major;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "amember_association",
               joinColumns = @JoinColumn(name="amembers_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="associations_id", referencedColumnName="id"))
    private Set<Association> associations = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "amember_department",
               joinColumns = @JoinColumn(name="amembers_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="departments_id", referencedColumnName="id"))
    private Set<Department> departments = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "amember_role",
               joinColumns = @JoinColumn(name="amembers_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="roles_id", referencedColumnName="id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToOne
    private Duty dutymemb;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMembName() {
        return membName;
    }

    public Amember membName(String membName) {
        this.membName = membName;
        return this;
    }

    public void setMembName(String membName) {
        this.membName = membName;
    }

    public String getMembNO() {
        return membNO;
    }

    public Amember membNO(String membNO) {
        this.membNO = membNO;
        return this;
    }

    public void setMembNO(String membNO) {
        this.membNO = membNO;
    }

    public String getMembClass() {
        return membClass;
    }

    public Amember membClass(String membClass) {
        this.membClass = membClass;
        return this;
    }

    public void setMembClass(String membClass) {
        this.membClass = membClass;
    }

    public String getMembPhone() {
        return membPhone;
    }

    public Amember membPhone(String membPhone) {
        this.membPhone = membPhone;
        return this;
    }

    public void setMembPhone(String membPhone) {
        this.membPhone = membPhone;
    }

    public String getMembQQ() {
        return membQQ;
    }

    public Amember membQQ(String membQQ) {
        this.membQQ = membQQ;
        return this;
    }

    public void setMembQQ(String membQQ) {
        this.membQQ = membQQ;
    }

    public String getMembEmail() {
        return membEmail;
    }

    public Amember membEmail(String membEmail) {
        this.membEmail = membEmail;
        return this;
    }

    public void setMembEmail(String membEmail) {
        this.membEmail = membEmail;
    }

    public LocalDate getMembJoinDate() {
        return membJoinDate;
    }

    public Amember membJoinDate(LocalDate membJoinDate) {
        this.membJoinDate = membJoinDate;
        return this;
    }

    public void setMembJoinDate(LocalDate membJoinDate) {
        this.membJoinDate = membJoinDate;
    }

    public GENDER getGender() {
        return gender;
    }

    public Amember gender(GENDER gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public String getDormNum() {
        return dormNum;
    }

    public Amember dormNum(String dormNum) {
        this.dormNum = dormNum;
        return this;
    }

    public void setDormNum(String dormNum) {
        this.dormNum = dormNum;
    }

    public POLITICSSTATUS getPoliticsStatus() {
        return politicsStatus;
    }

    public Amember politicsStatus(POLITICSSTATUS politicsStatus) {
        this.politicsStatus = politicsStatus;
        return this;
    }

    public void setPoliticsStatus(POLITICSSTATUS politicsStatus) {
        this.politicsStatus = politicsStatus;
    }

    public College getCollege() {
        return college;
    }

    public Amember college(College college) {
        this.college = college;
        return this;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public Amember major(String major) {
        this.major = major;
        return this;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Set<Association> getAssociations() {
        return associations;
    }

    public Amember associations(Set<Association> associations) {
        this.associations = associations;
        return this;
    }

    public Amember addAssociation(Association association) {
        this.associations.add(association);
        association.getMembassos().add(this);
        return this;
    }

    public Amember removeAssociation(Association association) {
        this.associations.remove(association);
        association.getMembassos().remove(this);
        return this;
    }

    public void setAssociations(Set<Association> associations) {
        this.associations = associations;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public Amember departments(Set<Department> departments) {
        this.departments = departments;
        return this;
    }

    public Amember addDepartment(Department department) {
        this.departments.add(department);
        department.getMembdepts().add(this);
        return this;
    }

    public Amember removeDepartment(Department department) {
        this.departments.remove(department);
        department.getMembdepts().remove(this);
        return this;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Amember roles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Amember addRole(Role role) {
        this.roles.add(role);
        role.getMembroles().add(this);
        return this;
    }

    public Amember removeRole(Role role) {
        this.roles.remove(role);
        role.getMembroles().remove(this);
        return this;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Duty getDutymemb() {
        return dutymemb;
    }

    public Amember dutymemb(Duty duty) {
        this.dutymemb = duty;
        return this;
    }

    public void setDutymemb(Duty duty) {
        this.dutymemb = duty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Amember amember = (Amember) o;
        if (amember.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, amember.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Amember{" +
            "id=" + id +
            ", membName='" + membName + "'" +
            ", membNO='" + membNO + "'" +
            ", membClass='" + membClass + "'" +
            ", membPhone='" + membPhone + "'" +
            ", membQQ='" + membQQ + "'" +
            ", membEmail='" + membEmail + "'" +
            ", membJoinDate='" + membJoinDate + "'" +
            ", gender='" + gender + "'" +
            ", dormNum='" + dormNum + "'" +
            ", politicsStatus='" + politicsStatus + "'" +
            ", college='" + college + "'" +
            ", major='" + major + "'" +
            '}';
    }
}
