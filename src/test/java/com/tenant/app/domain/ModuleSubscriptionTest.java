package com.tenant.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tenant.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModuleSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModuleSubscription.class);
        ModuleSubscription moduleSubscription1 = new ModuleSubscription();
        moduleSubscription1.setId(1L);
        ModuleSubscription moduleSubscription2 = new ModuleSubscription();
        moduleSubscription2.setId(moduleSubscription1.getId());
        assertThat(moduleSubscription1).isEqualTo(moduleSubscription2);
        moduleSubscription2.setId(2L);
        assertThat(moduleSubscription1).isNotEqualTo(moduleSubscription2);
        moduleSubscription1.setId(null);
        assertThat(moduleSubscription1).isNotEqualTo(moduleSubscription2);
    }
}
