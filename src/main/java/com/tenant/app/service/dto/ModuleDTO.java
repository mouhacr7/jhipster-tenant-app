package com.tenant.app.service.dto;

import com.tenant.app.domain.enumeration.ModuleCategory;
import com.tenant.app.domain.enumeration.ModuleType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tenant.app.domain.Module} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModuleDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String description;

    @NotNull(message = "must not be null")
    private ModuleType type;

    @NotNull(message = "must not be null")
    private ModuleCategory category;

    @NotNull(message = "must not be null")
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ModuleType getType() {
        return type;
    }

    public void setType(ModuleType type) {
        this.type = type;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public void setCategory(ModuleCategory category) {
        this.category = category;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModuleDTO)) {
            return false;
        }

        ModuleDTO moduleDTO = (ModuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moduleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModuleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", category='" + getCategory() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
