package com.tenant.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tenant.app.domain.ModuleSubscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModuleSubscriptionDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Instant subscriptionDate;

    @NotNull(message = "must not be null")
    private Boolean active;

    private ModuleDTO module;

    private ClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Instant subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
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

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModuleSubscriptionDTO)) {
            return false;
        }

        ModuleSubscriptionDTO moduleSubscriptionDTO = (ModuleSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moduleSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModuleSubscriptionDTO{" +
            "id=" + getId() +
            ", subscriptionDate='" + getSubscriptionDate() + "'" +
            ", active='" + getActive() + "'" +
            ", module=" + getModule() +
            ", client=" + getClient() +
            "}";
    }
}
