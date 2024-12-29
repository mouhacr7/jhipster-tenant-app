package com.tenant.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tenant.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Module.class);
        Module module1 = new Module();
        module1.setId(1L);
        Module module2 = new Module();
        module2.setId(module1.getId());
        assertThat(module1).isEqualTo(module2);
        module2.setId(2L);
        assertThat(module1).isNotEqualTo(module2);
        module1.setId(null);
        assertThat(module1).isNotEqualTo(module2);
    }
}
