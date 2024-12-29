package com.tenant.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tenant.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccessRightTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccessRight.class);
        AccessRight accessRight1 = new AccessRight();
        accessRight1.setId(1L);
        AccessRight accessRight2 = new AccessRight();
        accessRight2.setId(accessRight1.getId());
        assertThat(accessRight1).isEqualTo(accessRight2);
        accessRight2.setId(2L);
        assertThat(accessRight1).isNotEqualTo(accessRight2);
        accessRight1.setId(null);
        assertThat(accessRight1).isNotEqualTo(accessRight2);
    }
}
