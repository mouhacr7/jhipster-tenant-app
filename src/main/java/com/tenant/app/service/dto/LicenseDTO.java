package com.tenant.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tenant.app.domain.License} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LicenseDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Instant startDate;

    @NotNull(message = "must not be null")
    private Instant endDate;

    @NotNull(message = "must not be null")
    private Boolean active;

    private ModuleDTO module;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ModuleDTO getModule() {
        return module;
    }

    public void setModule(ModuleDTO module) {
        this.module = module;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LicenseDTO)) {
            return false;
        }

        LicenseDTO licenseDTO = (LicenseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, licenseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LicenseDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", active='" + getActive() + "'" +
            ", module=" + getModule() +
            "}";
    }
}
