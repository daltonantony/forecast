package com.opensolutions.forecast.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The Class EmployeeForecast.
 */
public class EmployeeForecast {

    private Long id;
    private Long associateId;
    private String name;
    private String domain;
    private String project;
    private Map<LocalDate, List<DaysOfMonth>> employeeHours;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Gets the associate id.
     *
     * @return the associate id
     */
    public Long getAssociateId() {
        return associateId;
    }

    /**
     * Sets the associate id.
     *
     * @param associateId the new associate id
     */
    public void setAssociateId(final Long associateId) {
        this.associateId = associateId;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the domain.
     *
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets the domain.
     *
     * @param domain the new domain
     */
    public void setDomain(final String domain) {
        this.domain = domain;
    }

    /**
     * Gets the project.
     *
     * @return the project
     */
    public String getProject() {
        return project;
    }

    /**
     * Sets the project.
     *
     * @param project the new project
     */
    public void setProject(final String project) {
        this.project = project;
    }

    /**
     * Gets the employee hours.
     *
     * @return the employee hours
     */
    public Map<LocalDate, List<DaysOfMonth>> getEmployeeHours() {
        return employeeHours;
    }

    /**
     * Sets the employee hours.
     *
     * @param employeeHours the employee hours
     */
    public void setEmployeeHours(final Map<LocalDate, List<DaysOfMonth>> employeeHours) {
        this.employeeHours = employeeHours;
    }

}
