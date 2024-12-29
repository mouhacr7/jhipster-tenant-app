package com.tenant.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tenant.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LicenseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LicenseDTO.class);
        LicenseDTO licenseDTO1 = new LicenseDTO();
        licenseDTO1.setId(1L);
        LicenseDTO licenseDTO2 = new LicenseDTO();
        assertThat(licenseDTO1).isNotEqualTo(licenseDTO2);
        licenseDTO2.setId(licenseDTO1.getId());
        assertThat(licenseDTO1).isEqualTo(licenseDTO2);
        licenseDTO2.setId(2L);
        assertThat(licenseDTO1).isNotEqualTo(licenseDTO2);
        licenseDTO1.setId(null);
        assertThat(licenseDTO1).isNotEqualTo(licenseDTO2);
    }
}
