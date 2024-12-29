package com.tenant.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccessRightMapperTest {

    private AccessRightMapper accessRightMapper;

    @BeforeEach
    public void setUp() {
        accessRightMapper = new AccessRightMapperImpl();
    }
}
