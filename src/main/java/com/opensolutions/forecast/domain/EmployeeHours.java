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

    @Column(name = "week1")
    private Integer week1;

    @Column(name = "week2")
    private Integer week2;

    @Column(name = "week3")
    private Integer week3;

    @Column(name = "week4")
    private Integer week4;

    @Column(name = "week5")
    private Integer week5;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "forecast_date")
    private LocalDate forecastDate;

    @Column(name = "type")
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
            ", week1='" + week1 + "'" +
            ", week2='" + week2 + "'" +
            ", week3='" + week3 + "'" +
            ", week4='" + week4 + "'" +
            ", week5='" + week5 + "'" +
            ", createdDate='" + createdDate + "'" +
            ", forecastDate='" + forecastDate + "'" +
            ", type='" + type + "'" +
            ", lastChangedDate='" + lastChangedDate + "'" +
            ", lastChangedBy='" + lastChangedBy + "'" +
            '}';
    }
}
