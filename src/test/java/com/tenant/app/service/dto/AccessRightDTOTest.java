package com.tenant.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tenant.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccessRightDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccessRightDTO.class);
        AccessRightDTO accessRightDTO1 = new AccessRightDTO();
        accessRightDTO1.setId(1L);
        AccessRightDTO accessRightDTO2 = new AccessRightDTO();
        assertThat(accessRightDTO1).isNotEqualTo(accessRightDTO2);
        accessRightDTO2.setId(accessRightDTO1.getId());
        assertThat(accessRightDTO1).isEqualTo(accessRightDTO2);
        accessRightDTO2.setId(2L);
        assertThat(accessRightDTO1).isNotEqualTo(accessRightDTO2);
        accessRightDTO1.setId(null);
        assertThat(accessRightDTO1).isNotEqualTo(accessRightDTO2);
    }
}
