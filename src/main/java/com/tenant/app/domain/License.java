package com.tenant.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A License.
 */
@Table("license")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class License implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("start_date")
    private Instant startDate;

    @NotNull(message = "must not be null")
    @Column("end_date")
    private Instant endDate;

    @NotNull(message = "must not be null")
    @Column("active")
    private Boolean active;

    @Transient
    @JsonIgnoreProperties(value = { "licenses", "employees" }, allowSetters = true)
    private Module module;

    @Column("module_id")
    private Long moduleId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public License id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public License startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public License endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return this.active;
    }

    public License active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Module getModule() {
        return this.module;
    }

    public void setModule(Module module) {
        this.module = module;
        this.moduleId = module != null ? module.getId() : null;
    }

    public License module(Module module) {
        this.setModule(module);
        return this;
    }

    public Long getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(Long module) {
        this.moduleId = module;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof License)) {
            return false;
        }
        return id != null && id.equals(((License) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "License{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
