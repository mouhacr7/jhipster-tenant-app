package com.tenant.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tenant.app.domain.enumeration.ModuleCategory;
import com.tenant.app.domain.enumeration.ModuleType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Module.
 */
@Table("module")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Column("type")
    private ModuleType type;

    @NotNull(message = "must not be null")
    @Column("category")
    private ModuleCategory category;

    @NotNull(message = "must not be null")
    @Column("active")
    private Boolean active;

    @Transient
    @JsonIgnoreProperties(value = { "module" }, allowSetters = true)
    private Set<License> licenses = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "accessRights", "client" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Module id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Module name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Module description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ModuleType getType() {
        return this.type;
    }

    public Module type(ModuleType type) {
        this.setType(type);
        return this;
    }

    public void setType(ModuleType type) {
        this.type = type;
    }

    public ModuleCategory getCategory() {
        return this.category;
    }

    public Module category(ModuleCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(ModuleCategory category) {
        this.category = category;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Module active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<License> getLicenses() {
        return this.licenses;
    }

    public void setLicenses(Set<License> licenses) {
        if (this.licenses != null) {
            this.licenses.forEach(i -> i.setModule(null));
        }
        if (licenses != null) {
            licenses.forEach(i -> i.setModule(this));
        }
        this.licenses = licenses;
    }

    public Module licenses(Set<License> licenses) {
        this.setLicenses(licenses);
        return this;
    }

    public Module addLicenses(License license) {
        this.licenses.add(license);
        license.setModule(this);
        return this;
    }

    public Module removeLicenses(License license) {
        this.licenses.remove(license);
        license.setModule(null);
        return this;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.removeAccessRights(this));
        }
        if (employees != null) {
            employees.forEach(i -> i.addAccessRights(this));
        }
        this.employees = employees;
    }

    public Module employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Module addEmployees(Employee employee) {
        this.employees.add(employee);
        employee.getAccessRights().add(this);
        return this;
    }

    public Module removeEmployees(Employee employee) {
        this.employees.remove(employee);
        employee.getAccessRights().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Module)) {
            return false;
        }
        return id != null && id.equals(((Module) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Module{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", category='" + getCategory() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
