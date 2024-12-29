package com.tenant.app.service.mapper;

import com.tenant.app.domain.License;
import com.tenant.app.domain.Module;
import com.tenant.app.service.dto.LicenseDTO;
import com.tenant.app.service.dto.ModuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link License} and its DTO {@link LicenseDTO}.
 */
@Mapper(componentModel = "spring")
public interface LicenseMapper extends EntityMapper<LicenseDTO, License> {
    @Mapping(target = "module", source = "module", qualifiedByName = "moduleId")
    LicenseDTO toDto(License s);

    @Named("moduleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModuleDTO toDtoModuleId(Module module);
}
