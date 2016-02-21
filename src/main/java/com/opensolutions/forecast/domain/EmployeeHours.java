package com.opensolutions.forecast.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A EmployeeHours.
 */
@Entity
@Table(name = "employee_hours")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employeehours")
public class EmployeeHours implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "holidays")
    private String holidays;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "forecast_date")
    private LocalDate forecastDate;

    @Column(name = "last_changed_date")
    private LocalDate lastChangedDate;

    @Column(name = "last_changed_by")
    private String lastChangedBy;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHolidays() {
        return holidays;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(LocalDate forecastDate) {
        this.forecastDate = forecastDate;
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
        EmployeeHours employeeHours = (EmployeeHours) o;
        return Objects.equals(id, employeeHours.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EmployeeHours{" +
            "id=" + id +
            ", holidays='" + holidays + "'" +
            ", createdDate='" + createdDate + "'" +
            ", forecastDate='" + forecastDate + "'" +
            ", lastChangedDate='" + lastChangedDate + "'" +
            ", lastChangedBy='" + lastChangedBy + "'" +
            '}';
    }
}
