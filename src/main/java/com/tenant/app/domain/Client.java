package com.tenant.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Client.
 */
@Table("client")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("email")
    private String email;

    @Column("phone")
    private String phone;

    @Column("address")
    private String address;

    @Transient
    @JsonIgnoreProperties(value = { "accessRights", "client" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "module", "client" }, allowSetters = true)
    private Set<ModuleSubscription> moduleSubscriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Client name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Client email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Client phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public Client address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setClient(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setClient(this));
        }
        this.employees = employees;
    }

    public Client employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Client addEmployees(Employee employee) {
        this.employees.add(employee);
        employee.setClient(this);
        return this;
    }

    public Client removeEmployees(Employee employee) {
        this.employees.remove(employee);
        employee.setClient(null);
        return this;
    }

    public Set<ModuleSubscription> getModuleSubscriptions() {
        return this.moduleSubscriptions;
    }

    public void setModuleSubscriptions(Set<ModuleSubscription> moduleSubscriptions) {
        if (this.moduleSubscriptions != null) {
            this.moduleSubscriptions.forEach(i -> i.setClient(null));
        }
        if (moduleSubscriptions != null) {
            moduleSubscriptions.forEach(i -> i.setClient(this));
        }
        this.moduleSubscriptions = moduleSubscriptions;
    }

    public Client moduleSubscriptions(Set<ModuleSubscription> moduleSubscriptions) {
        this.setModuleSubscriptions(moduleSubscriptions);
        return this;
    }

    public Client addModuleSubscriptions(ModuleSubscription moduleSubscription) {
        this.moduleSubscriptions.add(moduleSubscription);
        moduleSubscription.setClient(this);
        return this;
    }

    public Client removeModuleSubscriptions(ModuleSubscription moduleSubscription) {
        this.moduleSubscriptions.remove(moduleSubscription);
        moduleSubscription.setClient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
