package com.tenant.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tenant.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModuleSubscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModuleSubscriptionDTO.class);
        ModuleSubscriptionDTO moduleSubscriptionDTO1 = new ModuleSubscriptionDTO();
        moduleSubscriptionDTO1.setId(1L);
        ModuleSubscriptionDTO moduleSubscriptionDTO2 = new ModuleSubscriptionDTO();
        assertThat(moduleSubscriptionDTO1).isNotEqualTo(moduleSubscriptionDTO2);
        moduleSubscriptionDTO2.setId(moduleSubscriptionDTO1.getId());
        assertThat(moduleSubscriptionDTO1).isEqualTo(moduleSubscriptionDTO2);
        moduleSubscriptionDTO2.setId(2L);
        assertThat(moduleSubscriptionDTO1).isNotEqualTo(moduleSubscriptionDTO2);
        moduleSubscriptionDTO1.setId(null);
        assertThat(moduleSubscriptionDTO1).isNotEqualTo(moduleSubscriptionDTO2);
    }
}
