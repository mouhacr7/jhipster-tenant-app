package com.tenant.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tenant.app.domain.AccessRight} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccessRightDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Boolean canRead;

    @NotNull(message = "must not be null")
    private Boolean canWrite;

    @NotNull(message = "must not be null")
    private Boolean canDelete;

    private ModuleDTO module;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCanRead() {
        return canRead;
    }

    public void setCanRead(Boolean canRead) {
        this.canRead = canRead;
    }

    public Boolean getCanWrite() {
        return canWrite;
    }

    public void setCanWrite(Boolean canWrite) {
        this.canWrite = canWrite;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public ModuleDTO getModule() {
        return module;
    }

    public void setModule(ModuleDTO module) {
        this.module = module;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccessRightDTO)) {
            return false;
        }

        AccessRightDTO accessRightDTO = (AccessRightDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accessRightDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccessRightDTO{" +
            "id=" + getId() +
            ", canRead='" + getCanRead() + "'" +
            ", canWrite='" + getCanWrite() + "'" +
            ", canDelete='" + getCanDelete() + "'" +
            ", module=" + getModule() +
            ", employee=" + getEmployee() +
            "}";
    }
}
