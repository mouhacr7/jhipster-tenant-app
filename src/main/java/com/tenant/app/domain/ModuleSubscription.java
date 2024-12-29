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
 * A ModuleSubscription.
 */
@Table("module_subscription")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModuleSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("subscription_date")
    private Instant subscriptionDate;

    @NotNull(message = "must not be null")
    @Column("active")
    private Boolean active;

    @Transient
    @JsonIgnoreProperties(value = { "licenses", "employees" }, allowSetters = true)
    private Module module;

    @Transient
    @JsonIgnoreProperties(value = { "employees", "moduleSubscriptions" }, allowSetters = true)
    private Client client;

    @Column("module_id")
    private Long moduleId;

    @Column("client_id")
    private Long clientId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ModuleSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSubscriptionDate() {
        return this.subscriptionDate;
    }

    public ModuleSubscription subscriptionDate(Instant subscriptionDate) {
        this.setSubscriptionDate(subscriptionDate);
        return this;
    }

    public void setSubscriptionDate(Instant subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public Boolean getActive() {
        return this.active;
    }

    public ModuleSubscription active(Boolean active) {
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

    public ModuleSubscription module(Module module) {
        this.setModule(module);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
        this.clientId = client != null ? client.getId() : null;
    }

    public ModuleSubscription client(Client client) {
        this.setClient(client);
        return this;
    }

    public Long getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(Long module) {
        this.moduleId = module;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long client) {
        this.clientId = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModuleSubscription)) {
            return false;
        }
        return id != null && id.equals(((ModuleSubscription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModuleSubscription{" +
            "id=" + getId() +
            ", subscriptionDate='" + getSubscriptionDate() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
