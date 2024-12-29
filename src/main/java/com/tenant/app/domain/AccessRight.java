package com.tenant.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AccessRight.
 */
@Table("access_right")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccessRight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("can_read")
    private Boolean canRead;

    @NotNull(message = "must not be null")
    @Column("can_write")
    private Boolean canWrite;

    @NotNull(message = "must not be null")
    @Column("can_delete")
    private Boolean canDelete;

    @Transient
    @JsonIgnoreProperties(value = { "licenses", "employees" }, allowSetters = true)
    private Module module;

    @Transient
    @JsonIgnoreProperties(value = { "accessRights", "client" }, allowSetters = true)
    private Employee employee;

    @Column("module_id")
    private Long moduleId;

    @Column("employee_id")
    private Long employeeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccessRight id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCanRead() {
        return this.canRead;
    }

    public AccessRight canRead(Boolean canRead) {
        this.setCanRead(canRead);
        return this;
    }

    public void setCanRead(Boolean canRead) {
        this.canRead = canRead;
    }

    public Boolean getCanWrite() {
        return this.canWrite;
    }

    public AccessRight canWrite(Boolean canWrite) {
        this.setCanWrite(canWrite);
        return this;
    }

    public void setCanWrite(Boolean canWrite) {
        this.canWrite = canWrite;
    }

    public Boolean getCanDelete() {
        return this.canDelete;
    }

    public AccessRight canDelete(Boolean canDelete) {
        this.setCanDelete(canDelete);
        return this;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public Module getModule() {
        return this.module;
    }

    public void setModule(Module module) {
        this.module = module;
        this.moduleId = module != null ? module.getId() : null;
    }

    public AccessRight module(Module module) {
        this.setModule(module);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.employeeId = employee != null ? employee.getId() : null;
    }

    public AccessRight employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Long getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(Long module) {
        this.moduleId = module;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employee) {
        this.employeeId = employee;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccessRight)) {
            return false;
        }
        return id != null && id.equals(((AccessRight) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccessRight{" +
            "id=" + getId() +
            ", canRead='" + getCanRead() + "'" +
            ", canWrite='" + getCanWrite() + "'" +
            ", canDelete='" + getCanDelete() + "'" +
            "}";
    }
}
