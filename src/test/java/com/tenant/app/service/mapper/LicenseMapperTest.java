package com.tenant.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LicenseMapperTest {

    private LicenseMapper licenseMapper;

    @BeforeEach
    public void setUp() {
        licenseMapper = new LicenseMapperImpl();
    }
}
