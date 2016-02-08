package com.opensolutions.forecast.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A EmployeeAllocation.
 */
@Entity
@Table(name = "employee_allocation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employeeallocation")
public class EmployeeAllocation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "project")
    private String project;

    @NotNull
    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name = "location")
    private String location;

    @NotNull
    @Column(name = "allocation")
    private Integer allocation;

    @Column(name = "last_changed_date")
    private LocalDate lastChangedDate;

    @Column(name = "last_changedby")
    private String lastChangedby;

    @NotNull
    @Column(name = "role")
    private String role;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getAllocation() {
        return allocation;
    }

    public void setAllocation(Integer allocation) {
        this.allocation = allocation;
    }

    public LocalDate getLastChangedDate() {
        return lastChangedDate;
    }

    public void setLastChangedDate(LocalDate lastChangedDate) {
        this.lastChangedDate = lastChangedDate;
    }

    public String getLastChangedby() {
        return lastChangedby;
    }

    public void setLastChangedby(String lastChangedby) {
        this.lastChangedby = lastChangedby;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmployeeAllocation employeeAllocation = (EmployeeAllocation) o;
        return Objects.equals(id, employeeAllocation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EmployeeAllocation{" +
            "id=" + id +
            ", project='" + project + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", location='" + location + "'" +
            ", allocation='" + allocation + "'" +
            ", lastChangedDate='" + lastChangedDate + "'" +
            ", lastChangedby='" + lastChangedby + "'" +
            ", role='" + role + "'" +
            '}';
    }
}
