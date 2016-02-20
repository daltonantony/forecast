package com.opensolutions.forecast.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A EmployeeBillingHours.
 */
@Entity
@Table(name = "employee_billing_hours")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employeebillinghours")
public class EmployeeBillingHours implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "week1")
    private Integer week1;

    @NotNull
    @Column(name = "week2")
    private Integer week2;

    @NotNull
    @Column(name = "week3")
    private Integer week3;

    @NotNull
    @Column(name = "week4")
    private Integer week4;

    @NotNull
    @Column(name = "week5")
    private Integer week5;

    @NotNull
    @Column(name = "created_date")
    private LocalDate createdDate;

    @NotNull
    @Column(name = "forecast_date")
    private LocalDate forecastDate;

    @Column(name = "last_changed_date")
    private LocalDate lastChangedDate;

    @Column(name = "last_changed_by")
    private String lastChangedBy;

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

    public Integer getWeek1() {
        return week1;
    }

    public void setWeek1(Integer week1) {
        this.week1 = week1;
    }

    public Integer getWeek2() {
        return week2;
    }

    public void setWeek2(Integer week2) {
        this.week2 = week2;
    }

    public Integer getWeek3() {
        return week3;
    }

    public void setWeek3(Integer week3) {
        this.week3 = week3;
    }

    public Integer getWeek4() {
        return week4;
    }

    public void setWeek4(Integer week4) {
        this.week4 = week4;
    }

    public Integer getWeek5() {
        return week5;
    }

    public void setWeek5(Integer week5) {
        this.week5 = week5;
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
        EmployeeBillingHours employeeBillingHours = (EmployeeBillingHours) o;
        return Objects.equals(id, employeeBillingHours.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EmployeeBillingHours{" +
            "id=" + id +
            ", week1='" + week1 + "'" +
            ", week2='" + week2 + "'" +
            ", week3='" + week3 + "'" +
            ", week4='" + week4 + "'" +
            ", week5='" + week5 + "'" +
            ", createdDate='" + createdDate + "'" +
            ", forecastDate='" + forecastDate + "'" +
            ", lastChangedDate='" + lastChangedDate + "'" +
            ", lastChangedBy='" + lastChangedBy + "'" +
            '}';
    }
}
