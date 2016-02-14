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
 * A EmployeeHolidays.
 */
@Entity
@Table(name = "employee_holidays")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employeeholidays")
public class EmployeeHolidays implements Serializable {

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

    @Column(name = "last_changed_date")
    private LocalDate lastChangedDate;

    @Column(name = "last_changed_by")
    private String lastChangedBy;

    @ManyToOne
    @JoinColumn(name = "employee_billing_hours_id")
    private EmployeeBillingHours employeeBillingHours;

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

    public EmployeeBillingHours getEmployeeBillingHours() {
        return employeeBillingHours;
    }

    public void setEmployeeBillingHours(EmployeeBillingHours employeeBillingHours) {
        this.employeeBillingHours = employeeBillingHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmployeeHolidays employeeHolidays = (EmployeeHolidays) o;
        return Objects.equals(id, employeeHolidays.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EmployeeHolidays{" +
            "id=" + id +
            ", week1='" + week1 + "'" +
            ", week2='" + week2 + "'" +
            ", week3='" + week3 + "'" +
            ", week4='" + week4 + "'" +
            ", week5='" + week5 + "'" +
            ", lastChangedDate='" + lastChangedDate + "'" +
            ", lastChangedBy='" + lastChangedBy + "'" +
            '}';
    }
}
