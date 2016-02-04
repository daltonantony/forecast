package com.opensolutions.forecast.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employee")
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "associate_id")
    private Long associateId;

    @Column(name = "rabo_id")
    private String raboId;

    @Column(name = "domain")
    private String domain;

    //@JoinFormula("select * from code_values where code_type='domain'")
    @Transient
    private List<CodeValues> domains;

    @Column(name = "last_changed_date")
    private LocalDate lastChangedDate;

    @Column(name = "last_changed_by")
    private String lastChangedBy;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EmployeeAllocation> employeeAllocations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAssociateId() {
        return associateId;
    }

    public void setAssociateId(Long associateId) {
        this.associateId = associateId;
    }

    public String getRaboId() {
        return raboId;
    }

    public void setRaboId(String raboId) {
        this.raboId = raboId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

    public Set<EmployeeAllocation> getEmployeeAllocations() {
        return employeeAllocations;
    }

    public void setEmployeeAllocations(Set<EmployeeAllocation> employeeAllocations) {
        this.employeeAllocations = employeeAllocations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", associateId='" + associateId + "'" +
            ", raboId='" + raboId + "'" +
            ", domain='" + domain + "'" +
            ", lastChangedDate='" + lastChangedDate + "'" +
            ", lastChangedBy='" + lastChangedBy + "'" +
            '}';
    }

    public List<CodeValues> getDomains() {
        return domains;
    }

    public void setDomains(List<CodeValues> domains) {
        this.domains = domains;
    }
}
