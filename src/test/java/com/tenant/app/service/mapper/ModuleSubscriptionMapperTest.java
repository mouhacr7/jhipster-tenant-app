package com.tenant.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModuleSubscriptionMapperTest {

    private ModuleSubscriptionMapper moduleSubscriptionMapper;

    @BeforeEach
    public void setUp() {
        moduleSubscriptionMapper = new ModuleSubscriptionMapperImpl();
    }
}
